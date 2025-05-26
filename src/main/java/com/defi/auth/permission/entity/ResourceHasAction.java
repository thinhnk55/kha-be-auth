package com.defi.auth.permission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource_has_action")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceHasAction {
    @EmbeddedId
    ResourceHasActionId id;
}
