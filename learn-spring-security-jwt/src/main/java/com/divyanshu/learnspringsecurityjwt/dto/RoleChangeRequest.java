package com.divyanshu.learnspringsecurityjwt.dto;

import com.divyanshu.learnspringsecurityjwt.entity.Role;
import lombok.Data;

@Data
public class RoleChangeRequest {

    private Role role;

}