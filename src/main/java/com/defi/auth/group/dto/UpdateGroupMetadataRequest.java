package com.defi.auth.group.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGroupMetadataRequest {
    private ObjectNode metadata;
}