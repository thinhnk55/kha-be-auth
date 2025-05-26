package com.defi.auth.user.dto;

import com.defi.common.CommonMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = CommonMessage.INVALID)
    @Size(min = 3, max = 50, message = CommonMessage.INVALID)
    private String userName;

    @NotBlank(message = CommonMessage.INVALID)
    @Size(min = 8, max = 64, message = CommonMessage.INVALID)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$",
            message = CommonMessage.INVALID
    )
    private String password;
}
