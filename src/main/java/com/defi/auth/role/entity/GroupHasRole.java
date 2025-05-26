package com.defi.auth.role.entity;

import jakarta.persistence.EmbeddedId;

public class GroupHasRole {
    @EmbeddedId
    GroupHasRoleId id;
}
