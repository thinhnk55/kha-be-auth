package com.defi.auth.user.dto;

import com.defi.common.CommonMessage;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = CommonMessage.INVALID)
    @Size(min = 3, max = 50, message = CommonMessage.INVALID)
    private String userName;

    @NotBlank(message = CommonMessage.INVALID)
    @Size(max = 100, message = CommonMessage.INVALID)
    private String fullName;

    @NotBlank(message = CommonMessage.INVALID)
    @Email(message = CommonMessage.INVALID)
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = CommonMessage.INVALID
    )
    private String email;

    @NotBlank(message = CommonMessage.INVALID)
    @Size(min = 8, max = 64, message = CommonMessage.INVALID)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$",
            message = CommonMessage.INVALID
    )
    private String password;

    @NotBlank(message = CommonMessage.INVALID)
    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,12}$", // ví dụ: +84123456789, 0912345678
            message = CommonMessage.INVALID
    )
    @Size(max = 20, message = CommonMessage.INVALID)
    private String phone;
}
