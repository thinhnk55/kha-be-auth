package com.defi.auth.group.dto;

import com.defi.common.api.CommonMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGroupRequest {
    private Long parentId;

    @NotBlank(message = CommonMessage.INVALID)
    @Size(min = 3, max = 64, message = CommonMessage.INVALID)
    @Pattern(
            regexp = "^[a-z0-9-]+$",
            message = CommonMessage.INVALID
    )
    private String code;

    @NotBlank(message = CommonMessage.INVALID)
    @Size(max = 128, message = CommonMessage.INVALID)
    private String name;
}
