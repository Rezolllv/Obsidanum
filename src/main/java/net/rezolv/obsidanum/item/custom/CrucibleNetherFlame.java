package net.rezolv.obsidanum.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class CrucibleNetherFlame extends Item {
    public CrucibleNetherFlame(Properties pProperties) {
        super(pProperties);
    }
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        // Используем getInventory() для доступа к инвентарю
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof CrucibleNetherFlame) {
                // Уменьшаем прочность на 1 каждую секунду
                if (random.nextInt(100) < 30) {
                    if (event.phase == TickEvent.Phase.END && player.tickCount % 20 == 0) { // Проверяем, что это конец тика и прошло 20 тиков (1 секунда)
                        stack.hurt(1, player.getRandom(), null);
                        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) { // Если прочность стала меньше 1, ломаем предмет
                            stack.setCount(0); // Удаляем предмет из инвентаря
                        }
                    }
                }
            }
        }
    }
}
