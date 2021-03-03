package com.atthnei.replenish;

import com.atthnei.replenish.config.ReplenishConfig;
import com.mojang.datafixers.util.Pair;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Function;

public class Replenish implements ModInitializer {

    public static final String MOD_ID = "replenish";

    public static ReplenishConfig REPLENISH_CONFIG;

    private final MinecraftClient mc = MinecraftClient.getInstance();

    private final ExtendedKeyBinding replenishKeyBinding = (ExtendedKeyBinding) KeyBindingHelper.registerKeyBinding(new ExtendedKeyBinding(
            MOD_ID + ".key.replenish-key",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R,
            MOD_ID + ".key.categories"));

    private final ExtendedKeyBinding clearKeyBinding = (ExtendedKeyBinding) KeyBindingHelper.registerKeyBinding(new ExtendedKeyBinding(
            MOD_ID + ".key.clear-key",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
            MOD_ID + ".key.categories"));

    private int slotIndexBeforePress = 0;

    @Override
    public void onInitialize() {
        AutoConfig.register(ReplenishConfig.class, Toml4jConfigSerializer::new);
        REPLENISH_CONFIG = AutoConfig.getConfigHolder(ReplenishConfig.class).getConfig();

        ClientTickEvents.END_CLIENT_TICK.register(this::onEndTick);
    }

    private void onEndTick(MinecraftClient client) {
        KeyBindingPressed(client, replenishKeyBinding, this::DoesItemFitFoodConditions);
        KeyBindingPressed(client, clearKeyBinding, this::DoesItemFitClearConditions);
    }

    private void KeyBindingPressed(MinecraftClient client, ExtendedKeyBinding keyBinding, Function<Item, Boolean> condition) {
        assert client.player != null;

        if (keyBinding.isPressed()) {
            if (!keyBinding.WasUseKeySetPressed) {

                slotIndexBeforePress = client.player.inventory.selectedSlot;
                int hotbarIndex = GetHotbarIndex(client.player, condition);

                if (hotbarIndex != -1) {
                    client.player.inventory.selectedSlot = hotbarIndex;
                    client.options.keyUse.setPressed(true);
                    keyBinding.WasUseKeySetPressed = true;
                }
            }
        } else if (keyBinding.WasUseKeySetPressed) {
            client.options.keyUse.setPressed(false);
            client.player.inventory.selectedSlot = slotIndexBeforePress;
            keyBinding.WasUseKeySetPressed = false;
        }
    }

    private int GetHotbarIndex(ClientPlayerEntity player, Function<Item, Boolean> condition) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = player.inventory.getStack(i);

            if (condition.apply(itemStack.getItem())) {
                return i;
            }
        }

        return -1;
    }

    private boolean DoesItemFitFoodConditions(Item item) {
        if (!REPLENISH_CONFIG.ignoreList.isEmpty()) {
//        if (REPLENISH_CONFIG.isIgnoreListAllowed) {
            String itemId = Registry.ITEM.getId(item).toString();

            if (REPLENISH_CONFIG.ignoreList.contains(itemId)) return true;
        }

        if (REPLENISH_CONFIG.includePotions) {
            if (IsPotion(item)) return true;
        }

        if (item.isFood()) {
            if (REPLENISH_CONFIG.ignoreItemsWithHarmfulEffects) {
                return !IsHarmful(item);
            }

            return true;
        }

        return false;
    }

    private boolean DoesItemFitClearConditions(Item item) {
        String itemId = Registry.ITEM.getId(item).toString();

        return REPLENISH_CONFIG.clearList.contains(itemId);
    }

    private boolean IsHarmful(Item item) {
        List<Pair<StatusEffectInstance, Float>> statusEffects = item.getFoodComponent().getStatusEffects();

        for (Pair<StatusEffectInstance, Float> pair : statusEffects) {
            StatusEffect status = pair.getFirst().getEffectType();

            if (!status.isBeneficial()) return true;
        }

        return false;
    }

    private boolean IsPotion(Item item) {
        ItemGroup itemGroup = item.getGroup();

        if (itemGroup != null && itemGroup == ItemGroup.BREWING) {
            return item == Items.POTION;
        }

        return false;
    }
}
