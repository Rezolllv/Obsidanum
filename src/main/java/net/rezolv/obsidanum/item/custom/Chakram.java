package net.rezolv.obsidanum.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.item.item_entity.obsidan_chakram.ObsidianChakramEntity;

public class Chakram extends Item {
    public Chakram(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);
        ItemStack itemStack = entity.getItemInHand(hand);

        // Server-side only logic
        if (!world.isClientSide) {
            // Create the projectile entity
            ObsidianChakramEntity chakramEntity = new ObsidianChakramEntity(world, entity);
            // Уменьшение количества предметов на 1
            if (!entity.isCreative()) {
                itemStack.shrink(1);
            }
            // Play throw sound
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            // Set the position and motion of the projectile
            Vec3 lookVec = entity.getLookAngle();
            chakramEntity.setPos(entity.getX(), entity.getEyeY() - (double)0.1F, entity.getZ());
            chakramEntity.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5F, 1.0F);

            // Add the projectile entity to the world
            world.addFreshEntity(chakramEntity);
        }
        // Кулдаун
        entity.getCooldowns().addCooldown(this, 30);
        return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }
}
