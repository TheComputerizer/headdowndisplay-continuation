package red.jad.headdowndisplay.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.headdowndisplay.HDD;
import red.jad.headdowndisplay.backend.HudConditionHandler;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Shadow
    @Final
    protected Minecraft minecraft;

    // Exp
    @Inject(method = "setExperienceValues", at = @At(value = "HEAD"))
    private void expChanged(float currentXP, int maxXP, int level, CallbackInfo ci) {
        if (minecraft.player != null && !minecraft.player.isCreative()) {
            HudConditionHandler.revealDelta(level, minecraft.player.experienceLevel, HDD.config.revealExpLvl());
            HudConditionHandler.revealDelta(currentXP, minecraft.player.experienceProgress, HDD.config.revealExp());
        }
    }
}
