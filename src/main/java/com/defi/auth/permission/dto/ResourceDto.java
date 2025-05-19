package com.defi.auth.permission.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import java.util.List;

@Data
public class ResourceDto {
    private Long id;
    private Long created_at;
    private Long updated_at;
    private String code;
    private String name;
    private ObjectNode metadata;
    private List<Long> actions;
}
