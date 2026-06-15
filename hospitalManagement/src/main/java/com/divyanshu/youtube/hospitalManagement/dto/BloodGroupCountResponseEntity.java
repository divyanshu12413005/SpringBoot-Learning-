package com.divyanshu.youtube.hospitalManagement.dto;

import com.divyanshu.youtube.hospitalManagement.entity.type.BloodGroupType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BloodGroupCountResponseEntity {
    private BloodGroupType bloodGroup;
    private Long count;
}