package com.w1z4r_d.noboringarrows.compat;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

/** Місця, де API відрізняється між версіями Minecraft (mc262). */
public final class Compat {
    private Compat() {
    }

    public static KeyMapping registerKey(String name, int key, String namespace, String category) {
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(name, key,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath(namespace, category))));
    }

    public static void setScreen(Minecraft minecraft, Screen screen) {
        minecraft.setScreenAndShow(screen);
    }
}
