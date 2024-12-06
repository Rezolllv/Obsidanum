package net.rezolv.obsidanum.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.projectile_functions.StrikeNetherFlame;

@Mod.EventBusSubscriber
public class NetherFlame extends Item {
    public NetherFlame(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack itemstack) {
        return false;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        ItemStack retval = new ItemStack(this);
        retval.setDamageValue(itemstack.getDamageValue() + 1);
        if (retval.getDamageValue() >= retval.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return retval;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);

        StrikeNetherFlame.execute(entity,level,entity.getX(),entity.getY(),entity.getZ());

        // Кулдаун
        entity.getCooldowns().addCooldown(this, 100);
        return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        // Проверяем каждый слот инвентаря
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof NetherFlame) {
                // Проверяем, прошло ли уже минута с последнего поджога
                if (player.tickCount % (20 * 60) == 0) { // 20 тиков в секунду * 60 секунд = 1 минута
                    // Генерируем случайное число от 0 до 99
                    int chance = player.getRandom().nextInt(100);
                    // Если случайное число меньше 20, поджигаем игрока
                    if (chance < 20) {
                        player.setSecondsOnFire(5); // Поджигаем игрока на 5 секунд
                    }
                }
            }
        }
    }
}
