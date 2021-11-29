package com.sweethome.vo;

import com.sweethome.entities.StudentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentClockInVo extends StudentEntity {
    private Float clockPercentage;
}
