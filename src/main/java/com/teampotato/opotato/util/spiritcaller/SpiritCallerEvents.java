package com.teampotato.opotato.util.spiritcaller;

import com.yellowbrossproductions.spiritcallerbackport.entities.IllagerSoulEntity;
import com.yellowbrossproductions.spiritcallerbackport.entities.SpiritcallerEntity;
import com.yellowbrossproductions.spiritcallerbackport.entities.goal.LoseAIGoal;
import com.yellowbrossproductions.spiritcallerbackport.init.ModEntityTypes;
import com.yellowbrossproductions.spiritcallerbackport.util.EffectRegisterer;
import com.yellowbrossproductions.spiritcallerbackport.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class SpiritCallerEvents {
    public static void register(@NotNull IEventBus forgeBus) {
        forgeBus.addListener(SpiritCallerEvents::scareVillagersAway);
        forgeBus.addListener(SpiritCallerEvents::stopMobs);
        forgeBus.addListener(SpiritCallerEvents::misconductionAttack1);
        forgeBus.addListener(SpiritCallerEvents::misconductionAttack2);
        forgeBus.addListener(SpiritCallerEvents::preventGettingHurt);
    }

    private static void scareVillagersAway(@NotNull EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity.level.isClientSide()) return;
        if (entity instanceof Villager) {
            double runSpeed = 0.8;
            ((PathfinderMob)entity).goalSelector.addGoal(0, new AvoidEntityGoal<>((PathfinderMob)entity, SpiritcallerEntity.class, 8.0F, runSpeed, runSpeed));
        }

        if (entity instanceof WanderingTrader) {
            double runSpeed = 0.5F;
            ((PathfinderMob)entity).goalSelector.addGoal(0, new AvoidEntityGoal<>((PathfinderMob)entity, SpiritcallerEntity.class, 8.0F, runSpeed, runSpeed));
        }

    }

    private static void stopMobs(@NotNull EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity.level.isClientSide()) return;
        if (entity instanceof PathfinderMob) {
            ((PathfinderMob)entity).goalSelector.addGoal(0, new LoseAIGoal((PathfinderMob)entity));
        }

    }

    private static void misconductionAttack1(PlayerInteractEvent.@NotNull RightClickBlock event) {
        if (event.getPlayer().getMainHandItem() == ItemStack.EMPTY && event.getHitVec().getDirection() == Direction.UP && event.getPlayer().hasEffect(EffectRegisterer.MISCONDUCTION.get())) {
            BlockPos blockpos = event.getPos();
            if (event.getWorld().isClientSide()) {
                event.getPlayer().swing(InteractionHand.MAIN_HAND);
            } else {
                EntityUtil.createLineImpsAttack(blockpos, event.getPlayer(), event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ(), event.getWorld());
            }
        }
    }

    private static void misconductionAttack2(@NotNull LivingAttackEvent event) {
        if (!event.isCanceled() && event.getSource().getEntity() instanceof LivingEntity && ((LivingEntity)event.getSource().getEntity()).getMainHandItem() == ItemStack.EMPTY && ((LivingEntity)event.getSource().getEntity()).hasEffect(EffectRegisterer.MISCONDUCTION.get()) && !event.getSource().isMagic()) {
            LivingEntity entity = (LivingEntity)event.getSource().getEntity();
            if (entity.level.isClientSide()) return;
            List<IllagerSoulEntity> list = entity.level.getEntitiesOfClass(IllagerSoulEntity.class, entity.getBoundingBox().inflate(100.0F), (predicate) -> predicate.getTarget() == event.getEntityLiving() && predicate.getOwner() == entity);
            if (list.isEmpty() ) {
                for(int i = 0; i < 3; ++i) {
                    Random random = new Random();
                    IllagerSoulEntity soul = (IllagerSoulEntity)((EntityType<?>) ModEntityTypes.IllagerSoul.get()).create(event.getEntityLiving().level);

                    assert soul != null;

                    soul.setPos(event.getEntityLiving().getX() + (double)-4.0F + (double)random.nextInt(8), event.getEntityLiving().getY() + (double)(1 + random.nextInt(4)), event.getEntityLiving().getZ() + (double)-4.0F + (double)random.nextInt(8));
                    soul.setOwner(entity);
                    soul.setAngelOrDevil(random.nextBoolean());
                    soul.setTarget(event.getEntityLiving());
                    soul.setDeltaMovement(0.0F, 0.1, 0.0F);
                    if (entity.getTeam() != null) {
                        event.getEntityLiving().level.getScoreboard().addPlayerToTeam(soul.getStringUUID(), event.getEntityLiving().level.getScoreboard().getPlayerTeam(entity.getTeam().getName()));
                    }

                    event.getEntityLiving().level.addFreshEntity(soul);
                }
            }
        }

    }

    private static void preventGettingHurt(@NotNull LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof IllagerSoulEntity) {
            IllagerSoulEntity soul = (IllagerSoulEntity)event.getSource().getEntity();
            if (soul.getOwner() == event.getEntityLiving()) {
                event.setCanceled(true);
            }
        }
    }
}
