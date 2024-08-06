package net.rezolv.obsidanum.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class Inviolability extends MobEffect {

    public Inviolability(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        // Проверка, является ли сущность игроком
        if (pLivingEntity instanceof Player player) {
            // Перебираем всех агрессивных мобов в области
            for (Entity entity : player.getCommandSenderWorld().getEntities(player, player.getBoundingBox().inflate(200.0D))) {
                if (entity instanceof Mob monster) {
                    // Устанавливаем агрессивность моба в false
                    monster.setTarget(null);
                    monster.setAggressive(false); // Устанавливаем нейтральное состояние

                }
            }
        }
    }
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
