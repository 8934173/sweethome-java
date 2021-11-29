package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.RevertEntity;

import java.util.List;

public interface RevertService extends IService<RevertEntity> {

    void applyForRevert(RevertEntity revertEntity);

    List<RevertEntity> getLeaveEntityByStuId(Long stuId, Integer auditStatus, String uname);

    void deleteRecordByOutId(Long inId);

    List<RevertEntity> getRevertEntitiesByClassId(Long classId, String uname, Integer auditStatus);

    void updateRevertById(RevertEntity revertEntity);
}
