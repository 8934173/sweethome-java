package com.sweethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.StudentDao;
import com.sweethome.entities.StudentEntity;
import com.sweethome.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentDao, StudentEntity> implements StudentService {

    @Override
    public List<StudentEntity> getStudentByIds(List<Long> ids) {
        return this.baseMapper.selectBatchIds(ids);
    }

    @Override
    public List<StudentEntity> getStudentAll() {
        return this.list();
    }

    @Override
    public List<StudentEntity> getStudentByClassId(Long caId) {
        return this.list(new QueryWrapper<StudentEntity>().eq("ca_id", caId));
    }

    @Override
    public void updateStudentsByClassId(Long caId, Long newCaId) {
        baseMapper.updateStudentsByClassId(caId, newCaId);
    }

    @Override
    public List<StudentEntity> getStudentByUid(String uid) {
        return this.list(new QueryWrapper<StudentEntity>().eq("uid", uid));
    }

    @Override
    public StudentEntity getStudentOneByUid(String uid) {
        return this.getOne(new QueryWrapper<StudentEntity>().eq("uid", uid));
    }

    @Override
    public void deleteRoRecoverStudentByUid(String uid, Integer status) {
        this.baseMapper.deleteRoRecoverStudentByUid(uid, status);
    }
}
