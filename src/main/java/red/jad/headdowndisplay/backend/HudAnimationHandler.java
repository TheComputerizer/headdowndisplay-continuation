package red.jad.headdowndisplay.backend;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import red.jad.headdowndisplay.HDD;

public class HudAnimationHandler {

    private static double y = 0;
    private static double speed = 0;

    public static double getY(){
        return -Math.min(Math.max(y, HDD.config.getMinY()), HDD.config.getMaxY());
    }

    private static long lastRevealed;
    public static void revealHud(){
        y = HDD.config.getMaxY();
        speed = 0;
        if(Minecraft.getInstance().level != null) lastRevealed = Minecraft.getInstance().level.getGameTime();
    }

    private static float now = 0;
    public static void render(float delta){
        if(Minecraft.getInstance().level != null){
            float before = now;
            now = Minecraft.getInstance().level.getGameTime() + delta;
            float tdelta = now - before;

            if(Minecraft.getInstance().level.getGameTime() - lastRevealed > HDD.config.getTimeVisible()*20 && y > HDD.config.getMinY()){
                speed += HDD.config.getSpeed() * tdelta;
                y -= speed * tdelta;
            }
        }
    }

    public static void preInject(final PoseStack matrices) {
        matrices.pushPose();
        matrices.translate(0, HudAnimationHandler.getY(), 0);
    }

    public static void postInject(final PoseStack matrices) {
        matrices.popPose();
    }
}
