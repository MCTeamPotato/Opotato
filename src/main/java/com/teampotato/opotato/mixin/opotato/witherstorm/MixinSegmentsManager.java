package com.teampotato.opotato.mixin.opotato.witherstorm;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.util.SegmentsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(value = SegmentsManager.class, remap = false)
public abstract class MixinSegmentsManager {
    @Shadow public abstract void setSegment(@Nullable WitherStormSegmentEntity entity, int index);

    @Shadow @Final protected WitherStormSegmentEntity[] segments;

    @Shadow @Final protected WitherStormEntity owner;

    /**
     * @author Kasualix
     * @reason impl cache and optimization
     */
    @Overwrite
    public void findSegments(ServerLevel world) {
        for (Entity entity : world.getAllEntities()) {
            if (entity instanceof WitherStormSegmentEntity) {
                WitherStormSegmentEntity segment = (WitherStormSegmentEntity)entity;
                UUID parent = segment.getParentUUID();
                if (this.owner.getUUID().equals(parent)) {
                    int index = segment.isMirrored() ? 0 : 1;
                    WitherStormSegmentEntity existing = this.segments[index];
                    if (existing != null) {
                        boolean flag = true;
                        for (WitherStormSegmentEntity s : this.segments) {
                            if (s != null && segment.getUUID().equals(s.getUUID())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            if (segment.getTimeWithParent() > existing.getTimeWithParent()) {
                                existing.remove();
                                setSegment(segment, index);
                                continue;
                            }
                            if (existing.getTimeWithParent() > segment.getTimeWithParent()) {
                                segment.remove();
                                setSegment(existing, index);
                            }
                        }
                        continue;
                    }
                    setSegment(segment, index);
                }
            }
        }
    }
}
