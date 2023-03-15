package red.jad.headdowndisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.headdowndisplay.backend.HudAnimationHandler;
import red.jad.headdowndisplay.backend.HudConditionHandler;

import java.util.Objects;

@Mixin(Gui.class)
public class GuiMixin {

	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "tick()V", at = @At("RETURN"))
	private void injectIntoTick(CallbackInfo ci){
		if(Objects.nonNull(minecraft) && Objects.nonNull(minecraft.player))
			HudConditionHandler.tick(minecraft.player);
	}

	// Hotbar
	@Inject( method = "renderEffects", at = @At(value = "HEAD") )
	private void translateEffects(PoseStack matrix, CallbackInfo ci){
		HudAnimationHandler.preInject(matrix);
	}
	@Inject( method = "renderEffects", at = @At(value = "RETURN") )
	private void cancelEffects(PoseStack matrix, CallbackInfo ci){
		HudAnimationHandler.postInject(matrix);
	}

	// Hotbar
	@Inject( method = "renderHotbar", at = @At(value = "HEAD") )
	private void translateHotbar(final float f, final PoseStack matrix, final CallbackInfo ci){
		HudAnimationHandler.preInject(matrix);
	}
	@Inject( method = "renderHotbar", at = @At(value = "RETURN") )
	private void cancelHotbar(final float f, final PoseStack matrix, final CallbackInfo ci){
		HudAnimationHandler.postInject(matrix);
	}

	// Tooltip
	@Inject( method = "renderSelectedItemName", at = @At(value = "HEAD") )
	private void translateTooltip(PoseStack matrix, CallbackInfo ci){
		HudAnimationHandler.preInject(matrix);
	}
	@Inject( method = "renderSelectedItemName", at = @At(value = "RETURN") )
	private void cancelTooltip(PoseStack matrix, CallbackInfo ci){
		HudAnimationHandler.postInject(matrix);
	}

	// Experience Bar
	@Inject( method = "renderExperienceBar", at = @At(value = "HEAD") )
	private void translateExperienceBar(final PoseStack matrix, final int x, final CallbackInfo ci){
		HudAnimationHandler.preInject(matrix);
	}

	@Inject( method = "renderExperienceBar", at = @At(value = "RETURN") )
	private void exitExperienceBar(final PoseStack matrix, final int x, final CallbackInfo ci){
		HudAnimationHandler.postInject(matrix);
	}

	// Health bar
	@Inject( method = "renderPlayerHealth", at = @At(value = "HEAD") )
	private void translateHearts(PoseStack matrix, final CallbackInfo ci){
		HudAnimationHandler.preInject(matrix);
	}

	@Inject( method = "renderPlayerHealth", at = @At(value = "RETURN") )
	private void exitHearts(PoseStack matrix, CallbackInfo ci){
		HudAnimationHandler.postInject(matrix);
	}

	// Mount Health
	@Inject( method = "renderVehicleHealth", at = @At(value = "HEAD") )
	private void translateMountHealth(PoseStack matrix, CallbackInfo ci){
		HudAnimationHandler.preInject(matrix);
	}
	@Inject( method = "renderVehicleHealth", at = @At(value = "RETURN") )
	private void exitMountHealth(PoseStack matrix, CallbackInfo ci){
		HudAnimationHandler.postInject(matrix);
	}

	// Mount Jump Bar
	@Inject( method = "renderJumpMeter", at = @At(value = "HEAD") )
	private void translateMountJumpBar(PoseStack matrix, int x, CallbackInfo ci){
		HudAnimationHandler.preInject(matrix);
	}
	@Inject( method = "renderJumpMeter", at = @At(value = "RETURN") )
	private void exitMountJumpBar(PoseStack matrix, int x, CallbackInfo ci){
		HudAnimationHandler.postInject(matrix);
	}
}
