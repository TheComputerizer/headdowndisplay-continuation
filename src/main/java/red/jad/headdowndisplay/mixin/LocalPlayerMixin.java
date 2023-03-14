package red.jad.headdowndisplay.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.headdowndisplay.HDD;
import red.jad.headdowndisplay.backend.HudAnimationHandler;
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

    // Jumpbar
    @Shadow
    public Input input;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getRootVehicle()Lnet/minecraft/world/entity/Entity;"))
    private void jumpbarChanged(CallbackInfo ci) {
        if (this.input.jumping && HDD.config.revealJumpbarChange()) HudAnimationHandler.revealHud();
    }

    // Mount health
    @Unique
    private int previousMountHealth;

    @Inject(method = "rideTick", at = @At(value = "RETURN"))
    private void mountHealthChange(CallbackInfo ci) {
        if (((LocalPlayer) (Object) this).getVehicle() instanceof LivingEntity en) {
            HudConditionHandler.revealDelta((int) en.getHealth(), previousMountHealth, HDD.config.revealMountHealth());
            previousMountHealth = (int) en.getHealth();
        }
    }
}
