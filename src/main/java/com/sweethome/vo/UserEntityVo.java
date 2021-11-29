package com.sweethome.vo;

import com.sweethome.entities.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntityVo extends UserEntity {
    /**
     * 学生id
     */
    private Long stuId;

    /**
     * 学生名字
     */
    private String stuName;

    /**
     * 教室id 教室对应班主任，这样学生和班主任就有对应关系
     */
    private Long caId;

    /**
     * 教室名称
     */
    private String claName;

    /**
     * 教师id
     */
    private Long teaId;

    /**
     * 教师名字
     */
    private String teaName;

    /**
     * 学院id
     */
    private Long collegeId;

    /**
     * 所带领的班级 [] 数组模板字符串
     */
    private String caIds;


    private Integer status;
}
