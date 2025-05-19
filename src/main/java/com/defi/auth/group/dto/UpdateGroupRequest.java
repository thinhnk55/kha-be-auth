package com.defi.auth.group.dto;

import com.defi.common.CommonMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGroupRequest {
    @NotBlank(message = CommonMessage.INVALID)
    @Size(max = 128, message = CommonMessage.INVALID)
    private String name;
}
