package com.teampotato.potatoptimize.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ChunkStatus implements Comparable<ChunkStatus>{
    private int score = 0;
    private int coords[] = new int[2];

    private LevelChunk chunk;
    private String world;

    private HashMap<BlockEntityType, Integer> blockEntityScore = new HashMap<BlockEntityType, Integer>() {{
        put(BlockEntityType.CHEST, 1);
        put(BlockEntityType.TRAPPED_CHEST, 2);
        put(BlockEntityType.BLAST_FURNACE, 3);
        put(BlockEntityType.FURNACE, 3);
        put(BlockEntityType.SIGN, 1);
        put(BlockEntityType.BEACON, 35);
        put(BlockEntityType.HOPPER, 6);
        put(BlockEntityType.PISTON, 5);
        put(BlockEntityType.DISPENSER, 10);
        put(BlockEntityType.MOB_SPAWNER, 60);
    }};

    private HashMap<EntityType, Integer> livingEntityScore = new HashMap<EntityType, Integer>(){{
        put(EntityType.PLAYER, 15);
        put(EntityType.ZOMBIE, 2);
        put(EntityType.SPIDER, 2);
        put(EntityType.SKELETON, 2);
        put(EntityType.CREEPER, 3);
        put(EntityType.COW, 1);
        put(EntityType.PIG, 1);
        put(EntityType.SHEEP, 1);
        put(EntityType.EYE_OF_ENDER, 15);
        put(EntityType.EGG, 3);
        put(EntityType.FISHING_BOBBER, 2);
        put(EntityType.SNOWBALL, 2);
        put(EntityType.ARROW, 1);
        put(EntityType.PAINTING, 1);
        put(EntityType.ITEM_FRAME, 3);
        put(EntityType.ARMOR_STAND, 4);
        put(EntityType.ITEM, 2);
        put(EntityType.EXPERIENCE_ORB, 3);
        put(EntityType.FIREWORK_ROCKET, 8);
        put(EntityType.ENDER_DRAGON, 85);
        put(EntityType.END_CRYSTAL, 10);
        put(EntityType.WITHER, 55);
        put(EntityType.WITHER_SKULL, 20);
        put(EntityType.TNT, 7);
        put(EntityType.TNT_MINECART, 15);
        put(EntityType.HOPPER_MINECART, 20);
        put(EntityType.SPAWNER_MINECART, 80);
        put(EntityType.ENDERMITE, 5);
    }};

    public ChunkStatus(LevelChunk chunk) {
        this.chunk = chunk;
        if (chunk.getLevel().dimension().equals(Level.OVERWORLD)) {
            this.world = "overworld";
        } else if (chunk.getLevel().dimension().equals(Level.NETHER)) {
            this.world = "nether";
        } else if (chunk.getLevel().dimension().equals(Level.END)) {
            this.world = "end";
        }
        coords[0] = (chunk.getPos().getMinBlockX() + chunk.getPos().getMaxBlockX()) / 2;
        coords[1] = (chunk.getPos().getMinBlockZ() + chunk.getPos().getMaxBlockZ()) / 2;
    }

    public void genScore() {
        Map<BlockPos, BlockEntity> blockentities = chunk.getBlockEntities();
        for (BlockEntity blockEntity : blockentities.values()) {
            if (blockEntityScore.get(blockEntity.getType()) != null) {
                score += blockEntityScore.get(blockEntity.getType());
            }
        }

        int startx = chunk.getPos().getMinBlockX();
        int startz = chunk.getPos().getMinBlockZ();
        int endx = chunk.getPos().getMaxBlockX();
        int endz = chunk.getPos().getMaxBlockZ();
        AABB box = new AABB(startx, 0, startz, endx, 256, endz);
        List<Entity> livingEntityList = new ArrayList<>();
        chunk.getEntities((Entity) null, box, livingEntityList, entity -> true);
        for (Entity entity : livingEntityList) {
            if (livingEntityScore.get(entity.getType()) != null) {
                score += livingEntityScore.get(entity.getType());
            }
        }
    }

    public TextComponent genText() {
        return new TextComponent("Chunk Center Pos: " + coords[0] + " " + coords[1] + "\nScore: " + score + "\nWorld: " + world);
    }

    private static Integer getChunkBlockEntitySize(LevelChunk chunk) {
        return chunk.getBlockEntities().size();
    }

    private static Integer getChunkLivingEntitySize(LevelChunk chunk) {
        int startx = chunk.getPos().getMinBlockX();
        int startz = chunk.getPos().getMinBlockZ();
        int endx = chunk.getPos().getMaxBlockX();
        int endz = chunk.getPos().getMaxBlockZ();
        AABB box = new AABB(startx, 0, startz, endx, 256, endz);
        List<Entity> entityList = new ArrayList<>();
        chunk.getEntities((Entity) null, box, entityList, entity -> true);
        return entityList.size();
    }

    @Override
    public int compareTo(ChunkStatus from) {
        int CompareQuantity = from.score;
        return CompareQuantity - score;
    }
}
