package com.ankitsingh.bestcard.util;

import com.ankitsingh.bestcard.model.Category;
import com.google.common.collect.ImmutableSet;

public class Categories {

    public static final Category GROCERY_STORE =
            Category.builder()
                    .id("bda411f0-153a-42bb-829c-769ac72f172d")
                    .name("Grocery Store")
                    .iconName("grocery_store")
                    .tags(ImmutableSet.of(Tags.FOOD, Tags.VEGETABLES, Tags.GROCERIES))
                    .build();
    public static final Category GAS_STATION =
            Category.builder()
                    .id("7088c581-7ef9-4e33-8f68-434f1c6d3921")
                    .name("Gas Station")
                    .iconName("gas_station")
                    .tags(ImmutableSet.of(Tags.FUEL, Tags.PUMP, Tags.PETROL, Tags.GASOLINE))
                    .build();
    public static final Category PHARMACY =
            Category.builder()
                    .id("8f94dba1-2def-432f-9a6a-49cfcca34bab")
                    .name("Pharmacy")
                    .iconName("pharmacy")
                    .tags(ImmutableSet.of(Tags.MEDICINE, Tags.DRUG_STORE))
                    .build();
    public static final Category RESTAURANT =
            Category.builder()
                    .id("9d49309b-588e-477e-8edc-1988aa548cd8")
                    .name("Restaurant")
                    .iconName("restaurant")
                    .tags(ImmutableSet.of(Tags.FOOD, Tags.DINING))
                    .build();
    public static final Category DEPARTMENT_STORE =
            Category.builder()
                    .id("a9b5eb4b-f327-4f7d-bbe1-da91f194e5e2")
                    .name("Department Store")
                    .iconName("department_store")
                    .build();
    public static final Category TRAVEL =
            Category.builder()
                    .id("941c416c-8331-42dc-947c-02cfdb08dc3f")
                    .name("Travel")
                    .iconName("travel")
                    .tags(ImmutableSet.of(Tags.FLIGHT, Tags.VACATION))
                    .build();
    public static final Category REI =
            Category.builder()
                    .id("d7666c3b-49ad-4844-9431-4c02f7bf4a89")
                    .name("REI")
                    .iconName("rei")
                    .tags(ImmutableSet.of(Tags.HIKING, Tags.OUTDOORS))
                    .build();
    public static final Category WHOLESALE_CLUB =
            Category.builder()
                    .id("3b6142f8-c3e9-4385-94be-75e349aeba07")
                    .name("Wholesale Club")
                    .iconName("wholesale_club")
                    .tags(ImmutableSet.of(Tags.COSTSCO, Tags.SAMS_CLUB, Tags.DEPARTMENT_STORE))
                    .build();
    public static final Category AMAZON =
            Category.builder()
                    .id("e45a7143-6613-48b2-9371-d4dd2cd73ea2")
                    .name("Amazon")
                    .iconName("amazon")
                    .build();
    public static final Category WHOLE_FOODS =
            Category.builder()
                    .id("77dc0674-e0e3-40f6-9111-8e4a4a9bdf37")
                    .name("Whole Foods")
                    .iconName("whole_foods")
                    .tags(ImmutableSet.of(Tags.DEPARTMENT_STORE))
                    .build();
    public static final Category WALMART =
            Category.builder()
                    .id("3e11422d-ea10-4395-b972-a2036faed7a0")
                    .name("Walmart")
                    .iconName("walmart")
                    .tags(ImmutableSet.of(Tags.DEPARTMENT_STORE))
                    .build();
    public static final Category TARGET =
            Category.builder()
                    .id("2fcd507a-970a-4a9a-b357-441d0a35935d")
                    .name("Target")
                    .iconName("target")
                    .tags(ImmutableSet.of(Tags.DEPARTMENT_STORE))
                    .build();
    public static final Category HOME_IMPROVEMENT =
            Category.builder()
                    .id("0426a2c8-0f0b-446f-8c26-938adf6133e6")
                    .name("Home Improvement")
                    .iconName("home_improvement")
                    .tags(ImmutableSet.of(Tags.DEPARTMENT_STORE))
                    .build();
    public static final Category STREAMING_SERVICE =
            Category.builder()
                    .id("a08c6b25-f671-4cc5-8677-983e8d3ddca8")
                    .name("Streaming Service")
                    .iconName("streaming_service")
                    .tags(ImmutableSet.of(Tags.NETFLIX, Tags.HULU, Tags.PANDORA, Tags.SPOTIFY))
                    .build();
    public static final Category RIDE_SHARE =
            Category.builder()
                    .id("d5b2d90f-acde-4021-814d-6d5d157a6c25")
                    .name("Ride Share")
                    .iconName("ride_share")
                    .tags(ImmutableSet.of(Tags.CAR_POOL, Tags.UBER, Tags.LYFT))
                    .build();
    public static final Category PAYPAL =
            Category.builder()
                    .id("334a3366-b3ef-4781-8397-e60b769dd061")
                    .name("PayPal")
                    .iconName("paypal")
                    .tags(ImmutableSet.of(Tags.PAYMENT, Tags.MONEY, Tags.TRANSFER))
                    .build();
    public static final Category CHASE_PAY =
            Category.builder()
                    .id("1691f097-0049-4fce-bd41-2b577f06dd39")
                    .name("Chase Pay")
                    .iconName("chase_pay")
                    .tags(ImmutableSet.of(Tags.PAYMENT, Tags.MONEY))
                    .build();
    public static final Category TV =
            Category.builder()
                    .id("7ec78a78-24b8-4745-8afd-f2519825ccc9")
                    .name("TV")
                    .iconName("tv")
                    .tags(ImmutableSet.of())
                    .build();
    public static final Category INTERNET =
            Category.builder()
                    .id("5f92547f-04ac-4cf0-b0c0-8613a578b8f2")
                    .name("Internet")
                    .iconName("internet")
                    .tags(ImmutableSet.of(Tags.WEB))
                    .build();
    public static final Category FAST_FOOD =
            Category.builder()
                    .id("dc938180-695a-45a1-a615-db56c7939897")
                    .name("Fast Food")
                    .iconName("fast_food")
                    .tags(ImmutableSet.of(Tags.FOOD))
                    .build();
    public static final Category CELL_PHONE_PROVIDERS =
            Category.builder()
                    .id("1a5abf33-0218-4b2c-900e-05ae2c99671b")
                    .name("Cell Phone Providers")
                    .iconName("phone")
                    .tags(ImmutableSet.of(Tags.CARRIER))
                    .build();
    public static final Category HOME_UTILITIES =
            Category.builder()
                    .id("3a861a95-4a3d-4f96-aa20-787104c59ab7")
                    .name("Home Utilities")
                    .iconName("home_utilities")
                    .tags(ImmutableSet.of(Tags.ELECTRICITY))
                    .build();
    public static final Category CLOTHING_STORE =
            Category.builder()
                    .id("6a9d9f00-1fef-42a1-9c91-956878a6596a")
                    .name("Clothing Store")
                    .iconName("clothing_store")
                    .tags(ImmutableSet.of(Tags.CLOTH, Tags.DRESS))
                    .build();
    public static final Category ELECTRONICS_STORE =
            Category.builder()
                    .id("9f892b9f-b980-4252-8b12-19bb68184f7e")
                    .name("Electronics Store")
                    .iconName("electronics_store")
                    .tags(ImmutableSet.of(Tags.LAPTOP, Tags.CAMERA))
                    .build();
    public static final Category SPORTING_GOODS_STORE =
            Category.builder()
                    .id("7ad9dca2-215b-48d0-9de7-b7eebbafbef0")
                    .name("Sporting Goods Store")
                    .iconName("sporting_goods_store")
                    .tags(ImmutableSet.of(Tags.EQUIPMENT))
                    .build();
    public static final Category FURNITURE_STORE =
            Category.builder()
                    .id("81878dbc-bdeb-4e9d-8f4e-2e0812cae638")
                    .name("Furniture Store")
                    .iconName("furniture_store")
                    .tags(ImmutableSet.of(Tags.CHAIR, Tags.DESK, Tags.BED))
                    .build();
    public static final Category MOVIE_THEATER =
            Category.builder()
                    .id("16025c29-9e77-4e28-a6cf-015abdb522c4")
                    .name("Movie Theater")
                    .iconName("movie_theater")
                    .tags(ImmutableSet.of(Tags.BOX_OFFICE, Tags.PREMIER))
                    .build();
    public static final Category FITNESS_CENTER =
            Category.builder()
                    .id("a5b28754-b7ba-4fc5-971a-1a359c1147b2")
                    .name("Fitness Center")
                    .iconName("fitness_center")
                    .tags(ImmutableSet.of(Tags.GYM, Tags.CARDIO, Tags.TREADMILL, Tags.ELLIPTICAL))
                    .build();
    public static final Category GROUND_TRANSPORTATION =
            Category.builder()
                    .id("784cc56e-1e91-40e8-ab88-a200b08374b6")
                    .name("Ground Transportation")
                    .iconName("ground_transportation")
                    .tags(ImmutableSet.of(Tags.TAXI, Tags.CAB, Tags.BUS, Tags.METRO, Tags.SUBWAY))
                    .build();
    public static final Category EVERYTHING =
            Category.builder()
                    .id("8166e782-2172-4dda-9196-f530a2f58778")
                    .name("Everything")
                    .iconName("category")
                    .tags(ImmutableSet.of(Tags.ANYTHING))
                    .build();
}
