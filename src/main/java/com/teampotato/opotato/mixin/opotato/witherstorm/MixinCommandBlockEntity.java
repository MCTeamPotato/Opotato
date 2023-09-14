package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.EntitiesCacheEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(CommandBlockEntity.class)
public abstract class MixinCommandBlockEntity extends LivingEntity {
    protected MixinCommandBlockEntity(EntityType<? extends LivingEntity> arg, Level arg2) {
        super(arg, arg2);
    }

    @Shadow(remap = false) public abstract CommandBlockEntity.State getState();

    @Shadow(remap = false) public abstract void setState(CommandBlockEntity.State state);

    @Shadow(remap = false) @Nullable public abstract UUID getOwnerUUID();

    @Shadow(remap = false) public abstract void setOwner(@org.jetbrains.annotations.Nullable WitherStormEntity entity);

    @Shadow(remap = false) @Nullable public abstract WitherStormEntity getOwner();

    @Unique
    private static final CommandBlockEntity.Mode[] ALL_MODES = CommandBlockEntity.Mode.values();
    @Unique
    private static final CommandBlockEntity.State[] ALL_STATES = CommandBlockEntity.State.values();

    @Unique
    private static final int states_length = ALL_STATES.length;

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$Mode;values()[Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$Mode;", remap = false))
    private CommandBlockEntity.Mode[] avoidModeValues() {
        return ALL_MODES;
    }

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$State;values()[Lnonamecrackers2/witherstormmod/common/entity/CommandBlockEntity$State;", remap = false))
    private CommandBlockEntity.State[] avoidStateValues() {
        return ALL_STATES;
    }

    /**
     * @author Kasualix
     * @reason avoid allocation
     */
    @Overwrite(remap = false)
    public void nextState() {
        if (getState().ordinal() + 1 < states_length) setState(ALL_STATES[getState().ordinal() + 1]);
    }

    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite(remap = false)
    public void findOwner() {
        if (this.getOwnerUUID() != null && this.getOwner() == null) {
            MinecraftServer server = this.level.getServer();
            if (this.getState() != CommandBlockEntity.State.BOSSFIGHT && this.level instanceof ServerLevel) {

                for (UUID witherStormUUID : EntitiesCacheEvent.witherStorms.keySet()) {
                    WitherStormEntity witherStormEntity = (WitherStormEntity) ((ServerLevel) this.level).getEntity(witherStormUUID);
                    if (witherStormEntity == null) continue;
                    if (witherStormEntity.getUUID().equals(this.getOwnerUUID())) {
                        this.setOwner(witherStormEntity);
                        witherStormEntity.getPlayDeadManager().setCommandBlock((CommandBlockEntity) (Object)this);
                    }
                }
            } else if (server != null) {

                for (ResourceKey<Level> resourceKey : EntitiesCacheEvent.witherStorms.values()) {
                    ServerLevel serverLevel = server.getLevel(resourceKey);
                    if (serverLevel == null) continue;
                    Entity entity = serverLevel.getEntity(this.getOwnerUUID());
                    if (entity instanceof WitherStormEntity) {
                        this.setOwner((WitherStormEntity) entity);
                        break;
                    }
                }
            }
        }
    }
}
