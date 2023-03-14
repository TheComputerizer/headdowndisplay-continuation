package red.jad.headdowndisplay;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFW;
import red.jad.headdowndisplay.backend.HudAnimationHandler;
import red.jad.headdowndisplay.config.AutoConfigIntegration;
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
        if(FabricLoader.getInstance().getModContainer("cloth-config").isPresent())
            config = AutoConfig.register(AutoConfigIntegration.class, GsonConfigSerializer::new).getConfig();
        else {
            LogManager.getLogger().error("NO CLOTH!!!");
            config = new DefaultConfig();
        }
	}
}