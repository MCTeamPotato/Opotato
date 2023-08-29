package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.WitherStormCaches;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(CommandBlockEntity.class)
public abstract class MixinCommandBlockEntity  extends LivingEntity {
    protected MixinCommandBlockEntity(EntityType<? extends LivingEntity> arg, Level arg2) {
        super(arg, arg2);
    }

    @Shadow(remap = false) public abstract CommandBlockEntity.State getState();

    @Shadow(remap = false) public abstract void setState(CommandBlockEntity.State state);

    @Shadow @Nullable public abstract UUID getOwnerUUID();

    @Shadow public abstract void setOwner(@org.jetbrains.annotations.Nullable WitherStormEntity entity);

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

    @Inject(method = "findOwner", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getServer()Lnet/minecraft/server/MinecraftServer;", shift = At.Shift.BEFORE), cancellable = true)
    private void onFindOwner(CallbackInfo ci) {
        ci.cancel();
        MinecraftServer server = this.level.getServer();
        if (server == null) return;
        for (ResourceKey<Level> resourceKey : WitherStormCaches.witherStorms.values()) {
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
