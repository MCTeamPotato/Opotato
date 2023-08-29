package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.WitherStormCaches;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(WitherStormSegmentEntity.class)
public abstract class MixinWitherStormSegmentEntity extends WitherStormEntity {
    @Shadow(remap = false) @Nullable public abstract WitherStormEntity getParent();

    @Shadow(remap = false) @Nullable public abstract UUID getParentUUID();

    @Shadow(remap = false) public abstract void setParent(WitherStormEntity parent);

    @Shadow(remap = false) private int timeWithParent;

    public MixinWitherStormSegmentEntity(EntityType<? extends WitherStormEntity> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    /**
     * @author Kasualix
     * @reason impl cache and optimize
     */
    @Overwrite
    public void tick() {
        super.tick();
        if (this.getParent() == null && this.getParentUUID() != null) {
            for (UUID witherStormUUID : WitherStormCaches.witherStorms.keySet()) {
                if (this.getParentUUID().equals(witherStormUUID) && this.level instanceof ServerLevel) {
                    this.setParent((WitherStormEntity) ((ServerLevel) this.level).getEntity(witherStormUUID));
                }
            }

            if (this.isOnDistantRenderer()) {
                this.level.getCapability(WitherStormModClientCapabilities.DISTANT_RENDERER).ifPresent((renderer) -> {

                    for (WitherStormEntity storm : renderer.getKnown()) {
                        if (this.getParentUUID().equals(storm.getUUID())) {
                            this.setParent(storm);
                            break;
                        }
                    }

                });
            }
        }

        if (this.getParent() != null) {
            ++this.timeWithParent;
        }
    }
}
