package com.w1z4r_d.noboringarrows;

import com.mojang.blaze3d.platform.InputConstants;
import com.w1z4r_d.noboringarrows.compat.Compat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoBoringArrowsClient implements ClientModInitializer {
    public static final String MOD_ID = "noboringarrows";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        StuckConfig.get();

        toggleKey = Compat.registerKey("key.noboringarrows.toggle", InputConstants.KEY_J, MOD_ID, "main");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                StuckConfig config = StuckConfig.get();
                config.enabled = !config.enabled;
                config.save();
            }
        });
    }
}
