package com.ankitsingh.bestcard.model.codecs;

import com.ankitsingh.bestcard.model.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class SavedCategoriesMapCodec {

    private final ObjectMapper objectMapper;

    public SavedCategoriesMapCodec() {
        this.objectMapper = new ObjectMapper();

        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Map<String, Category> deserialize(
            final String serializedValue) {
        try {
            return this.objectMapper.readValue(serializedValue, new TypeReference<Map<String, Category>>() {});
        } catch (final Exception e) {
            return new HashMap<>();
        }
    }

    public String serialize(
            final Map<String, Category> savedCategoriesMap) {
        try {
            return this.objectMapper.writeValueAsString(savedCategoriesMap);
        } catch (final Exception e) {
            return "{}";
        }
    }
}
