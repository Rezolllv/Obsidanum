package net.rezolv.obsidanum.damage_source;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class ObsidianTotemDamageSource extends DamageSource {

    private final Entity directEntity;

    public ObsidianTotemDamageSource(Holder<DamageType> damageTypeHolder, Entity directEntity) {
        super(damageTypeHolder); // Передаём Holder<DamageType>
        this.directEntity = directEntity;
    }

    @Override
    public Entity getEntity() {
        return this.directEntity;
    }
}
