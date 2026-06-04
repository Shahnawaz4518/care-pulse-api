package com.devshahnawaz.hospitalManagement.dto;

import java.util.HashSet;
import java.util.Set;

import com.devshahnawaz.hospitalManagement.entity.type.RoleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String username;
    private String password;
    private String name;

    private Set<RoleType> roles = new HashSet<>();

}
