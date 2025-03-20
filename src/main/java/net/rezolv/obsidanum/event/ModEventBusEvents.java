package net.rezolv.obsidanum.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.block.ModFlammableBlocks;
import net.rezolv.obsidanum.block.entity.renderer.PranaCrystallRenderer;
import net.rezolv.obsidanum.effect.EffectsObs;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.gart.Gart;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetle;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;

@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributesElemental(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OBSIDIAN_ELEMENTAL.get(), ObsidianElemental.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerAttributesBeetle(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MEET_BEETLE.get(), MeetBeetle.createAttributes().build());
    }
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModFlammableBlocks::registerFlammableBlocks);
    }

    @SubscribeEvent
    public static void registerAttributesGart(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GART.get(), Gart.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerAttributesMutatedGart(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MUTATED_GART.get(), MutatedGart.createAttributes().build());
    }

}