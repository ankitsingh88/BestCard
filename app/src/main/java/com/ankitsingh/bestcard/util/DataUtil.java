package com.ankitsingh.bestcard.util;

import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Cards;
import com.ankitsingh.bestcard.model.Categories;
import com.ankitsingh.bestcard.model.Category;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    public static List<Card> loadCards() {
        final List<Card> cards = new ArrayList<>();

        cards.add(Cards.CHASE_FREEDOM);
        cards.add(Cards.CHASE_FREEDOM_UNLIMITED);
        cards.add(Cards.CHASE_SAPPHIRE_RESERVE);
        cards.add(Cards.AMAZON_PRIME);
        cards.add(Cards.REI_COOP);
        cards.add(Cards.DISCOVER_IT);
        cards.add(Cards.CITI_REWARDS);
        cards.add(Cards.AMEX_BLUE_CASH_EVERYDAY);
        cards.add(Cards.BANKAMERICA_CASH_REWARDS);

        return cards;
    }

    public static List<Category> loadCategories() {
        final List<Category> categories = new ArrayList<>();

        categories.add(Categories.GROCERY_STORE);
        categories.add(Categories.GAS_STATION);
        categories.add(Categories.PHARMACY);
        categories.add(Categories.RESTAURANT);
        categories.add(Categories.DEPARTMENT_STORE);
        categories.add(Categories.TRAVEL);
        categories.add(Categories.REI);
        categories.add(Categories.WHOLESALE_CLUB);
        categories.add(Categories.AMAZON);
        categories.add(Categories.WHOLE_FOODS);
        categories.add(Categories.WALMART);
        categories.add(Categories.TARGET);
        categories.add(Categories.HOME_IMPROVEMENT);
        categories.add(Categories.UBER);
        categories.add(Categories.LYFT);
        categories.add(Categories.PAYPAL);
        categories.add(Categories.NETFLIX);
        categories.add(Categories.HULU);
        categories.add(Categories.SLING_TV);
        categories.add(Categories.VUDU);
        categories.add(Categories.FUBO_TV);
        categories.add(Categories.APPLE_MUSIC);
        categories.add(Categories.SIRIUS_XM);
        categories.add(Categories.PANDORA);
        categories.add(Categories.SPOTIFY);

        categories.add(Categories.EVERYTHING);

        return categories;
    }
}
