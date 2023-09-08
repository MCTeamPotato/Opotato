package com.teampotato.opotato.mixin.opotato.minecraft;

import net.minecraft.gametest.framework.GameTestRegistry;
import net.minecraft.gametest.framework.TestFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Optional;

@Mixin(GameTestRegistry.class)
public abstract class MixinGameTestRegistry {
    @Shadow public static Collection<TestFunction> getAllTestFunctions() {
        throw new RuntimeException();
    }

    /**
     * @author Kasualix
     * @reason avoid stream
     */
    @Overwrite
    public static Optional<TestFunction> findTestFunction(String testName) {
        for (TestFunction testFunction : getAllTestFunctions()) {
            if (testFunction.getTestName().equalsIgnoreCase(testName)) return Optional.of(testFunction);
        }
        return Optional.empty();
    }
}
