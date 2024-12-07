package net.rezolv.obsidanum.event;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.ModEntities;
import net.rezolv.obsidanum.entity.gart.Gart;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetle;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElemental;

@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
    public static void registerAttributesGart(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GART.get(), Gart.createAttributes().build());
    }

}