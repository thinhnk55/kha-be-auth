package com.defi.auth.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ObjectNodeConverter implements AttributeConverter<ObjectNode, String> {

    private ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(ObjectNode objectNode) {
        try {
            return objectNode != null ? mapper.writeValueAsString(objectNode) : null;
        } catch (Exception e) {
            throw new RuntimeException("Could not serialize ObjectNode", e);
        }
    }

    @Override
    public ObjectNode convertToEntityAttribute(String json) {
        try {
            return json != null ? (ObjectNode) mapper.readTree(json) : null;
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize to ObjectNode", e);
        }
    }
}
