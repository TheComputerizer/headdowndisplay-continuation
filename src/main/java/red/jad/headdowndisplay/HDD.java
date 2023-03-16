package red.jad.headdowndisplay;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import red.jad.headdowndisplay.backend.HudAnimationHandler;
import red.jad.headdowndisplay.config.DefaultConfig;

public class HDD implements ClientModInitializer {

    public static final String MOD_ID = "headdowndisplay";
    public static DefaultConfig config;
    private static KeyMapping keyShowHud;

    @Override
	public void onInitializeClient() {
        HudRenderCallback.EVENT.register((matrixStack, delta) -> {
            if(HDD.config.isEnabled()) HudAnimationHandler.render(delta);
        });

        keyShowHud = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key." + MOD_ID + ".showhud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.categories." + MOD_ID
        ){});

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyShowHud.isDown() && config.isEnabled()) {
                HudAnimationHandler.revealHud();
            }
        });
        /*if(FabricLoader.getInstance().getModContainer("cloth-config").isPresent())
            //config = AutoConfig.register(AutoConfigIntegration.class, GsonConfigSerializer::new).getConfig();
        else*/
        config = new DefaultConfig();
	}
}
