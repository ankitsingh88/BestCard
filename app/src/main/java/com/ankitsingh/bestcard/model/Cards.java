package com.ankitsingh.bestcard.model;

import com.ankitsingh.bestcard.model.interval.DefaultInterval;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.google.common.collect.ImmutableList;

import java.time.LocalDate;
import java.util.UUID;

public class Cards {

    public static final Card CHASE_FREEDOM =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Chase Freedom")
                    .iconName("chase_freedom")
                    .cardType(CardType.VISA)
                    .multiplier(1.5)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.GROCERY_STORE)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 4, 1))
                                                                    .endDate(LocalDate.of(2019, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.HOME_IMPROVEMENT)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 4, 1))
                                                                    .endDate(LocalDate.of(2019, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.NETFLIX)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.HULU)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.SLING_TV)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.VUDU)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.FUBO_TV)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.APPLE_MUSIC)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.SIRIUS_XM)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.PANDORA)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.SPOTIFY)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card CHASE_FREEDOM_UNLIMITED =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Chase Freedom Unlimited")
                    .iconName("chase_freedom_unlimited")
                    .cardType(CardType.VISA)
                    .multiplier(1.5)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(1.5)
                                                    .category(Categories.EVERYTHING)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card CHASE_SAPPHIRE_RESERVE =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Chase Sapphire Reserve")
                    .iconName("chase_sapphire_reserve")
                    .cardType(CardType.VISA)
                    .multiplier(1.5)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .category(Categories.TRAVEL)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .category(Categories.RESTAURANT)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .category(Categories.EVERYTHING)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMAZON_PRIME =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Amazon Prime")
                    .iconName("amazon_prime")
                    .cardType(CardType.VISA)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.AMAZON)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.WHOLE_FOODS)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.RESTAURANT)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.GAS_STATION)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.PHARMACY)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .category(Categories.EVERYTHING)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card REI_COOP =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("REI Co-op")
                    .iconName("rei_coop")
                    .cardType(CardType.MASTERCARD)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.REI)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .category(Categories.EVERYTHING)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card DISCOVER_IT =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Discover It")
                    .iconName("discover_it")
                    .cardType(CardType.DISCOVER)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.GAS_STATION)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 4, 1))
                                                                    .endDate(LocalDate.of(2019, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.UBER)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 4, 1))
                                                                    .endDate(LocalDate.of(2019, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.LYFT)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 4, 1))
                                                                    .endDate(LocalDate.of(2019, 6, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.RESTAURANT)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.PAYPAL)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 7, 1))
                                                                    .endDate(LocalDate.of(2019, 9, 30))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.AMAZON)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.WALMART)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(5.0)
                                                    .category(Categories.TARGET)
                                                    .interval(
                                                            TimeInterval.builder()
                                                                    .startDate(LocalDate.of(2019, 10, 1))
                                                                    .endDate(LocalDate.of(2019, 12, 31))
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card CITI_REWARDS =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Citi Rewards+")
                    .iconName("citi_rewards")
                    .cardType(CardType.MASTERCARD)
                    .multiplier(1.1)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.GROCERY_STORE)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.GAS_STATION)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .category(Categories.EVERYTHING)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card AMEX_BLUE_CASH_EVERYDAY =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Amex Blue Cash Everyday")
                    .iconName("amex_blue_cash_everyday")
                    .cardType(CardType.AMEX)
                    .multiplier(1.0)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .category(Categories.GROCERY_STORE)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.GAS_STATION)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.DEPARTMENT_STORE)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .category(Categories.EVERYTHING)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
    public static final Card BANKAMERICA_CASH_REWARDS =
            Card.builder()
                    .id(UUID.randomUUID().toString())
                    .name("BankAmerica Cash Rewards")
                    .iconName("bankamerica_cash_rewards")
                    .cardType(CardType.VISA)
                    .multiplier(1.1)
                    .rewards(
                            new ImmutableList.Builder<Reward>()
                                    .add(
                                            Reward.builder()
                                                    .value(3.0)
                                                    .category(Categories.GAS_STATION)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.GROCERY_STORE)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(2.0)
                                                    .category(Categories.WHOLESALE_CLUB)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .add(
                                            Reward.builder()
                                                    .value(1.0)
                                                    .category(Categories.EVERYTHING)
                                                    .interval(DefaultInterval.builder().build())
                                                    .build())
                                    .build())
                    .build();
}
