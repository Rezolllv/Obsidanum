package net.rezolv.obsidanum.effect.effects.effect_overlay;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.effect.EffectsObs;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.fog.PotGrenadeFog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID)
public class FogEffectHandler {
    private static final Map<UUID, PlayerFogState> SERVER_PLAYER_STATES = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.player.level().isClientSide) return;

        if (event.player instanceof ServerPlayer player) {
            UUID playerId = player.getUUID();
            PlayerFogState state = SERVER_PLAYER_STATES.computeIfAbsent(playerId, k -> new PlayerFogState());

            boolean inFog = isPlayerInFog(player);
            if (inFog) {
                state.fogExposureTime = Math.min(state.fogExposureTime + 1, PlayerFogState.MAX_EXPOSURE_TIME_LVL2);
            } else {
                state.fogExposureTime = Math.max(state.fogExposureTime - 1, 0);
            }

            if (state.fogExposureTime >= PlayerFogState.MAX_EXPOSURE_TIME_LVL2 && state.appliedEffectLevel < 2) {
                player.addEffect(new MobEffectInstance(EffectsObs.FLASH.get(), PlayerFogState.EFFECT_DURATION, 1));
                state.appliedEffectLevel = 2;
            } else if (state.fogExposureTime >= PlayerFogState.MAX_EXPOSURE_TIME_LVL1 && state.appliedEffectLevel < 1) {
                player.addEffect(new MobEffectInstance(EffectsObs.FLASH.get(), PlayerFogState.EFFECT_DURATION, 0));
                state.appliedEffectLevel = 1;
            } else if (state.fogExposureTime <= 0 && state.appliedEffectLevel > 0) {
                player.removeEffect(EffectsObs.FLASH.get());
                state.appliedEffectLevel = 0;
            }
        }
    }

    private static boolean isPlayerInFog(ServerPlayer player) {
        // Реализация проверки нахождения в тумане на сервере
        return !player.level().getEntitiesOfClass(PotGrenadeFog.class, player.getBoundingBox())
                .isEmpty();
    }
}
