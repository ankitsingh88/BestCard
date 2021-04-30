package com.ankitsingh.bestcard.util;

import java.util.Locale;
import java.util.Set;

public class CategoryUtil {

    public static boolean isEqualTag(
            final String originalQuery,
            final Set<String> tags) {
        final String query = originalQuery == null ? null : originalQuery.toLowerCase(Locale.getDefault());

        for (final String tag : tags) {
            if (tag.toLowerCase().equals(query)) {
                return true;
            }
        }

        return false;
    }
}
