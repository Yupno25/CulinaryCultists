package net.yupno.culinarycultists.entity.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SeethEntity extends Monster implements GeoEntity {
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int hoverTime;
    private int clientSideAttackTime;
    @Nullable
    protected RandomStrollGoal randomStrollGoal;
    @Nullable
    private LivingEntity clientSideCachedAttackTarget;
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(SeethEntity.class, EntityDataSerializers.INT);

    public SeethEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, false);
    }

    // Very Important! Needs to be called in ModEvents
    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.FLYING_SPEED, (double)0.4F)
                .add(Attributes.MOVEMENT_SPEED, (double)0.4f).build();
    }

    @Override
    protected void registerGoals() {
        this.randomStrollGoal = new RandomStrollGoal(this, 1.0D, 80);
        
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, randomStrollGoal);
        this.goalSelector.addGoal(3, new SeethEntity.SeethAttackGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public void aiStep() {
        BlockPos blockPos = new BlockPos(new Vec3i((int)this.position().x, (int)this.position().y, (int)this.position().z));
        if(!this.level.getBlockState(blockPos.below(2)).isAir() || !this.level.getBlockState(blockPos.below()).isAir() || this.onGround){
            if(hoverTime <= 0){
                this.setDeltaMovement(new Vec3(this.getDeltaMovement().x, 0.3D, this.getDeltaMovement().z));
            }else {
                hoverTime = this.random.nextInt(6);
            }
        }

        super.aiStep();
    }

    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    private PlayState predicate(AnimationState animationState) {
        if(false) { //animationState.isMoving()
            animationState.getController().setAnimation(RawAnimation.begin().then("animation.seeth.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        animationState.getController().setAnimation(RawAnimation.begin().then("animation.seeth.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
/*
    private PlayState attackPredicate(AnimationState state) {
        if(this.swinging && state.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            state.getController().forceAnimationReset();
            state.getController().setAnimation(RawAnimation.begin().then("animation.seeth.attack", Animation.LoopType.PLAY_ONCE));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }
 */

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller",
                0, this::predicate));
        /*
        controllers.add(new AnimationController(this, "attackController",
                0, this::attackPredicate));
         */
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.PHANTOM_FLAP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WARDEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.5F;
    }

    static class SeethAttackGoal extends Goal {
        private final SeethEntity seeth;
        private int attackTime;

        public SeethAttackGoal(SeethEntity pSeeth) {
            this.seeth = pSeeth;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity target = this.seeth.getTarget();
            return target != null && target.isAlive();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.seeth.getTarget() != null && this.seeth.distanceToSqr(this.seeth.getTarget()) > 9.0D);
        }

        public void start() {
            this.attackTime = -10;
            this.seeth.getNavigation().stop();
            LivingEntity livingentity = this.seeth.getTarget();
            if (livingentity != null) {
                this.seeth.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
            }

            this.seeth.hasImpulse = true;
        }

        public void stop() {
            this.seeth.setActiveAttackTarget(0);
            this.seeth.setTarget((LivingEntity)null);
            this.seeth.randomStrollGoal.trigger();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        // Handles attack beam logic (the renderer does the texture)
        public void tick() {
            LivingEntity target = this.seeth.getTarget();
            if (target != null) {
                this.seeth.getNavigation().stop();
                this.seeth.getLookControl().setLookAt(target, 90.0F, 90.0F);
                if (!this.seeth.hasLineOfSight(target)) {
                    this.seeth.setTarget((LivingEntity)null);
                } else {
                    ++this.attackTime;
                    if (this.attackTime == 0) {
                        this.seeth.setActiveAttackTarget(target.getId());
                        if (!this.seeth.isSilent()) {
                            this.seeth.level.broadcastEntityEvent(this.seeth, (byte)21);
                        }
                    } else if (this.attackTime >= seeth.getAttackDuration()) {
                        for (MobEffectInstance mobEffectInstance: target.getActiveEffects()) {
                            if(mobEffectInstance.getEffect().getDisplayName().getString().equals("Weakness")){
                                int amplifier = mobEffectInstance.getAmplifier() >= 1 ? mobEffectInstance.getAmplifier() : mobEffectInstance.getAmplifier() +1;
                                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100 + 80 * amplifier, amplifier));
                            }
                            else if(mobEffectInstance.getEffect().getDisplayName().getString().equals("Slowness")){
                                int amplifier = mobEffectInstance.getAmplifier() >= 4 ? mobEffectInstance.getAmplifier() : mobEffectInstance.getAmplifier() +1;
                                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80 + 80 * amplifier, amplifier));
                            }
                        }

                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));

                        this.seeth.setTarget((LivingEntity)null);
                    }

                    super.tick();
                }
            }
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_ATTACK_TARGET, 0);
    }

    void setActiveAttackTarget(int pActiveAttackTargetId) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, pActiveAttackTargetId);
    }

    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level.isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level.getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity)entity;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (DATA_ID_ATTACK_TARGET.equals(pKey)) {
            this.clientSideAttackTime = 0;
            this.clientSideCachedAttackTarget = null;
        }

    }

    public float getAttackAnimationScale(float pPartialTick) {
        return ((float)this.clientSideAttackTime + pPartialTick) / (float)this.getAttackDuration();
    }

    public float getClientSideAttackTime() {
        return (float)this.clientSideAttackTime;
    }

    public int getAttackDuration() {
        return 100;
    }
}
