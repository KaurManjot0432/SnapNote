package com.manjot.snapnote.dto.authentication.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}