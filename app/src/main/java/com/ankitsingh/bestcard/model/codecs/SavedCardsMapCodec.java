package com.ankitsingh.bestcard.model.codecs;

import com.ankitsingh.bestcard.model.Card;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.Map;

public class SavedCardsMapCodec {

    private final ObjectMapper objectMapper;

    public SavedCardsMapCodec() {
        this.objectMapper = new ObjectMapper();

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Map<String, Card> deserialize(
            final String serializedValue) {
        try {
            return this.objectMapper.readValue(serializedValue, new TypeReference<Map<String, Card>>() {});
        } catch (final Exception e) {
            return new HashMap<>();
        }
    }

    public String serialize(
            final Map<String, Card> savedCardsMap) {
        try {
            return this.objectMapper.writeValueAsString(savedCardsMap);
        } catch (final Exception e) {
            return "{}";
        }
    }
}
