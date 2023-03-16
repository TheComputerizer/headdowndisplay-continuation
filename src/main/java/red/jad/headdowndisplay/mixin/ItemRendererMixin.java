package red.jad.headdowndisplay.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import red.jad.headdowndisplay.backend.HudAnimationHandler;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    //Items
    @Inject( method = "render", at = @At(value = "HEAD") )
    private void translateItem(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand,
                                  PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay,
                                  BakedModel model, CallbackInfo ci){
        if(itemStack != ItemStack.EMPTY && transformType == ItemTransforms.TransformType.GUI) {
            matrix.pushPose();
            matrix.translate(0, -1 * HudAnimationHandler.getY(), 0);
        }
    }
    @Inject( method = "render", at = @At(value = "RETURN") )
    private void cancelItem(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean leftHand,
                               PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay,
                               BakedModel model, CallbackInfo ci){
        if(itemStack != ItemStack.EMPTY && transformType == ItemTransforms.TransformType.GUI) matrix.popPose();
    }

    //Durability and usage bars
    @ModifyVariable( method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private int translateItemBars(int yPosition) {
        return (int)(yPosition+HudAnimationHandler.getY());
    }
}