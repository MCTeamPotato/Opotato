package com.teampotato.opotato;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teampotato.opotato.config.PotatoCommonConfig;
import com.teampotato.opotato.util.schwarz.ChunkCommandHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mod(Opotato.ID)
public class Opotato {

    public static final String ID = "opotato";
    public static final Logger LOGGER = LogManager.getLogger(ID);
    public static CopyOnWriteArrayList<Chunk> loadedChunks = new CopyOnWriteArrayList<>();

    public Opotato() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PotatoCommonConfig.COMMON_CONFIG);
        if (PotatoCommonConfig.PRINT_MOD_LIST_WHEN_LAUNCHING_GAME.get()) ModList.get().getMods().forEach(modInfo -> LOGGER.info("Mod " + modInfo.getOwningFile().getFile().getFileName() + " loaded!"));
        LOGGER.info("Oh, potato!");
    }

    public static class OpotatoCommand {
        public static void register(CommandDispatcher<CommandSource> dispatcher) {
            LiteralArgumentBuilder<CommandSource> schwarz = Commands.literal("schwarz").then(Commands.literal("chunkanalyse").executes(ChunkCommandHandler::ChunkAnalyse));
            dispatcher.register(schwarz);
        }
    }

    public static class EmptyThread extends Thread {
        public EmptyThread() {
            this.setName("Empty Thread");
            this.setDaemon(true);
            this.start();
        }
        public void run() {}
    }

    public static boolean isLoaded(String modID) {
        return ModList.get().isLoaded(modID);
    }

    public static void exeCmd(MinecraftServer server, String cmd) {
        server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), cmd);
    }

    public static void addIncompatibleWarn(FMLCommonSetupEvent event, String translationKey) {
        event.enqueueWork(() -> ModLoader.get().addWarning(new ModLoadingWarning(ModLoadingContext.get().getActiveContainer().getModInfo(), ModLoadingStage.COMMON_SETUP, translationKey)));
    }

    public static <T extends Entity> void getEntitiesOfClass(Class<? extends T> cs, AxisAlignedBB aabb, List<T> list, @Nullable Predicate<? super T> predicate, World level, ClassInheritanceMultiMap<Entity>[] entitySections) {
        for(int k = MathHelper.clamp(MathHelper.floor((aabb.minY - level.getMaxEntityRadius()) / 16.0D), 0, entitySections.length - 1);
            k <= MathHelper.clamp(MathHelper.floor((aabb.maxY + level.getMaxEntityRadius()) / 16.0D), 0, entitySections.length - 1); ++k) {
            list.addAll(entitySections[k].find(cs).stream().filter(t -> t.getBoundingBox().intersects(aabb) && (predicate == null || predicate.test(t))).collect(Collectors.toList()));
        }
    }

    public static <T extends Entity> @NotNull List<T> getEntitiesOfClass(@NotNull Class<? extends T> cs, AxisAlignedBB aabb, @Nullable Predicate<? super T> predicate, IProfiler profiler, double maxEntityRadius, AbstractChunkProvider chunkSource) {
        profiler.incrementCounter("getEntities");
        List<T> list = Lists.newArrayList();
        for(int i1 = MathHelper.floor((aabb.minX - maxEntityRadius) / 16.0D); i1 < MathHelper.ceil((aabb.maxX + maxEntityRadius) / 16.0D); ++i1) {
            for(int j1 = MathHelper.floor((aabb.minZ - maxEntityRadius) / 16.0D); j1 < MathHelper.ceil((aabb.maxZ + maxEntityRadius) / 16.0D); ++j1) {
                Chunk chunk = chunkSource.getChunk(i1, j1, false);
                if (chunk != null) chunk.getEntitiesOfClass(cs, aabb, list, predicate);
            }
        }
        return list;
    }

    public static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkCodecRange(final N minInclusive, final N maxInclusive) {
        return value -> {
            if (value.compareTo(minInclusive) >= 0 && value.compareTo(maxInclusive) <= 0) return DataResult.success(value);
            return DataResult.error("Value " + value + " outside of range [" + minInclusive + ":" + maxInclusive + "]", value);
        };
    }
/*
    public static final Codec<DimensionType> DIRECT_CODEC = RecordCodecBuilder
            .create(
                    (p_236026_0_) -> p_236026_0_
                            .group(Codec.LONG.optionalFieldOf("fixed_time")
                                    .xmap(
                                            (p_236028_0_) -> p_236028_0_.map(OptionalLong::of).orElseGet(OptionalLong::empty),
                                            (p_236029_0_) -> p_236029_0_.isPresent() ? Optional.of(p_236029_0_.getAsLong()) : Optional.empty())
                                    .forGetter((p_236044_0_) -> p_236044_0_.fixedTime), Codec.BOOL.fieldOf("has_skylight")
                                    .forGetter(DimensionType::hasSkyLight), Codec.BOOL.fieldOf("has_ceiling")
                                    .forGetter(DimensionType::hasCeiling), Codec.BOOL.fieldOf("ultrawarm")
                                    .forGetter(DimensionType::ultraWarm), Codec.BOOL.fieldOf("natural")
                                    .forGetter(DimensionType::natural), Codec.doubleRange((double)1.0E-5F, 3.0E7D).fieldOf("coordinate_scale")
                                    .forGetter(DimensionType::coordinateScale), Codec.BOOL.fieldOf("piglin_safe")
                                    .forGetter(DimensionType::piglinSafe), Codec.BOOL.fieldOf("bed_works")
                                    .forGetter(DimensionType::bedWorks), Codec.BOOL.fieldOf("respawn_anchor_works")
                                    .forGetter(DimensionType::respawnAnchorWorks), Codec.BOOL.fieldOf("has_raids")
                                    .forGetter(DimensionType::hasRaids), Codec.intRange(0, 256).fieldOf("logical_height")
                                    .forGetter(DimensionType::logicalHeight), ResourceLocation.CODEC.fieldOf("infiniburn")
                                    .forGetter((p_241508_0_) -> p_241508_0_.infiniburn), ResourceLocation.CODEC.fieldOf("effects").orElse(OVERWORLD_EFFECTS)
                                    .forGetter((p_242721_0_) -> p_242721_0_.effectsLocation), Codec.FLOAT.fieldOf("ambient_light")
                                    .forGetter((p_236042_0_) -> p_236042_0_.ambientLight)).apply(p_236026_0_, DimensionType::new));
*/}
