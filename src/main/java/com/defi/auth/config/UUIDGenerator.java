package com.defi.auth.config;

import java.util.UUID;

import com.github.f4b6a3.uuid.alt.GUID;

public class UUIDGenerator {
    public UUID uuidV1() {
        return GUID.v1().toUUID();
    }

    public UUID uuidV4() {
        return GUID.v4().toUUID();
    }

    public UUID uuidV7() {
        return GUID.v7().toUUID();
    }
}
