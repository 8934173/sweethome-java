package com.sweethome.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sweethome.entities.StudentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudentDao extends BaseMapper<StudentEntity> {
    void updateStudentsByClassId(@Param("caId") Long caId, @Param("newCaId") Long newCaId);

    void deleteRoRecoverStudentByUid(@Param("uid") String uid, @Param("status") Integer status);
}
