package net.rezolv.obsidanum.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.rezolv.obsidanum.item.item_entity.arrows.netherite_bolt.NetheriteBolt;

public class NetheriteBoltItem extends ArrowItem {
    public NetheriteBoltItem(Properties properties) {
        super(properties);
    }
    @Override
    public AbstractArrow createArrow(Level world, ItemStack stack, LivingEntity shooter) {
        return new NetheriteBolt(world, shooter);
    }
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        int enchant = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow);
        return enchant <= 0 ? false : this.getClass() == NetheriteBoltItem.class;
    }

}

