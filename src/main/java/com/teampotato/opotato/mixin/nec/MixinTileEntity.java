package com.teampotato.opotato.mixin.nec;

import net.minecraft.CrashReportCategory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockEntity.class, priority = 10000)
public class MixinTileEntity {

    private boolean noNBT = false;

    @Inject(method = "fillCrashReportCategory", at = @At("TAIL"))
    private void onFillCrashReportCategory(CrashReportCategory section, CallbackInfo ci) {
        if (!noNBT) {
            noNBT = true;
            section.setDetail("Entity NBT", () -> ((Entity) (Object) this).saveWithoutId(new CompoundTag()).toString());
            noNBT = false;
        }
    }
}
