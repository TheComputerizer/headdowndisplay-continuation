package red.jad.headdowndisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.headdowndisplay.backend.HudAnimationHandler;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject( method = "render", at = @At(value = "HEAD") )
    private void translateItem(ItemStack itemStack, ItemDisplayContext transformType, boolean leftHand,
                               PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay,
                               BakedModel model, CallbackInfo ci){
        if(itemStack != ItemStack.EMPTY && transformType == ItemDisplayContext.GUI) {
            matrix.pushPose();
            matrix.translate(0, -1 * HudAnimationHandler.getY(), 0);
        }
    }
    @Inject( method = "render", at = @At(value = "RETURN") )
    private void cancelItem(ItemStack itemStack, ItemDisplayContext transformType, boolean leftHand,
                               PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay,
                               BakedModel model, CallbackInfo ci){
        if(itemStack != ItemStack.EMPTY && transformType == ItemDisplayContext.GUI) matrix.popPose();
    }

    @Inject( method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "HEAD") )
    private void translateItemDecorations(PoseStack matrix, Font font, ItemStack itemStack, int i, int j, String string, CallbackInfo ci){
        if(itemStack != ItemStack.EMPTY)
            HudAnimationHandler.preInject(matrix);
    }

    @Inject( method = "renderGuiItemDecorations(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "RETURN") )
    private void cancelItemDecorations(PoseStack matrix, Font font, ItemStack itemStack, int i, int j, String string, CallbackInfo ci){
        if(itemStack != ItemStack.EMPTY)
            HudAnimationHandler.postInject(matrix);
    }
}