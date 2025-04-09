package net.rezolv.obsidanum.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.effect.effects.Confusion;
import net.rezolv.obsidanum.effect.effects.Inviolability;
import net.rezolv.obsidanum.effect.effects.ProtectionArrowEffect;

public class EffectsObs {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Obsidanum.MOD_ID);

    public static final RegistryObject<MobEffect> INVIOLABILITY = MOB_EFFECTS.register("inviolability",
            () -> new Inviolability(MobEffectCategory.HARMFUL, 0x6d3f5b));
    public static final RegistryObject<MobEffect> PROTECTION_ARROW = MOB_EFFECTS.register("protection_arrow",
            () -> new ProtectionArrowEffect(MobEffectCategory.HARMFUL, 0xFFD700));
    public static final RegistryObject<MobEffect> FLASH = MOB_EFFECTS.register("flash",
            () -> new Confusion(MobEffectCategory.HARMFUL, 0x444444));

}
