package com.w1z4r_d.noboringarrows.mixin;

import com.w1z4r_d.noboringarrows.StuckRules;
import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeStingerLayer.class)
public abstract class BeeStingerLayerMixin {
    @Inject(method = "numStuck", at = @At("RETURN"), cancellable = true)
    private void noboringarrows$limitStingers(AvatarRenderState state, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(StuckRules.limit(cir.getReturnValue(), state, StuckRules.Kind.STINGER));
    }
}
