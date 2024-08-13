package net.rezolv.obsidanum.entity.meat_beetle.ai;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LightLayer;
import net.rezolv.obsidanum.entity.meat_beetle.MeetBeetle;

public class PanicByLightGoal extends Goal {
    private final MeetBeetle beetle;

    public PanicByLightGoal(MeetBeetle beetle) {
        this.beetle = beetle;
    }

    @Override
    public boolean canUse() {
        // Проверяем, если уровень света выше 6 и сущность не в состоянии паники
        return beetle.level().getBrightness(LightLayer.BLOCK, beetle.blockPosition()) > 5 || beetle.level().getBrightness(LightLayer.SKY, beetle.blockPosition()) > 9 && !beetle.isPanicking();
    }

    @Override
    public void start() {
        beetle.setPanicking(true); // Устанавливаем состояние паники
    }

    @Override
    public void tick() {
        // Переменные для хранения позиции источника света
        double lightSourceX = beetle.getX();
        double lightSourceZ = beetle.getZ();
        int maxLightLevel = 0;

        // Проверяем соседние блоки в радиусе 5 блоков
        for (int xOffset = -5; xOffset <= 5; xOffset++) {
            for (int zOffset = -5; zOffset <= 5; zOffset++) {
                // Получаем уровень света в соседнем блоке
                int lightLevel = beetle.level().getBrightness(LightLayer.BLOCK, beetle.blockPosition().offset(xOffset, 0, zOffset));

                // Проверяем, если уровень света выше текущего максимального
                if (lightLevel > maxLightLevel) {
                    maxLightLevel = lightLevel;
                    lightSourceX = beetle.getX() + xOffset;
                    lightSourceZ = beetle.getZ() + zOffset;
                }
            }
        }

        // Рассчитываем направление от источника света
        double directionX = beetle.getX() - lightSourceX;
        double directionZ = beetle.getZ() - lightSourceZ;

        // Нормализуем вектор направления
        double length = Math.sqrt(directionX * directionX + directionZ * directionZ);
        if (length != 0) {
            directionX /= length;
            directionZ /= length;
        }

        // Перемещаем жука в сторону темноты
        double targetX = beetle.getX() + directionX * 1.5; // Увеличьте множитель для большей дистанции
        double targetZ = beetle.getZ() + directionZ * 1.5;

        beetle.getNavigation().moveTo(targetX, beetle.getY(), targetZ, 1.5);
    }

    @Override
    public void stop() {
        beetle.setPanicking(false); // Сбрасываем состояние паники
    }
}





