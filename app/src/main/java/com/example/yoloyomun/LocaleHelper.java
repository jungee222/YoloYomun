package com.example.yoloyomun;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

/**
 * Small helper around AppCompat's per-app locales so the globe button in every
 * fragment can flip the whole UI between Korean and English.
 */
public final class LocaleHelper {

    private LocaleHelper() {
    }

    /** The active 2-letter language code, e.g. "ko" or "en". */
    public static String currentLanguage() {
        LocaleListCompat locales = AppCompatDelegate.getApplicationLocales();
        if (!locales.isEmpty() && locales.get(0) != null) {
            return locales.get(0).getLanguage();
        }
        return Locale.getDefault().getLanguage();
    }

    /**
     * Toggles between Korean and English. Setting the application locales causes
     * AppCompat to recreate the activity so every string resource is re-resolved.
     */
    public static void toggleLanguage() {
        String next = "ko".equals(currentLanguage()) ? "en" : "ko";
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(next));
    }
}
