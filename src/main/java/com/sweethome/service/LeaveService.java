package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.LeaveEntity;

import java.util.List;

public interface LeaveService extends IService<LeaveEntity> {

    void applyForLeaving(LeaveEntity leaveEntity);

    List<LeaveEntity> getLeaveEntitiesByStuId(Long stuId, Integer auditStatus, String uname);

    void deleteRecordByOutId(Long outId);

    List<LeaveEntity> getLeaveEntitiesByClassId(Long classId, String uname, Integer auditStatus);

    void updateLeaveById(LeaveEntity leaveEntity);
}
