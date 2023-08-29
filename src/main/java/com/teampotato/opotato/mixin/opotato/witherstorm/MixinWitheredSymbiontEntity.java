package com.teampotato.opotato.mixin.opotato.witherstorm;

import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WitheredSymbiontEntity.class)
public abstract class MixinWitheredSymbiontEntity {
    @Shadow(remap = false) public abstract WitheredSymbiontEntity.BossfightStage getStage();

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

    @SuppressWarnings("unused")
    @Mixin(WitheredSymbiontEntity.SpellType.class)
    private static class MixinSpellType {
        @Redirect(method = "random", at = @At(value = "INVOKE", target = "Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$SpellType;values()[Lnonamecrackers2/witherstormmod/common/entity/WitheredSymbiontEntity$SpellType;", remap = false), remap = false)
        private static WitheredSymbiontEntity.SpellType[] avoidAllocation() {
            return SPELL_TYPES;
        }
    }
}
