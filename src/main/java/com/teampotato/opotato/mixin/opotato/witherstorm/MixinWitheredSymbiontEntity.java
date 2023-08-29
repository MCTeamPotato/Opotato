package com.teampotato.opotato.mixin.opotato.witherstorm;

import com.teampotato.opotato.events.WitherStormCaches;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(WitheredSymbiontEntity.class)
public abstract class MixinWitheredSymbiontEntity extends Monster {
    protected MixinWitheredSymbiontEntity(EntityType<? extends Monster> arg, Level arg2) {
        super(arg, arg2);
    }

    @Shadow(remap = false) public abstract WitheredSymbiontEntity.BossfightStage getStage();

    @Shadow(remap = false) @Nullable private UUID summoner;
    @Unique
    private static final WitheredSymbiontEntity.BossfightStage[] BOSS_FIGHT_STAGES = WitheredSymbiontEntity.BossfightStage.values();

    @Unique
    private static final int bossFightStages_length = BOSS_FIGHT_STAGES.length;
    @Unique
    private static final WitheredSymbiontEntity.SpellType[] SPELL_TYPES = WitheredSymbiontEntity.SpellType.values();

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$BossfightStage;values()[Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$BossfightStage;", remap = false))
    private WitheredSymbiontEntity.BossfightStage[] redirectBossFightStagesValues() {
        return BOSS_FIGHT_STAGES;
    }

    @Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$SpellType;values()[Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$SpellType;", remap = false))
    private WitheredSymbiontEntity.SpellType[] redirectBossSpellTypeValues() {
        return SPELL_TYPES;
    }

    /**
     * @author Kasualix
     * @reason avoid allocation
     */
    @Overwrite(remap = false)
    private WitheredSymbiontEntity.BossfightStage getNextStage(int advance) {
        int next = this.getStage().ordinal() + advance;
        return next < bossFightStages_length ? BOSS_FIGHT_STAGES[next] : BOSS_FIGHT_STAGES[0];
    }

    /**
     * @author Kasualix
     * @reason impl cache
     */
    @Overwrite(remap = false)
    @Nullable
    public WitherStormEntity getOwner() {
        if (this.level instanceof ServerLevel) {
            for (UUID uuid : WitherStormCaches.witherStorms.keySet()) {
                WitherStormEntity witherStormEntity = (WitherStormEntity) ((ServerLevel) this.level).getEntity(uuid);
                if (witherStormEntity == null) continue;
                if (uuid.equals(this.summoner)) return witherStormEntity;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    @Mixin(WitheredSymbiontEntity.SpellType.class)
    private static class MixinSpellType {
        @Redirect(method = "random", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$SpellType;values()[Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$SpellType;", remap = false), remap = false)
        private static WitheredSymbiontEntity.SpellType[] avoidAllocation() {
            return SPELL_TYPES;
        }
    }
}
