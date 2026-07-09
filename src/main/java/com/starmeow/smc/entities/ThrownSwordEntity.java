package com.starmeow.smc.entities;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ThrownSwordEntity extends PathfinderMob implements ItemSupplier {

    private static final int OUTBOUND_TICKS = 10;
    private static final int RETURN_TICKS = 10;
    private static final int RETURN_DELAY = 3;
    private static final int HARD_MAX_LIFE = OUTBOUND_TICKS + RETURN_TICKS + RETURN_DELAY + 20;
    private static final double OUTBOUND_ELLIPSE_WIDTH = 5D;
    private static final double OUTBOUND_ELLIPSE_HEIGHT = 3D;

    private static final double RETURN_ELLIPSE_WIDTH = 5D;
    private static final double RETURN_ELLIPSE_HEIGHT = 3D;
    private static final double RETURN_ANGLE_MULTIPLIER = 1.0D;

    private int life = 0;
    private int returnLife = 0;

    private LivingEntity targetEntity;

    private Vec3 targetPos;
    private Vec3 startPos;
    private Vec3 returnStartPos;

    private boolean fixedDamage;
    private boolean returning = false;
    private final List<UUID> attackedEntityUUID = new ArrayList<>();

    private LivingEntity owner;
    private Vec3 launchDirection = Vec3.ZERO;
    private Vec3 launchRight = Vec3.ZERO;
    private Vec3 launchUp = Vec3.ZERO;

    private double randomPhase;
    private double returnPhase;

    public ThrownSwordEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        this.setNoAi(true);
        this.setNoGravity(true);
        this.setInvulnerable(true);

        this.noPhysics = true;

        this.randomPhase = Math.random() * Math.PI * 2.0D;
        this.returnPhase = this.randomPhase + Math.PI;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void checkInsideBlocks() {
    }

    @Override
    public void move(MoverType type, Vec3 movement) {
        this.setPos(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z);
    }

    public void setTargetPos(Vec3 targetPos) {
        this.targetPos = targetPos;
    }

    public void setFixedDamage(boolean fixedDamage){
        this.fixedDamage = fixedDamage;
    }

    @Override
    public void tick() {
        super.tick();

        this.life++;

        if (!this.level().isClientSide) {
            if (this.owner == null || this.owner.isRemoved() || this.life > HARD_MAX_LIFE) {
                this.discard();
                return;
            }
        }

        if (this.owner == null) {
            return;
        }

        if (this.startPos == null) {
            this.startPos = this.position();
        }

        if (this.launchDirection == null || this.launchDirection.lengthSqr() < 0.001D) {
            this.launchDirection = this.owner.getLookAngle().normalize();
        }

        this.ensureLaunchBasis();

        Vec3 currentPos = this.position();
        Vec3 nextPos;

        if (!this.returning) {
            nextPos = this.calculateOutboundPath();

            if (!this.level().isClientSide) {
                nextPos = this.checkForCollisions(nextPos);
            }
            if (!this.returning && this.life >= OUTBOUND_TICKS) {
                this.beginReturn(nextPos);
            }
        } else {
            nextPos = this.calculateReturnPath();
        }

        Vec3 motion = nextPos.subtract(currentPos);

        this.setDeltaMovement(motion);
        this.setPos(nextPos.x, nextPos.y, nextPos.z);

        this.updateSwordRotation(motion);

        this.hasImpulse = true;

        if (!this.level().isClientSide && this.returning) {
            if (this.returnLife >= RETURN_TICKS + RETURN_DELAY) {
                this.discard();
            }
        }
    }

    private void setupLaunchBasis() {
        Vec3 forward = this.launchDirection;

        if (forward == null || forward.lengthSqr() < 0.001D) {
            forward = new Vec3(0.0D, 0.0D, 1.0D);
        }

        forward = forward.normalize();

        Vec3 worldUp = new Vec3(0.0D, 1.0D, 0.0D);
        Vec3 right = forward.cross(worldUp);

        if (right.lengthSqr() < 0.001D) {
            right = new Vec3(1.0D, 0.0D, 0.0D);
        } else {
            right = right.normalize();
        }

        Vec3 up = right.cross(forward);

        if (up.lengthSqr() < 0.001D) {
            up = worldUp;
        } else {
            up = up.normalize();
        }

        this.launchRight = right;
        this.launchUp = up;
    }

    private void ensureLaunchBasis() {
        if (this.launchRight == null || this.launchUp == null
                || this.launchRight.lengthSqr() < 0.001D
                || this.launchUp.lengthSqr() < 0.001D) {
            this.setupLaunchBasis();
        }
    }

    private Vec3 calculateOutboundPath() {
        double rawT = Mth.clamp((double) this.life / (double) OUTBOUND_TICKS, 0.0D, 1.0D);
        double progress = smoothStep(rawT);

        Vec3 currentTargetPoint;

        if (this.targetEntity != null && !this.targetEntity.isRemoved()) {
            currentTargetPoint = this.targetEntity.position().add(0.0D, this.targetEntity.getBbHeight() * 0.5D, 0.0D);
            this.targetPos = currentTargetPoint;
        } else if (this.targetPos != null) {
            currentTargetPoint = this.targetPos;
        } else {
            currentTargetPoint = this.startPos.add(this.launchDirection.scale(30.0D));
            this.targetPos = currentTargetPoint;
        }

        Vec3 toTarget = currentTargetPoint.subtract(this.startPos);

        if (toTarget.lengthSqr() < 0.001D) {
            toTarget = this.launchDirection;
        }

        double totalDistance = toTarget.length();
        Vec3 forward = toTarget.normalize();

        Vec3 linearPos = this.startPos.add(forward.scale(totalDistance * progress));

        double offsetWeight = Math.sin(rawT * Math.PI);
        double angle = this.randomPhase;

        Vec3 offsetVec = this.calculateEllipseOffset(angle, offsetWeight, OUTBOUND_ELLIPSE_WIDTH, OUTBOUND_ELLIPSE_HEIGHT);

        return linearPos.add(offsetVec);
    }

    private Vec3 calculateReturnPath() {
        this.returnLife++;

        if (this.returnStartPos == null) {
            this.returnStartPos = this.position();
        }

        Vec3 ownerPos = this.owner.getEyePosition();

        double rawT = Mth.clamp((double) this.returnLife / (double) RETURN_TICKS, 0.0D, 1.0D);
        double progress = smoothStep(rawT);

        Vec3 linearPos = this.returnStartPos.lerp(ownerPos, progress);

        double offsetWeight = Math.sin(rawT * Math.PI);

        double angle = RETURN_ANGLE_MULTIPLIER + this.returnPhase;

        Vec3 offsetVec = this.calculateEllipseOffset(angle, offsetWeight, RETURN_ELLIPSE_WIDTH, RETURN_ELLIPSE_HEIGHT);

        return linearPos.add(offsetVec);
    }

    private Vec3 calculateEllipseOffset(double angle, double offsetWeight, double width, double height) {
        this.ensureLaunchBasis();

        double sideOffset = width * Math.cos(angle) * offsetWeight;
        double vertOffset = height * Math.sin(angle) * offsetWeight;

        return this.launchRight.scale(sideOffset).add(this.launchUp.scale(vertOffset));
    }

    private static double smoothStep(double t) {
        t = Mth.clamp(t, 0.0D, 1.0D);
        return t * t * (3.0D - 2.0D * t);
    }

    private void beginReturn(Vec3 fromPos) {
        if (this.returning) {
            return;
        }

        this.returning = true;
        this.returnLife = 0;
        this.returnStartPos = fromPos;

        this.returnPhase = this.randomPhase + Math.PI;
    }

    private Vec3 checkForCollisions(Vec3 nextPos) {
        AABB pathBox = new AABB(this.position(), nextPos).inflate(1.0D);
        List<LivingEntity> entities = this.level().getEntitiesOfClass(LivingEntity.class, pathBox);

        Vec3 correctedNextPos = nextPos;

        for (LivingEntity target : entities) {
            if (target == this.owner || target instanceof ThrownSwordEntity) {
                continue;
            }
            if (target.isRemoved() || !target.isAlive()) {
                continue;
            }
            if (this.attackedEntityUUID.contains(target.getUUID())) {
                continue;
            }
            if (!target.getBoundingBox().inflate(0.3D).clip(this.position(), nextPos).isPresent()) {
                continue;
            }

            this.attackedEntityUUID.add(target.getUUID());

            Vec3 hitPos = target.position().add(
                    0.0D,
                    target.getBbHeight() * 0.5D,
                    0.0D
            );
            this.targetPos = hitPos;
            this.hurtTargetBySword(target);

            if (target == this.targetEntity) {
                this.beginReturn(hitPos);
                correctedNextPos = hitPos;
                break;
            }
        }

        return correctedNextPos;
    }

    private void hurtTargetBySword(LivingEntity target) {
        target.invulnerableTime = 0;
        if(this.getOwner() instanceof Player player){
            target.setLastHurtByPlayer(player);
            if(this.fixedDamage){
                target.hurt(player.damageSources().playerAttack(player), (float)player.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
                return;
            }
        }
        boolean success = this.doHurtTarget(target);

        ItemStack weapon = this.getMainHandItem();

        if (weapon.getItem() instanceof SwordItem sword) {
            sword.hurtEnemy(weapon, target, this.getOwner() != null ? this.getOwner() : this);

            if (this.getOwner() instanceof Player player) {
                sword.onLeftClickEntity(weapon, player, target);
            }
        }
        if (success && this.owner instanceof Player player) {
            target.setLastHurtByPlayer(player);
        }
    }

    private void updateSwordRotation(Vec3 motion) {
        if (motion.lengthSqr() <= 0.001D) {
            return;
        }

        double horizontalDistance = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

        float targetYRot = (float) (Mth.atan2(motion.x, motion.z) * 180.0D / Math.PI);
        float targetXRot = (float) (Mth.atan2(motion.y, horizontalDistance) * 180.0D / Math.PI);

        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

        this.setYRot(targetYRot);
        this.setXRot(targetXRot);

        this.yBodyRot = targetYRot;
        this.yBodyRotO = targetYRot;
        this.yHeadRot = targetYRot;
        this.yHeadRotO = targetYRot;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    public void launchFromRotation(Entity shooter, float velocity, float divergence, LivingEntity initialTarget) {
        this.startPos = shooter.getEyePosition().add(Math.random() - 0.5F, Math.random() - 0.5F,Math.random() - 0.5F);
        this.launchDirection = shooter.getLookAngle().normalize();

        this.setupLaunchBasis();

        this.targetEntity = initialTarget;

        if (initialTarget != null) {
            this.targetPos = initialTarget.position().add(
                    0.0D,
                    initialTarget.getBbHeight() * 0.5D,
                    0.0D
            );
        } else {
            this.targetPos = this.startPos.add(this.launchDirection.scale(30.0D));
        }

        this.setPos(this.startPos.x, this.startPos.y, this.startPos.z);

        Vec3 initialMotion = this.launchDirection.scale(velocity);
        this.setDeltaMovement(initialMotion);

        double horizontalDistance = initialMotion.horizontalDistance();

        this.setYRot((float) (Mth.atan2(initialMotion.x, initialMotion.z) * 57.2957763671875D));
        this.setXRot((float) (Mth.atan2(initialMotion.y, horizontalDistance) * 57.2957763671875D));
    }

    @Override
    public ItemStack getItem() {
        return this.getMainHandItem();
    }
}