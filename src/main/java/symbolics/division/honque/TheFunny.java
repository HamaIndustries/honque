package symbolics.division.honque;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TheFunny extends Item implements Equipment, ProjectileItem {
    private static final float INSTANT_DEATH_CHANCE = 1f/2000f;

    private final SoundEvent[] honks;
    public TheFunny() {
        super(new Item.Settings().maxDamage(200).component(Honque.HONKED, false));
        honks = Registries.SOUND_EVENT.getEntrySet().parallelStream()
                        .filter(entry -> {
                            var p = entry.getKey().getValue().getPath();
                            return p.startsWith("block") || p.startsWith("entity") || p.startsWith("item");
                        }).map(Map.Entry::getValue).toArray(SoundEvent[]::new);
    }

    @Nullable
    public ActionResult press(PlayerEntity player, LivingEntity entity, ItemStack itemStack) {
        var reach = player.getEntityInteractionRange();
        HitResult hit =  ProjectileUtil.raycast(
                player,
                player.getCameraPosVec(0),
                player.getCameraPosVec(0).add(player.getRotationVector().multiply(5)),
                player.getBoundingBox().stretch(player.getRotationVec(0).multiply(
                        reach
                )).expand(1.0D, 1.0D, 1.0D),
                e -> e == entity,
                reach
        );
        if (hit == null) return ActionResult.PASS;
        var p = hit.getPos();
        if (Math.abs(p.y - entity.getEyeY()) < 0.4) {
            if (!player.getWorld().isClient) {
                ServerWorld world = (ServerWorld)player.getWorld();
                HonqueTraquer.inc(world);
                var random = world.random;
                float prob = random.nextFloat();
                if (prob <= INSTANT_DEATH_CHANCE / 200) {
                    EntityType.WARDEN.spawn(world, entity.getBlockPos(), SpawnReason.TRIGGERED);
                } else if (prob <= INSTANT_DEATH_CHANCE / 5) {
                    player.kill();
                } else if (prob <= INSTANT_DEATH_CHANCE) {
                    entity.kill();
                } else {
                    SoundEvent honk = honks[random.nextInt(honks.length)];
                    world.playSound(entity, entity.getBlockPos(), honk, SoundCategory.AMBIENT, 1.0f, 1.0f);
                    for (var dir : Direction.values()) {
                        BlockPos bp = entity.getBlockPos().offset(dir);
                        world.setBlockState(bp, world.getBlockState(bp).withIfExists(Properties.POWERED, true), Block.NOTIFY_ALL_AND_REDRAW);
                    }
                }
            }
            player.getWorld().addImportantParticle(ParticleTypes.CRIT, p.x, p.y, p.z, 0, 0, 0);
        }
        return ActionResult.success(true);
    }

    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_SNOWBALL_THROW,
                SoundCategory.NEUTRAL,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            InterpersonalHarassmentEnabler silly = new InterpersonalHarassmentEnabler(world, user);
            silly.setItem(itemStack);
            silly.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(silly);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        itemStack.decrementUnlessCreative(1, user);
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        InterpersonalHarassmentEnabler h = new InterpersonalHarassmentEnabler(world, pos.getX(), pos.getY(), pos.getZ());
        h.setItem(stack);
        return h;
    }
}
