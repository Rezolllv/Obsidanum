package net.rezolv.obsidanum.item.item_entity.pot_grenade.fog;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.rezolv.obsidanum.item.entity.ModEntitiesItem;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PotGrenadeFog extends Entity implements TraceableEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int TIME_BETWEEN_APPLICATIONS = 5;
    private static final EntityDataAccessor<Float> DATA_RADIUS;
    private static final EntityDataAccessor<Integer> DATA_COLOR;
    private static final EntityDataAccessor<Boolean> DATA_WAITING;
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE;
    private static final float MAX_RADIUS = 32.0F;
    private static final float MINIMAL_RADIUS = 0.5F;
    private static final float DEFAULT_RADIUS = 3.0F;
    public static final float DEFAULT_WIDTH = 6.0F;
    public static final float HEIGHT = 0.5F;
    private Potion potion;
    private final List<MobEffectInstance> effects;
    private final Map<Entity, Integer> victims;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean fixedColor;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public PotGrenadeFog(EntityType<? extends PotGrenadeFog> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.potion = Potions.EMPTY;
        this.effects = Lists.newArrayList();
        this.victims = Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noPhysics = true;
    }

    public PotGrenadeFog(Level pLevel, double pX, double pY, double pZ) {
        this(ModEntitiesItem.POT_GRENADE_FOG.get(), pLevel);
        this.setPos(pX, pY, pZ);
    }

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_COLOR, 0);
        this.getEntityData().define(DATA_RADIUS, 3.0F);
        this.getEntityData().define(DATA_WAITING, false);
        this.getEntityData().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }

    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(pRadius, 0.0F, 32.0F));
        }

    }

    public void refreshDimensions() {
        double $$0 = this.getX();
        double $$1 = this.getY();
        double $$2 = this.getZ();
        super.refreshDimensions();
        this.setPos($$0, $$1, $$2);
    }

    public float getRadius() {
        return (Float)this.getEntityData().get(DATA_RADIUS);
    }

    public void setPotion(Potion pPotion) {
        this.potion = pPotion;
        if (!this.fixedColor) {
            this.updateColor();
        }

    }

    private void updateColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.getEntityData().set(DATA_COLOR, 0);
        } else {
            this.getEntityData().set(DATA_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
        }

    }

    public void addEffect(MobEffectInstance pEffectInstance) {
        this.effects.add(pEffectInstance);
        if (!this.fixedColor) {
            this.updateColor();
        }

    }

    public int getColor() {
        return (Integer)this.getEntityData().get(DATA_COLOR);
    }

    public void setFixedColor(int pColor) {
        this.fixedColor = true;
        this.getEntityData().set(DATA_COLOR, pColor);
    }

    public ParticleOptions getParticle() {
        return (ParticleOptions)this.getEntityData().get(DATA_PARTICLE);
    }

    public void setParticle(ParticleOptions pParticleOption) {
        this.getEntityData().set(DATA_PARTICLE, pParticleOption);
    }

    protected void setWaiting(boolean pWaiting) {
        this.getEntityData().set(DATA_WAITING, pWaiting);
    }

    public boolean isWaiting() {
        return (Boolean)this.getEntityData().get(DATA_WAITING);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int pDuration) {
        this.duration = pDuration;
    }

    public void tick() {
        super.tick();
        boolean $$0 = this.isWaiting();
        float $$1 = this.getRadius();
        if (this.level().isClientSide) {
            if ($$0 && this.random.nextBoolean()) {
                return;
            }

            ParticleOptions $$2 = this.getParticle();
            int $$3;
            float $$4;
            if ($$0) {
                $$3 = 2;
                $$4 = 0.2F;
            } else {
                $$3 = Mth.ceil((float)Math.PI * $$1 * $$1);
                $$4 = $$1;
            }

            for(int $$7 = 0; $$7 < $$3; ++$$7) {
                float $$8 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float $$9 = Mth.sqrt(this.random.nextFloat()) * $$4;
                double $$10 = this.getX() + (double)(Mth.cos($$8) * $$9);
                double $$11 = this.getY();
                double $$12 = this.getZ() + (double)(Mth.sin($$8) * $$9);
                double $$14;
                double $$15;
                double $$16;
                if ($$2.getType() == ParticleTypes.ENTITY_EFFECT) {
                    int $$13 = $$0 && this.random.nextBoolean() ? 16777215 : this.getColor();
                    $$14 = (double)((float)($$13 >> 16 & 255) / 255.0F);
                    $$15 = (double)((float)($$13 >> 8 & 255) / 255.0F);
                    $$16 = (double)((float)($$13 & 255) / 255.0F);
                } else if ($$0) {
                    $$14 = (double)0.0F;
                    $$15 = (double)0.0F;
                    $$16 = (double)0.0F;
                } else {
                    $$14 = ((double)0.5F - this.random.nextDouble()) * 0.15;
                    $$15 = (double)0.01F;
                    $$16 = ((double)0.5F - this.random.nextDouble()) * 0.15;
                }

                this.level().addAlwaysVisibleParticle($$2, $$10, $$11, $$12, $$14, $$15, $$16);
            }
        } else {
            if (this.tickCount >= this.waitTime + this.duration) {
                this.discard();
                return;
            }

            boolean $$23 = this.tickCount < this.waitTime;
            if ($$0 != $$23) {
                this.setWaiting($$23);
            }

            if ($$23) {
                return;
            }

            if (this.radiusPerTick != 0.0F) {
                $$1 += this.radiusPerTick;
                if ($$1 < 0.5F) {
                    this.discard();
                    return;
                }

                this.setRadius($$1);
            }

            if (this.tickCount % 5 == 0) {
                this.victims.entrySet().removeIf((p_287380_) -> this.tickCount >= (Integer)p_287380_.getValue());
                List<MobEffectInstance> $$24 = Lists.newArrayList();

                for(MobEffectInstance $$25 : this.potion.getEffects()) {
                    $$24.add(new MobEffectInstance($$25.getEffect(), $$25.mapDuration((p_267926_) -> p_267926_ / 4), $$25.getAmplifier(), $$25.isAmbient(), $$25.isVisible()));
                }

                $$24.addAll(this.effects);
                if ($$24.isEmpty()) {
                    this.victims.clear();
                } else {
                    List<LivingEntity> $$26 = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                    if (!$$26.isEmpty()) {
                        for(LivingEntity $$27 : $$26) {
                            if (!this.victims.containsKey($$27) && $$27.isAffectedByPotions()) {
                                double $$28 = $$27.getX() - this.getX();
                                double $$29 = $$27.getZ() - this.getZ();
                                double $$30 = $$28 * $$28 + $$29 * $$29;
                                if ($$30 <= (double)($$1 * $$1)) {
                                    this.victims.put($$27, this.tickCount + this.reapplicationDelay);

                                    for(MobEffectInstance $$31 : $$24) {
                                        if ($$31.getEffect().isInstantenous()) {
                                            $$31.getEffect().applyInstantenousEffect(this, this.getOwner(), $$27, $$31.getAmplifier(), (double)0.5F);
                                        } else {
                                            $$27.addEffect(new MobEffectInstance($$31), this);
                                        }
                                    }

                                    if (this.radiusOnUse != 0.0F) {
                                        $$1 += this.radiusOnUse;
                                        if ($$1 < 0.5F) {
                                            this.discard();
                                            return;
                                        }

                                        this.setRadius($$1);
                                    }

                                    if (this.durationOnUse != 0) {
                                        this.duration += this.durationOnUse;
                                        if (this.duration <= 0) {
                                            this.discard();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public float getRadiusOnUse() {
        return this.radiusOnUse;
    }

    public void setRadiusOnUse(float pRadiusOnUse) {
        this.radiusOnUse = pRadiusOnUse;
    }

    public float getRadiusPerTick() {
        return this.radiusPerTick;
    }

    public void setRadiusPerTick(float pRadiusPerTick) {
        this.radiusPerTick = pRadiusPerTick;
    }

    public int getDurationOnUse() {
        return this.durationOnUse;
    }

    public void setDurationOnUse(int pDurationOnUse) {
        this.durationOnUse = pDurationOnUse;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int pWaitTime) {
        this.waitTime = pWaitTime;
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        this.owner = pOwner;
        this.ownerUUID = pOwner == null ? null : pOwner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity $$0 = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if ($$0 instanceof LivingEntity) {
                this.owner = (LivingEntity)$$0;
            }
        }

        return this.owner;
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age");
        this.duration = pCompound.getInt("Duration");
        this.waitTime = pCompound.getInt("WaitTime");
        this.reapplicationDelay = pCompound.getInt("ReapplicationDelay");
        this.durationOnUse = pCompound.getInt("DurationOnUse");
        this.radiusOnUse = pCompound.getFloat("RadiusOnUse");
        this.radiusPerTick = pCompound.getFloat("RadiusPerTick");
        this.setRadius(pCompound.getFloat("Radius"));
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }

        if (pCompound.contains("Particle", 8)) {
            try {
                this.setParticle(ParticleArgument.readParticle(new StringReader(pCompound.getString("Particle")), BuiltInRegistries.PARTICLE_TYPE.asLookup()));
            } catch (CommandSyntaxException $$1) {
                LOGGER.warn("Couldn't load custom particle {}", pCompound.getString("Particle"), $$1);
            }
        }

        if (pCompound.contains("Color", 99)) {
            this.setFixedColor(pCompound.getInt("Color"));
        }

        if (pCompound.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotion(pCompound));
        }

        if (pCompound.contains("Effects", 9)) {
            ListTag $$2 = pCompound.getList("Effects", 10);
            this.effects.clear();

            for(int $$3 = 0; $$3 < $$2.size(); ++$$3) {
                MobEffectInstance $$4 = MobEffectInstance.load($$2.getCompound($$3));
                if ($$4 != null) {
                    this.addEffect($$4);
                }
            }
        }

    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Age", this.tickCount);
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("WaitTime", this.waitTime);
        pCompound.putInt("ReapplicationDelay", this.reapplicationDelay);
        pCompound.putInt("DurationOnUse", this.durationOnUse);
        pCompound.putFloat("RadiusOnUse", this.radiusOnUse);
        pCompound.putFloat("RadiusPerTick", this.radiusPerTick);
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putString("Particle", this.getParticle().writeToString());
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }

        if (this.fixedColor) {
            pCompound.putInt("Color", this.getColor());
        }

        if (this.potion != Potions.EMPTY) {
            pCompound.putString("Potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
        }

        if (!this.effects.isEmpty()) {
            ListTag $$1 = new ListTag();

            for(MobEffectInstance $$2 : this.effects) {
                $$1.add($$2.save(new CompoundTag()));
            }

            pCompound.put("Effects", $$1);
        }

    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(pKey);
    }

    public Potion getPotion() {
        return this.potion;
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    static {
        DATA_RADIUS = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.FLOAT);
        DATA_COLOR = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.INT);
        DATA_WAITING = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.BOOLEAN);
        DATA_PARTICLE = SynchedEntityData.defineId(AreaEffectCloud.class, EntityDataSerializers.PARTICLE);
    }
}