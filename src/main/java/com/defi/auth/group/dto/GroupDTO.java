package com.defi.auth.group.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDTO {
    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private ObjectNode metadata;
    private Long createdAt;
    private Long updatedAt;
}