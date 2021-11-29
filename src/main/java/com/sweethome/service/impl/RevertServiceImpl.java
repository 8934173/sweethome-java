package com.sweethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.RevertDao;
import com.sweethome.entities.RevertEntity;
import com.sweethome.service.RevertService;
import com.sweethome.utils.BaseRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevertServiceImpl extends ServiceImpl<RevertDao, RevertEntity> implements RevertService {

    @Override
    public void applyForRevert(RevertEntity revertEntity) {
        this.save(revertEntity);
    }

    @Override
    public List<RevertEntity> getLeaveEntityByStuId(Long stuId, Integer auditStatus, String uname) {
        QueryWrapper<RevertEntity> wrapper = new QueryWrapper<>();
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
    public void deleteRecordByOutId(Long inId) {
        RevertEntity revert = this.getById(inId);
        Integer auditStatus = revert.getAuditStatus();
        if (auditStatus != 1) throw BaseRuntimeException.getException("只能删除待审核中的条目");
        this.removeById(inId);
    }

    @Override
    public List<RevertEntity> getRevertEntitiesByClassId(Long classId, String uname, Integer auditStatus) {
        QueryWrapper<RevertEntity> wrapper = new QueryWrapper<>();
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
    public void updateRevertById(RevertEntity revertEntity) {
        this.updateById(revertEntity);
    }
}
