package com.atthnei.replenish;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

public class Replenish implements ModInitializer {

    public static final String MOD_ID = "replenish";

    private static MinecraftClient mc = MinecraftClient.getInstance();

    private KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            MOD_ID + ".key.replenish-key",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R,
            MOD_ID + ".key.categories"));

    private boolean wasKeySet = false;

    private int slotIndexBeforePress = 0;

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onEndTick);
    }

    private void onEndTick(MinecraftClient client) {
        if (keyBinding.isPressed()) {

            if (!wasKeySet) {

                slotIndexBeforePress = client.player.inventory.selectedSlot;
                int foodIndex = GetInventoryFoodIndex(client.player);

                if (foodIndex != -1) {
                    client.player.inventory.selectedSlot = foodIndex;
                    client.options.keyUse.setPressed(true);
                    wasKeySet = true;
                }
            }
        } else if (wasKeySet) {
            client.options.keyUse.setPressed(false);
            wasKeySet = false;
            client.player.inventory.selectedSlot = slotIndexBeforePress;
        }
    }

    private int GetInventoryFoodIndex(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemSlot = player.inventory.getStack(i);

            if (itemSlot.isFood()) {
                boolean isHarmful = IsHarmful(itemSlot);

                if (!isHarmful) {
                    return i;
                }
            }
        }

        return -1;
    }

    private boolean IsHarmful(ItemStack itemSlot) {
        List<Pair<StatusEffectInstance, Float>> statusEffects = itemSlot.getItem().getFoodComponent().getStatusEffects();
        Optional<Pair<StatusEffectInstance, Float>> first = statusEffects.stream().findFirst();

        if (first.isPresent()) {
            StatusEffect status = first.get().getFirst().getEffectType();

            boolean ko = StatusEffects.HUNGER.getTranslationKey() == status.getTranslationKey();

            return !status.isBeneficial();
        }

        return false;
    }
}
