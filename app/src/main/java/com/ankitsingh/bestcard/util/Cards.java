package com.ankitsingh.bestcard.util;

import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.CardType;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.DefaultInterval;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.google.common.collect.ImmutableList;

import java.time.LocalDate;

public class Cards {

    public static final Card CHASE_FREEDOM =
            Card.builder()
                    .id("7b4440d1-e8ca-46bb-8a2a-f1ede767d99d")
                    .name("Chase Freedom")
                    .iconName("chase_freedom")
                    .cardType(CardType.VISA)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.DEPARTMENT_STORE.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.PAYPAL.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.CHASE_PAY.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();

    public static final Card CHASE_FREEDOM_FLEX =
            Card.builder()
                    .id("f9f6280d-3f85-47f5-ba7a-7c8b7cef11a2")
                    .name("Chase Freedom Flex")
                    .iconName("chase_freedom_flex")
                    .cardType(CardType.MASTERCARD)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.TRAVEL.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.PHARMACY.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.PAYPAL.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 10, 1))
                                                                    .endDate(LocalDate.of(2020, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.WALMART.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 10, 1))
                                                                    .endDate(LocalDate.of(2020, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();

    public static final Card CHASE_FREEDOM_UNLIMITED =
            Card.builder()
                    .id("90a8e76f-a360-4d43-8b3e-46fb862f3f55")
                    .name("Chase Freedom Unlimited")
                    .iconName("chase_freedom_unlimited")
                    .cardType(CardType.VISA)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(1.5)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();

    public static final Card CAPITAL_ONE_QUICKSILVER =
            Card.builder()
                    .id("103726da-8c34-445a-8bd8-3a2c3816d633")
                    .name("CapitalOne Quicksilver")
                    .iconName("capitalone_quicksilver")
                    .cardType(CardType.VISA)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(1.5)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();

    public static final Card CHASE_SAPPHIRE_RESERVE =
            Card.builder()
                    .id("9ee35b19-f842-411c-863c-44e4d1484e49")
                    .name("Chase Sapphire Reserve")
                    .iconName("chase_sapphire_reserve")
                    .cardType(CardType.VISA)
                    .annualFees(450)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.TRAVEL.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card CHASE_SAPPHIRE_PREFERRED =
            Card.builder()
                    .id("8a25bb24-dad9-4031-be5c-0bdfafab28ac")
                    .name("Chase Sapphire Preferred")
                    .iconName("chase_sapphire_preferred")
                    .cardType(CardType.VISA)
                    .annualFees(95)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.TRAVEL.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMAZON_PRIME =
            Card.builder()
                    .id("8c7bddd6-b1c6-4ee4-82ba-c1b42d603b82")
                    .name("Amazon Prime")
                    .iconName("amazon_prime")
                    .cardType(CardType.VISA)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.AMAZON.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.WHOLE_FOODS.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.GAS_STATION.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.PHARMACY.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card REI_COOP =
            Card.builder()
                    .id("96c224e2-9cbc-442c-b200-e0c9013765c8")
                    .name("REI Co-op")
                    .iconName("rei_coop")
                    .cardType(CardType.MASTERCARD)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.REI.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card DISCOVER_IT =
            Card.builder()
                    .id("fecbb967-ef5b-4f36-92cd-15aaf8c400ae")
                    .name("Discover It")
                    .iconName("discover_it")
                    .cardType(CardType.DISCOVER)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.AMAZON.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.WALMART.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.TARGET.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.GROCERY_STORE.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 1, 1))
                                                                    .endDate(LocalDate.of(2020, 3, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.PHARMACY.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 1, 1))
                                                                    .endDate(LocalDate.of(2020, 3, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.GAS_STATION.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 4, 1))
                                                                    .endDate(LocalDate.of(2020, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.RIDE_SHARE.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 4, 1))
                                                                    .endDate(LocalDate.of(2020, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.WHOLESALE_CLUB.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 4, 1))
                                                                    .endDate(LocalDate.of(2020, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 7, 1))
                                                                    .endDate(LocalDate.of(2020, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.PAYPAL.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 7, 1))
                                                                    .endDate(LocalDate.of(2020, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.AMAZON.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 10, 1))
                                                                    .endDate(LocalDate.of(2020, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.WALMART.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 10, 1))
                                                                    .endDate(LocalDate.of(2020, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.TARGET.getId())
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2020, 10, 1))
                                                                    .endDate(LocalDate.of(2020, 12, 31))
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card CITI_REWARDS =
            Card.builder()
                    .id("c638ecdc-3a1a-448c-ab84-ad77466597ea")
                    .name("Citi Rewards+")
                    .iconName("citi_rewards")
                    .cardType(CardType.MASTERCARD)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.GROCERY_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.GAS_STATION.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card CITI_DOUBLE_CASH =
            Card.builder()
                    .id("46d17342-283d-4134-8028-cd79c243a808")
                    .name("Citi Double Cash")
                    .iconName("citi_double_cash")
                    .cardType(CardType.MASTERCARD)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMEX_BLUE_CASH_EVERYDAY =
            Card.builder()
                    .id("749b19c7-64d4-4da9-8edb-d9ba9d3860c4")
                    .name("Blue Cash Everyday")
                    .iconName("blue_cash_everyday")
                    .cardType(CardType.AMEX)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.GROCERY_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.DEPARTMENT_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.GAS_STATION.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMEX_BLUE_CASH_PREFERRED =
            Card.builder()
                    .id("065510c2-2e6f-4746-8e95-950675ffbcdc")
                    .name("Blue Cash Preferred")
                    .iconName("blue_cash_preferred")
                    .cardType(CardType.AMEX)
                    .annualFees(95)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(6.0)
                                                    .categoryId(Categories.GROCERY_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(6.0)
                                                    .categoryId(Categories.STREAMING_SERVICE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.GAS_STATION.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.RIDE_SHARE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMEX_GOLD_CARD =
            Card.builder()
                    .id("0fa6f789-abde-4599-99df-a2de620cc6ff")
                    .name("Amex Gold Card")
                    .iconName("amex_gold_card")
                    .cardType(CardType.AMEX)
                    .annualFees(250)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(4.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(4.0)
                                                    .categoryId(Categories.DEPARTMENT_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.TRAVEL.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMEX_PLATINUM_CARD =
            Card.builder()
                    .id("e37686a5-63b9-4b1d-9af1-8f8c4f7a7ba9")
                    .name("Amex Platinum Card")
                    .iconName("amex_platinum_card")
                    .cardType(CardType.AMEX)
                    .annualFees(550)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.TRAVEL.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMEX_GREEN_CARD =
            Card.builder()
                    .id("c20e6d48-be68-4fd7-b5d7-ad4fd7f5485d")
                    .name("Amex Green Card")
                    .iconName("amex_green_card")
                    .cardType(CardType.AMEX)
                    .annualFees(150)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .categoryId(Categories.TRAVEL.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMEX_CASH_MAGNET =
            Card.builder()
                    .id("f286ed2d-78ea-4f62-bd97-8f221017ed2f")
                    .name("Cash Magnet")
                    .iconName("amex_cash_magnet")
                    .cardType(CardType.AMEX)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card SOFI_CREDIT_CARD =
            Card.builder()
                    .id("b169a350-3daf-4bbd-a69a-28f17b9b13de")
                    .name("Sofi Credit Card")
                    .iconName("sofi_credit_card")
                    .cardType(CardType.MASTERCARD)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card BANK_OF_AMERICA_CASH_REWARDS =
            Card.builder()
                    .id("b0fc1d57-61e6-4f71-bad6-03802b9c6fc6")
                    .name("BofA Cash Rewards")
                    .iconName("bankofamerica_cash_rewards")
                    .cardType(CardType.VISA)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.GROCERY_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.WHOLESALE_CLUB.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .categoryId(Categories.EVERYTHING.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();

    public static final Card USBANK_CASH_PLUS =
            Card.builder()
                    .id("b954194a-16e4-48e3-b740-46eac8416a32")
                    .name("USBank Cash+")
                    .iconName("usbank_cash_plus")
                    .cardType(CardType.VISA)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.TV.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.INTERNET.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.STREAMING_SERVICE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.FAST_FOOD.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.CELL_PHONE_PROVIDERS.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.DEPARTMENT_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.HOME_UTILITIES.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.CLOTHING_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.ELECTRONICS_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.SPORTING_GOODS_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.FURNITURE_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.MOVIE_THEATER.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.FITNESS_CENTER.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .categoryId(Categories.GROUND_TRANSPORTATION.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.GROCERY_STORE.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.GAS_STATION.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .categoryId(Categories.RESTAURANT.getId())
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
}
