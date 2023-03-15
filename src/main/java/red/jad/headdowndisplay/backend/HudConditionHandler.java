package red.jad.headdowndisplay.backend;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import red.jad.headdowndisplay.HDD;
import red.jad.headdowndisplay.config.DefaultConfig;

import java.util.Objects;

import static red.jad.headdowndisplay.backend.HudAnimationHandler.revealHud;

public class HudConditionHandler {

    private static int previousSelected;
    private static int previousHealth;
    private static int previousMountHealth;
    private static int previousHunger;
    private static int previousArmor;
    private static int previousAir;
    private static int previousStatusEffects;

    public static void tick(LocalPlayer player) {
        if(HDD.config.revealSlot()){
            if(player.getInventory().selected != previousSelected) revealHud();
            previousSelected = player.getInventory().selected;
        }

        if(!player.isCreative() && HDD.config.revealStatusEffects()){
            if(player.getActiveEffects().size() != previousStatusEffects) revealHud();
            previousStatusEffects = player.getActiveEffects().size();
        }

        if(!player.isCreative() && HDD.config.revealHealth() != DefaultConfig.Change.never){
            revealDelta((int)player.getHealth(), previousHealth, HDD.config.revealHealth());
            previousHealth = (int)player.getHealth();
        }

        if(player.isPassenger() && player.getVehicle() instanceof LivingEntity living && HDD.config.revealMountHealth() != DefaultConfig.Change.never) {
            revealDelta((int)living.getHealth(), previousMountHealth, HDD.config.revealMountHealth());
            previousMountHealth = (int)living.getHealth();
        }

        if(Objects.nonNull(player.jumpableVehicle()) && HDD.config.revealJumpbarChange()) revealHud();

        if(!player.isCreative() && HDD.config.revealHunger() != DefaultConfig.Change.never){
            revealDelta(player.getFoodData().getFoodLevel(), previousHunger, HDD.config.revealHunger());
            previousHunger = player.getFoodData().getFoodLevel();
        }

        if(!player.isCreative() && HDD.config.revealArmor() != DefaultConfig.Change.never){
            revealDelta(player.getArmorValue(), previousArmor, HDD.config.revealArmor());
            previousArmor = player.getArmorValue();
        }

        if(!player.isCreative() && HDD.config.revealAir() != DefaultConfig.Change.never){
            revealDelta(player.getAirSupply(), previousAir, HDD.config.revealAir());
            previousAir = player.getAirSupply();
        }
    }

    public static void revealDelta(float newer, float older, DefaultConfig.Change change){
        if(newer != older && change != DefaultConfig.Change.never){
            if(newer > older && change != DefaultConfig.Change.decrease) revealHud(); // show inc
            if(newer < older && change != DefaultConfig.Change.increase) revealHud(); // show dec
        }
    }
}
