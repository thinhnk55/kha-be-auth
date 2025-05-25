package com.defi.auth.session.entity;

import com.defi.common.BaseModel;
import lombok.Data;

@Data
public class Session extends BaseModel {
    private Long userId;
    private long startTime;
    private long refreshTime;
    private long expiredTime;
    private long notBefore;
    private long duration;
    private String ipAddress;
    private String userAgent;
}
