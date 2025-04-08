package net.rezolv.obsidanum.effect.effects.effect_overlay;

public class PlayerFogState {
    public float fogExposureTime = 0;
    public boolean isInFog = false;
    // 0 — эффект не применён, 1 — FLASH I, 2 — FLASH II
    public int appliedEffectLevel = 0;
    public static final float MAX_EXPOSURE_TIME_LVL1 = 8.0f * 20;  // 5 секунд
    public static final float MAX_EXPOSURE_TIME_LVL2 = 16.0f * 20; // 10 секунд
    public static final int EFFECT_DURATION = 20 * 20;              // 7 секунд
}