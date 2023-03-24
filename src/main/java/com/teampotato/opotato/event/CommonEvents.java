package com.teampotato.opotato.event;

import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.HeadshotUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class CommonEvents {
    @SubscribeEvent
    public static void onHeadshot(LivingHurtEvent event) {
        if (!event.getSource().isProjectile() || !PotatoCommonConfig.ENABLE_HEADSHOT.get()) return;

        LivingEntity attacked = event.getEntityLiving();

        if (!HeadshotUtils.calculateIsHeadHit(event.getSource().getSourcePosition(), attacked) || attacked.isInvulnerableTo(event.getSource())) return;

        if (attacked instanceof PlayerEntity) {
            ((PlayerEntity) attacked).displayClientMessage(new TranslationTextComponent("headshot.opotato.on_player"), true);
        } else {
            Entity archer = event.getSource().getDirectEntity();
            if (archer instanceof PlayerEntity) ((PlayerEntity) archer).displayClientMessage(new TranslationTextComponent("headshot.opotato.on_entity"), true);
        }

        if (PotatoCommonConfig.PLAY_HEADSHOT_SOUND.get()) attacked.playSound(SoundEvents.ARROW_HIT_PLAYER, 1, 1);
        event.setAmount((float) (event.getAmount() * PotatoCommonConfig.DAMAGE_MULTIPLIER.get()));

        if (PotatoCommonConfig.DO_BLIND.get())
            attacked.addEffect(new EffectInstance(Effects.BLINDNESS, PotatoCommonConfig.BLIND_TICKS.get(), 3));
        if (PotatoCommonConfig.DO_NAUSEA.get())
            attacked.addEffect(new EffectInstance(Effects.CONFUSION, PotatoCommonConfig.NAUSEA_TICKS.get(), 2));
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Opotato.OpotatoCommand.register(event.getDispatcher());
    }

    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void duplicateEntityUUIDFix(EntityJoinWorldEvent event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            Entity entity = event.getEntity();
            if (entity instanceof PlayerEntity) return;
            Entity existing = world.getEntity(entity.getUUID());
            if (existing != null && existing != entity) {
                UUID newUUID = MathHelper.createInsecureUUID(RANDOM);
                while (world.getEntity(newUUID) != null) newUUID = MathHelper.createInsecureUUID(RANDOM);
                entity.setUUID(newUUID);
            }
        }
    }

    @SubscribeEvent
    public static void fixExperienceBug(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).setExperienceLevels(player.experienceLevel);
            ResourceLocation dim = event.getTo().getRegistryName();
            player.addTag("opotato_" + dim.getNamespace() + "_" + dim.getPath());
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (player.level.isClientSide) return;

        ResourceLocation dim = player.level.dimension().getRegistryName();
        if (!player.removeTag("opotato_" + dim.getNamespace() + "_" + dim.getPath())) return;

        List<EffectInstance> effects = new ArrayList<>(player.getActiveEffects());
        if (effects.isEmpty()) return;

        effects.sort(Comparator.comparingInt(EffectInstance::getDuration).reversed());

        player.removeAllEffects();
        for (EffectInstance effect : effects) {
            player.addEffect(effect);
        }
    }
}
