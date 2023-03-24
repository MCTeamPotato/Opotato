package com.teampotato.opotato.mixin.suggestionproviderfix;

import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Provides {@link Mixin} for the {@link ISuggestionProvider} class.
 *
 * @author Harley O'Connor
 */
@Mixin(ISuggestionProvider.class)
public interface ISuggestionProviderMixin {
    /**
     * Overwrites {@link ISuggestionProvider#filterResources(Iterable, String, Function, Consumer)}.
     * This is an {@link Overwrite} since mixin doesn't seem to allow for other kinds of annotated
     * methods to be {@code public} and {@code static} in interfaces, and the {@code private} access
     * modifier isn't allowed in {@code interfaces}.
     *
     * <p>Difference in this method to the original is that it negates a check for the namespace
     * being <tt>minecraft</tt> in the second {@code if} statement, meaning that if a namespace is
     * not specified it filters to any paths matching the {@code input} {@link String}.</p>
     *
     * @author Harley O'Connor
     * @see ISuggestionProvider#filterResources(Iterable, String, Function, Consumer)
     * @reason Saving The World
     */
    @Overwrite
    static <T> void filterResources(Iterable<T> resources, String input, Function<T, ResourceLocation> resourceMapper, Consumer<T> resourceConsumer) {
        final boolean hasColon = input.indexOf(':') > -1;
        final Predicate<String> matcher = str -> ISuggestionProvider.matchesSubStr(input, str);
        for (T resource : resources) {
            ResourceLocation resourceLocation = resourceMapper.apply(resource);
            if (hasColon ? matcher.test(resourceLocation.toString()) : matcher.test(resourceLocation.getNamespace()) || matcher.test(resourceLocation.getPath())) {
                resourceConsumer.accept(resource);
            }
        }
    }
}
