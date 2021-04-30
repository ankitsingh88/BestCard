package com.ankitsingh.bestcard.model.codecs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class SavedCategoryRankMapCodec {

    private final ObjectMapper objectMapper;

    public SavedCategoryRankMapCodec() {
        this.objectMapper = new ObjectMapper();

        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Map<String, Long> deserialize(
            final String serializedValue) {
        try {
            return this.objectMapper.readValue(serializedValue, new TypeReference<Map<String, Long>>() {});
        } catch (final Exception e) {
            return new HashMap<>();
        }
    }

    public String serialize(
            final Map<String, Long> savedCategoryRankMap) {
        try {
            return this.objectMapper.writeValueAsString(savedCategoryRankMap);
        } catch (final Exception e) {
            return "{}";
        }
    }
}
