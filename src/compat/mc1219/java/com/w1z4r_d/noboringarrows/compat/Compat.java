package com.w1z4r_d.noboringarrows.compat;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

/** Місця, де API відрізняється між версіями Minecraft (mc1219). */
public final class Compat {
    private Compat() {
    }

    public static KeyMapping registerKey(String name, int key, String namespace, String category) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(name, InputConstants.Type.KEYSYM, key,
                KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(namespace, category))));
    }

    public static void setScreen(Minecraft minecraft, Screen screen) {
        minecraft.setScreen(screen);
    }
}
