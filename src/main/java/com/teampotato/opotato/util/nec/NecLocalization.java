package com.teampotato.opotato.util.nec;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teampotato.opotato.Opotato;
import com.teampotato.opotato.platform.NecPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class NecLocalization {
    private static final String DEFAULT_LANGUAGE_CODE = "en_us";

    private static final boolean useCustomLocalization = !NecPlatform.instance().isForge()
            && !NecPlatform.instance().isModLoaded("fabric-resource-loader-v0");

    public static String localize(String translationKey) {
        TranslatableComponent text = new TranslatableComponent(translationKey);
        if (useCustomLocalization) return localizeCustom(translationKey);
        else return text.getContents();
    }

    @NotNull
    private static String localizeCustom(String translationKey) {
        String currentLanguageCode = getCurrentLanguageCode();
        String translationForChosenLanguage = localizeCustom(translationKey, currentLanguageCode);
        if (translationForChosenLanguage != null) return translationForChosenLanguage;
        else {
            String englishTranslation = localizeCustom(translationKey, DEFAULT_LANGUAGE_CODE);
            return englishTranslation == null ? translationKey : englishTranslation;
        }
    }

    @Nullable
    private static String localizeCustom(String translationKey, String languageCode) {
        LanguageTranslations translations = storedLanguages.computeIfAbsent(
                languageCode, (ignored) -> loadLanguage(languageCode)
        );
        return translations.get(translationKey);
    }

    public static Component translatedText(String translationKey) {
        if (useCustomLocalization) return new TextComponent(localize(translationKey));
        else return new TranslatableComponent(translationKey);
    }

    private static class LanguageTranslations {
        private final Map<String, String> translations;

        private LanguageTranslations(Map<String, String> translations) {
            this.translations = translations;
        }

        @Nullable String get(String translationKey) {
            return translations.get(translationKey);
        }
    }

    private static final Map<String, LanguageTranslations> storedLanguages = new HashMap<>();

    private static String getCurrentLanguageCode() {
        return Minecraft.getInstance().getLanguageManager().getSelected().getCode();
    }

    private static LanguageTranslations loadLanguage(String code) {
        Map<String, String> translations;
        try (InputStream localizations = getLocalizations(code)) {
            if (localizations == null) {
                translations = new HashMap<>();
                Opotato.LOGGER.debug("No localization for language code: " + code);
            } else {
                String content = IOUtils.toString(localizations, StandardCharsets.UTF_8); /*Java11.readString();*/
                translations = parseTranslations(content);
            }
        } catch (IOException e) {
            Opotato.LOGGER.error("Could not load translations: ", e);
            translations = new HashMap<>();
        }
        return new LanguageTranslations(translations);
    }

    @Nullable
    private static InputStream getLocalizations(String code) {
        Path relativePath = Paths.get("assets", Opotato.ID, "lang", code + ".json");
        return NecPlatform.instance().getResource(relativePath);
    }

    private static final Gson gson = new Gson();

    private static Map<String, String> parseTranslations(String raw) {
        JsonObject jsonObject = gson.fromJson(raw, JsonObject.class);
        Map<String, String> translations = new HashMap<>();
        for (Map.Entry<String, JsonElement> child : jsonObject.entrySet()) {
            translations.put(child.getKey(), child.getValue().getAsString());
        }
        return translations;
    }

}
