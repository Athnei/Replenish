package com.atthnei.replenish;

import org.lwjgl.glfw.GLFW;
import net.minecraft.item.ItemStack;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.network.ClientPlayerEntity;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
                return i;
            }
        }

        return -1;
    }
}
