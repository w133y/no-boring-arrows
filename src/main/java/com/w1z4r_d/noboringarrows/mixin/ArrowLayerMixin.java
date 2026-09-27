package com.w1z4r_d.noboringarrows.mixin;

import com.w1z4r_d.noboringarrows.StuckRules;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowLayer.class)
public abstract class ArrowLayerMixin {
    @Inject(method = "numStuck", at = @At("RETURN"), cancellable = true)
    private void noboringarrows$limitArrows(AvatarRenderState state, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(StuckRules.limit(cir.getReturnValue(), state, StuckRules.Kind.ARROW));
    }
}
