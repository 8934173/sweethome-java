package com.sweethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sweethome.entities.ClassEntity;
import com.sweethome.entities.ClockInEntity;
import com.sweethome.utils.PageUtil;
import com.sweethome.vo.PageVo;

import java.util.List;

public interface ClockInService extends IService<ClockInEntity> {
    void saveClockIn(ClockInEntity clockInEntity);

    PageUtil getClockIns(PageVo pages, String uId);

    boolean updateClockInByCid(ClockInEntity clockInEntity);

    boolean deleteClockInByCid(String cid);

    ClockInEntity getOneClockInByTime(String uid);

}
