package net.rezolv.obsidanum.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ObsFieryInfusionTemplateItem extends SmithingTemplateItem {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "obsidanum");

    public ObsFieryInfusionTemplateItem(
            Component displayName,
            Component baseSlotDescription,
            Component addSlotDescription,
            Component baseSlotTooltip,
            Component addSlotTooltip,
            List<ResourceLocation> baseSlotIcons,
            List<ResourceLocation> addSlotIcons
    ) {
        super(displayName, baseSlotDescription, addSlotDescription, baseSlotTooltip, addSlotTooltip, baseSlotIcons, addSlotIcons);
    }


}
