package com.defi.auth.user.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
public class UpdateMetadataRequest {
    private ObjectNode metadata;
}
