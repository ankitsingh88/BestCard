package com.ankitsingh.bestcard.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Category;
import com.ankitsingh.bestcard.model.codecs.SavedCardsMapCodec;
import com.ankitsingh.bestcard.model.codecs.SavedCategoriesMapCodec;
import com.ankitsingh.bestcard.model.codecs.SavedCategoryRankMapCodec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class DataLoader {

    private static final String SAVED_CARDS_MAP_PREFERENCE_KEY = "SavedCardsMapPreferences";
    private static final String SAVED_CARDS_MAP_KEY = "SavedCardsMap";

    private static final String SAVED_CATEGORIES_MAP_PREFERENCE_KEY = "SavedCategoriesMapPreferences";
    private static final String SAVED_CATEGORIES_MAP_KEY = "SavedCategoriesMap";

    private static final String SAVED_CATEGORY_RANK_MAP_PREFERENCE_KEY = "SavedCategoryRankMapPreferences";
    private static final String SAVED_CATEGORY_RANK_MAP_KEY = "SavedCategoryRankMap";

    private static final String SAVED_BIOMETRIC_AUTHENTICATION_PREFERENCE_KEY = "SavedBiometricAuthenticationPreferences";
    private static final String SAVED_BIOMETRIC_AUTHENTICATION_KEY = "SavedBiometricAuthentication";

    private static final String SAVED_NIGHT_MODE_PREFERENCE_KEY = "SavedNightModePreferences";
    private static final String SAVED_NIGHT_MODE_KEY = "SavedNightMode";

    private final SharedPreferences savedCardsMapSharedPreferences;
    private final SharedPreferences savedCategoriesMapSharedPreferences;
    private final SharedPreferences categoryRankMapSharedPreferences;
    private final SharedPreferences biometricAuthenticationSharedPreferences;
    private final SharedPreferences nightModeSharedPreferences;

    private final SavedCardsMapCodec savedCardsMapCodec = new SavedCardsMapCodec();
    private final SavedCategoriesMapCodec savedCategoriesMapCodec = new SavedCategoriesMapCodec();
    private final SavedCategoryRankMapCodec savedCategoryRankMapCodec = new SavedCategoryRankMapCodec();

    @Getter
    private Map<String, Card> savedCardsMap = new HashMap<>();

    @Getter
    private Map<String, Category> savedCategoriesMap = new HashMap<>();

    @Getter
    private Map<String, Long> savedCategoryRankMap = new HashMap<>();

    @Getter
    private boolean biometricAuthentication = false;

    @Getter
    private int nightMode;

    public DataLoader(
            final AppCompatActivity appCompatActivity) {
        this.savedCardsMapSharedPreferences = appCompatActivity.getSharedPreferences(SAVED_CARDS_MAP_PREFERENCE_KEY, Context.MODE_PRIVATE);
        this.savedCategoriesMapSharedPreferences = appCompatActivity.getSharedPreferences(SAVED_CATEGORIES_MAP_PREFERENCE_KEY, Context.MODE_PRIVATE);
        this.categoryRankMapSharedPreferences = appCompatActivity.getSharedPreferences(SAVED_CATEGORY_RANK_MAP_PREFERENCE_KEY, Context.MODE_PRIVATE);
        this.biometricAuthenticationSharedPreferences = appCompatActivity.getSharedPreferences(SAVED_BIOMETRIC_AUTHENTICATION_PREFERENCE_KEY, Context.MODE_PRIVATE);
        this.nightModeSharedPreferences = appCompatActivity.getSharedPreferences(SAVED_NIGHT_MODE_PREFERENCE_KEY, Context.MODE_PRIVATE);

        loadSavedCardMap();
        loadSavedCategoryMap();
        loadCategoryRankMap();
        loadBiometricAuthentication();
        loadNightMode();
    }

    private void loadSavedCardMap() {
        final String serializedSavedCardsMap = this.savedCardsMapSharedPreferences.getString(SAVED_CARDS_MAP_KEY, null);

        this.savedCardsMap = this.savedCardsMapCodec.deserialize(serializedSavedCardsMap);
    }

    private void loadSavedCategoryMap() {
        final String serializedSavedCategoriesMap = this.savedCategoriesMapSharedPreferences.getString(SAVED_CATEGORIES_MAP_KEY, null);

        this.savedCategoriesMap = this.savedCategoriesMapCodec.deserialize(serializedSavedCategoriesMap);
    }

    private void loadCategoryRankMap() {
        final String serializedSavedCategoryRankMap = this.categoryRankMapSharedPreferences.getString(SAVED_CATEGORY_RANK_MAP_KEY, null);

        this.savedCategoryRankMap = this.savedCategoryRankMapCodec.deserialize(serializedSavedCategoryRankMap);
    }

    private void loadBiometricAuthentication() {
        this.biometricAuthentication = this.biometricAuthenticationSharedPreferences.getBoolean(SAVED_BIOMETRIC_AUTHENTICATION_KEY, false);
    }

    private void loadNightMode() {
        this.nightMode = this.nightModeSharedPreferences.getInt(SAVED_NIGHT_MODE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public void saveCard(
            final Card card) {
        this.savedCardsMap.put(
                card.getId(),
                card.clone());

        saveCards();
    }

    public void saveCards(
            final List<Card> cards) {
        cards.forEach(card ->
                this.savedCardsMap.put(
                        card.getId(),
                        card.clone()));

        saveCards();
    }

    public void deleteCard(
            final Card card) {
        this.savedCardsMap.remove(card.getId());

        saveCards();
    }

    private void saveCards() {
        final String serializedSavedCardsMapToSave = this.savedCardsMapCodec.serialize(this.savedCardsMap);

        this.savedCardsMapSharedPreferences.edit().putString(SAVED_CARDS_MAP_KEY, serializedSavedCardsMapToSave).apply();
    }

    public void saveCategory(
            final Category category) {
        this.savedCategoriesMap.put(
                category.getId(),
                category.clone());

        saveCategories();
    }

    public void saveCategories(
            final List<Category> categories) {
        categories.forEach(category -> {
            if (!this.savedCategoriesMap.containsKey(category.getId())) {
                this.savedCategoriesMap.put(
                        category.getId(),
                        category.clone());
            }
        });

        saveCategories();
    }

    private void saveCategories() {
        final String serializedSavedCategoriesMapToSave = this.savedCategoriesMapCodec.serialize(this.savedCategoriesMap);

        this.savedCategoriesMapSharedPreferences.edit().putString(SAVED_CATEGORIES_MAP_KEY, serializedSavedCategoriesMapToSave).apply();
    }

    public void saveCategoryRankMap(
            final Map<String, Long> savedCategoryRankMap) {
        this.savedCategoryRankMap = savedCategoryRankMap;

        saveCategoryRankMap();
    }

    private void saveCategoryRankMap() {
        final String serializedSavedCategoryRankMapToSave = this.savedCategoryRankMapCodec.serialize(this.savedCategoryRankMap);

        this.categoryRankMapSharedPreferences.edit().putString(SAVED_CATEGORY_RANK_MAP_KEY, serializedSavedCategoryRankMapToSave).apply();
    }

    public void saveBiometricAuthentication(
            final boolean enabled) {
        this.biometricAuthentication = enabled;

        this.biometricAuthenticationSharedPreferences.edit().putBoolean(SAVED_BIOMETRIC_AUTHENTICATION_KEY, enabled).apply();
    }

    public void saveNightMode(
            final int nightMode) {
        this.nightMode = nightMode;

        this.nightModeSharedPreferences.edit().putInt(SAVED_NIGHT_MODE_KEY, nightMode).apply();
    }
}
