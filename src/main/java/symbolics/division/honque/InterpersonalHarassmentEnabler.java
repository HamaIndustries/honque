package symbolics.division.honque;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class InterpersonalHarassmentEnabler extends ThrownItemEntity {
    public InterpersonalHarassmentEnabler(EntityType<? extends InterpersonalHarassmentEnabler> entityType, World world) {
        super(entityType, world);
    }

    public InterpersonalHarassmentEnabler(World world, double x, double y, double z) {
        super(Honque.REALLY_FUNNY, x, y, z, world);
    }

    public InterpersonalHarassmentEnabler (World world, LivingEntity owner) {
        super(Honque.REALLY_FUNNY, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Honque.THE_FUNNY;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity e = entityHitResult.getEntity();
        if (e instanceof LivingEntity living && !e.isInvulnerable() && living.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.AIR)) {
            living.equipStack(EquipmentSlot.HEAD, getStack());
        } else {
            this.dropStack(this.getStack());
        }
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.dropStack(this.getStack());
        this.discard();
    }
}
