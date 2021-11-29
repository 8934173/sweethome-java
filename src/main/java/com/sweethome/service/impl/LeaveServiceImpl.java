package com.sweethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.LeaveDao;
import com.sweethome.entities.LeaveEntity;
import com.sweethome.entities.RevertEntity;
import com.sweethome.service.LeaveService;
import com.sweethome.utils.BaseRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveDao, LeaveEntity> implements LeaveService {

    @Override
    public void applyForLeaving(LeaveEntity leaveEntity) {
        this.save(leaveEntity);
    }

    /**
     * 根据学生id获取 数据
     * @param stuId 学生id
     * @param auditStatus 审核状态 null为查找全部
     * @return list
     */
    @Override
    public List<LeaveEntity> getLeaveEntitiesByStuId(Long stuId, Integer auditStatus, String uname) {
        QueryWrapper<LeaveEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("stu_id", stuId);
        if (auditStatus != null) {
            wrapper.and(w -> w.eq("audit_status", auditStatus));
        }
        if (StringUtils.isNotBlank(uname)) {
            wrapper.and(w -> {
                w.eq("uname", uname);
            });
        }
        return this.list(wrapper);
    }

    @Override
    public void deleteRecordByOutId(Long outId) {
        LeaveEntity leave = this.getById(outId);
        Integer auditStatus = leave.getAuditStatus();
        if (auditStatus != 1) throw BaseRuntimeException.getException("只能删除待审核中的条目");
        this.removeById(outId);
    }

    @Override
    public List<LeaveEntity> getLeaveEntitiesByClassId(Long classId, String uname, Integer auditStatus) {
        QueryWrapper<LeaveEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("class_id", classId);
        if (StringUtils.isNotBlank(uname)) {
            wrapper.and(w -> {
                w.like("u_name", uname);
            });
        }
        if (auditStatus!=null) {
            wrapper.and(w -> {
                w.eq("audit_status", auditStatus);
            });
        }
        return list(wrapper);
    }

    @Override
    public void updateLeaveById(LeaveEntity leaveEntity) {
        this.updateById(leaveEntity);
    }
}
