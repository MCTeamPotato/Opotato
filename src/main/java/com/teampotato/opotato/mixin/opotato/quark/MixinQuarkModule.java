package com.teampotato.opotato.mixin.opotato.quark;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.quark.api.event.ModuleLoadedEvent;
import vazkii.quark.base.module.QuarkModule;

import java.util.List;

@Mixin(value = QuarkModule.class, remap = false)
public abstract class MixinQuarkModule {
    @Shadow public boolean configEnabled;
    @Shadow private boolean firstLoad;
    @Shadow public String lowercaseName;
    @Shadow public boolean missingDep;
    @Shadow public boolean ignoreAntiOverlap;
    @Shadow public List<String> antiOverlap;
    @Shadow protected abstract void setEnabledAndManageSubscriptions(boolean enabled);

    /**
     * @author Kasualix
     * @reason Remove 'Loading Module ......' log spam.
     */
    @Overwrite
    public final void setEnabled(boolean enabled) {
        this.configEnabled = enabled;
        if (this.firstLoad) MinecraftForge.EVENT_BUS.post(new ModuleLoadedEvent(this.lowercaseName));
        this.firstLoad = false;
        if (this.missingDep) {
            enabled = false;
        } else if (!this.ignoreAntiOverlap && this.antiOverlap != null) {
            for (String s : this.antiOverlap) {
                if (ModList.get().isLoaded(s)) {
                    enabled = false;
                    break;
                }
            }
        }
        this.setEnabledAndManageSubscriptions(enabled);
    }
}
