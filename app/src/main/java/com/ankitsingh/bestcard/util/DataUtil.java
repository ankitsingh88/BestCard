package com.ankitsingh.bestcard.util;

import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {

    private static Map<String, Card> CARD_MAP = loadCards();
    private static Map<String, Category> CATEGORY_MAP = loadCategories();

    public static Map<String, Card> loadCards() {
        final Map<String, Card> cardMap = new HashMap<>();

        cardMap.put(Cards.CHASE_FREEDOM.getId(), Cards.CHASE_FREEDOM);
        cardMap.put(Cards.CHASE_FREEDOM_FLEX.getId(), Cards.CHASE_FREEDOM_FLEX);
        cardMap.put(Cards.CHASE_FREEDOM_UNLIMITED.getId(), Cards.CHASE_FREEDOM_UNLIMITED);
        cardMap.put(Cards.CAPITAL_ONE_QUICKSILVER.getId(), Cards.CAPITAL_ONE_QUICKSILVER);
        cardMap.put(Cards.CHASE_SAPPHIRE_RESERVE.getId(), Cards.CHASE_SAPPHIRE_RESERVE);
        cardMap.put(Cards.CHASE_SAPPHIRE_PREFERRED.getId(), Cards.CHASE_SAPPHIRE_PREFERRED);
        cardMap.put(Cards.AMAZON_PRIME.getId(), Cards.AMAZON_PRIME);
        cardMap.put(Cards.REI_COOP.getId(), Cards.REI_COOP);
        cardMap.put(Cards.DISCOVER_IT.getId(), Cards.DISCOVER_IT);
        cardMap.put(Cards.CITI_DOUBLE_CASH.getId(), Cards.CITI_DOUBLE_CASH);
        cardMap.put(Cards.CITI_REWARDS.getId(), Cards.CITI_REWARDS);
        cardMap.put(Cards.AMEX_BLUE_CASH_EVERYDAY.getId(), Cards.AMEX_BLUE_CASH_EVERYDAY);
        cardMap.put(Cards.AMEX_BLUE_CASH_PREFERRED.getId(), Cards.AMEX_BLUE_CASH_PREFERRED);
        cardMap.put(Cards.AMEX_GOLD_CARD.getId(), Cards.AMEX_GOLD_CARD);
        cardMap.put(Cards.AMEX_PLATINUM_CARD.getId(), Cards.AMEX_PLATINUM_CARD);
        cardMap.put(Cards.AMEX_GREEN_CARD.getId(), Cards.AMEX_GREEN_CARD);
        cardMap.put(Cards.AMEX_CASH_MAGNET.getId(), Cards.AMEX_CASH_MAGNET);
        cardMap.put(Cards.BANK_OF_AMERICA_CASH_REWARDS.getId(), Cards.BANK_OF_AMERICA_CASH_REWARDS);
        cardMap.put(Cards.USBANK_CASH_PLUS.getId(), Cards.USBANK_CASH_PLUS);
        cardMap.put(Cards.SOFI_CREDIT_CARD.getId(), Cards.SOFI_CREDIT_CARD);

        return cardMap;
    }

    public static List<Card> getCards() {
        final List<Card> cards = new ArrayList<>();

        cards.addAll(CARD_MAP.values());

        return cards;
    }

    public static Card getCard(
            final String cardId) {
        return CARD_MAP.get(cardId);
    }

    public static List<Category> getCategories() {
        final List<Category> categories = new ArrayList<>();

        categories.addAll(CATEGORY_MAP.values());

        return categories;
    }

    public static Category getCategory(
            final String categoryId) {
        return CATEGORY_MAP.get(categoryId);
    }

    public static Map<String, Category> loadCategories() {
        final Map<String, Category> categoryMap = new HashMap<>();

        categoryMap.put(Categories.GROCERY_STORE.getId(), Categories.GROCERY_STORE);
        categoryMap.put(Categories.GAS_STATION.getId(), Categories.GAS_STATION);
        categoryMap.put(Categories.PHARMACY.getId(), Categories.PHARMACY);
        categoryMap.put(Categories.RESTAURANT.getId(), Categories.RESTAURANT);
        categoryMap.put(Categories.DEPARTMENT_STORE.getId(), Categories.DEPARTMENT_STORE);
        categoryMap.put(Categories.TRAVEL.getId(), Categories.TRAVEL);
        categoryMap.put(Categories.REI.getId(), Categories.REI);
        categoryMap.put(Categories.WHOLESALE_CLUB.getId(), Categories.WHOLESALE_CLUB);
        categoryMap.put(Categories.AMAZON.getId(), Categories.AMAZON);
        categoryMap.put(Categories.WHOLE_FOODS.getId(), Categories.WHOLE_FOODS);
        categoryMap.put(Categories.WALMART.getId(), Categories.WALMART);
        categoryMap.put(Categories.TARGET.getId(), Categories.TARGET);
        categoryMap.put(Categories.HOME_IMPROVEMENT.getId(), Categories.HOME_IMPROVEMENT);
        categoryMap.put(Categories.RIDE_SHARE.getId(), Categories.RIDE_SHARE);
        categoryMap.put(Categories.PAYPAL.getId(), Categories.PAYPAL);
        categoryMap.put(Categories.CHASE_PAY.getId(), Categories.CHASE_PAY);
        categoryMap.put(Categories.STREAMING_SERVICE.getId(), Categories.STREAMING_SERVICE);
        categoryMap.put(Categories.TV.getId(), Categories.TV);
        categoryMap.put(Categories.INTERNET.getId(), Categories.INTERNET);
        categoryMap.put(Categories.FAST_FOOD.getId(), Categories.FAST_FOOD);
        categoryMap.put(Categories.CELL_PHONE_PROVIDERS.getId(), Categories.CELL_PHONE_PROVIDERS);
        categoryMap.put(Categories.HOME_UTILITIES.getId(), Categories.HOME_UTILITIES);
        categoryMap.put(Categories.CLOTHING_STORE.getId(), Categories.CLOTHING_STORE);
        categoryMap.put(Categories.ELECTRONICS_STORE.getId(), Categories.ELECTRONICS_STORE);
        categoryMap.put(Categories.SPORTING_GOODS_STORE.getId(), Categories.SPORTING_GOODS_STORE);
        categoryMap.put(Categories.FURNITURE_STORE.getId(), Categories.FURNITURE_STORE);
        categoryMap.put(Categories.MOVIE_THEATER.getId(), Categories.MOVIE_THEATER);
        categoryMap.put(Categories.FITNESS_CENTER.getId(), Categories.FITNESS_CENTER);
        categoryMap.put(Categories.GROUND_TRANSPORTATION.getId(), Categories.GROUND_TRANSPORTATION);

        categoryMap.put(Categories.EVERYTHING.getId(), Categories.EVERYTHING);

        return categoryMap;
    }
}
