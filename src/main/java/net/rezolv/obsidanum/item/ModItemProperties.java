package net.rezolv.obsidanum.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.custom.FlameCrossbowItem;

public class ModItemProperties {
    /**
     * Регистрирует кастомные свойства предметов для анимаций и визуальных эффектов
     */
    public static void register() {
        // Регистрация свойств для огненного арбалета (аналогично ванильному арбалету)
        registerFlameCrossbowProperties();
    }

    private static void registerFlameCrossbowProperties() {
        // Анимация натяжения (от 0.0 до 1.0 в процессе натягивания)
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(),
                new ResourceLocation(Obsidanum.MOD_ID, "pull"),
                (itemStack, level, livingEntity, seed) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    }
                    boolean isCharged = FlameCrossbowItem.isCharged(itemStack);
                    int useDuration = itemStack.getUseDuration();
                    int remainingTicks = livingEntity.getUseItemRemainingTicks();

                    return isCharged ? 0.0F : (float)(useDuration - remainingTicks) /
                            (float)FlameCrossbowItem.getChargeDuration(itemStack);
                });

        // Состояние натягивания (1.0 когда игрок натягивает, 0.0 когда нет)
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(),
                new ResourceLocation(Obsidanum.MOD_ID, "pulling"),
                (itemStack, level, livingEntity, seed) -> {
                    boolean isPulling = livingEntity != null &&
                            livingEntity.isUsingItem() &&
                            livingEntity.getUseItem() == itemStack &&
                            !FlameCrossbowItem.isCharged(itemStack);

                    return isPulling ? 1.0F : 0.0F;
                });

        // Состояние заряженности (1.0 когда заряжен, 0.0 когда нет)
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(),
                new ResourceLocation(Obsidanum.MOD_ID, "charged"),
                (itemStack, level, livingEntity, seed) -> {
                    boolean isCharged = livingEntity != null &&
                            FlameCrossbowItem.isCharged(itemStack);

                    return isCharged ? 1.0F : 0.0F;
                });

        // Проверка на наличие фейерверка (1.0 если заряжен фейерверком)
        ItemProperties.register(ItemsObs.FLAME_CROSSBOW.get(),
                new ResourceLocation(Obsidanum.MOD_ID, "firework"),
                (itemStack, level, livingEntity, seed) -> {
                    boolean hasFirework = livingEntity != null &&
                            FlameCrossbowItem.isCharged(itemStack) &&
                            FlameCrossbowItem.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET);

                    return hasFirework ? 1.0F : 0.0F;
                });
    }
}