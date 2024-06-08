package net.rezolv.obsidanum.chests;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.chests.client.TileEntityStoneChestRenderer;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SCEventHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(SCRegistry.CHEST_TILE_TYPE.get(), TileEntityStoneChestRenderer::new);
    }
}
