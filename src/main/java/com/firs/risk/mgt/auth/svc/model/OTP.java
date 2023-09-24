package com.firs.risk.mgt.auth.svc.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(as = OTP.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OTP implements Serializable {
    private String phone;
    private String email;
    private String channel;
    private String code;
}