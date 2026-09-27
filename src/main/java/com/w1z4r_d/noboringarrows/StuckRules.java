package com.w1z4r_d.noboringarrows;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

/** Вирішує, скільки стріл/жал рендерити на конкретному гравці. */
public final class StuckRules {
    public enum Kind { ARROW, STINGER }

    private StuckRules() {
    }

    public static int limit(int count, AvatarRenderState state, Kind kind) {
        if (count <= 0) {
            return count;
        }
        StuckConfig config = StuckConfig.get();
        if (!config.enabled) {
            return count;
        }
        int max = kind == Kind.ARROW ? config.maxArrows : config.maxStingers;
        if (max < 0) {
            return count;
        }
        if (!config.scope.appliesTo(isLocalPlayer(state))) {
            return count;
        }
        // 0 -> шар узагалі не малює жодного об'єкта, цикл рендеру не запускається
        return Math.min(count, max);
    }

    private static boolean isLocalPlayer(AvatarRenderState state) {
        var player = Minecraft.getInstance().player;
        return player != null && state.id == player.getId();
    }
}
