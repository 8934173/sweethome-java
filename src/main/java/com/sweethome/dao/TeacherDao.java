package com.sweethome.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweethome.entities.TeacherEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherDao extends BaseMapper<TeacherEntity> {
    void updateClass(@Param("tea_id") Long teaId, @Param("cla_id") String claId);

    List<TeacherEntity> getTeacherByUid(@Param("uIds") List<String> uIds);

    void deleteRoRecoverTeacherByUid(@Param("uid") String uid, @Param("status") Integer status);
}
