package com.atthnei.replenish;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class ExtendedKeyBinding extends KeyBinding {
    public boolean WasUseKeySetPressed = false;

    public ExtendedKeyBinding(String translationKey, int code, String category) {
        super(translationKey, code, category);
    }

    public ExtendedKeyBinding(String translationKey, InputUtil.Type type, int code, String category) {
        super(translationKey, type, code, category);
    }
}
