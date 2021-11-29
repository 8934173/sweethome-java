package com.sweethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.ClockInDao;
import com.sweethome.entities.ClockInEntity;
import com.sweethome.service.ClockInService;
import com.sweethome.utils.DateUtilSweet;
import com.sweethome.utils.PageUtil;
import com.sweethome.utils.Query;
import com.sweethome.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ClockInServiceImpl extends ServiceImpl<ClockInDao, ClockInEntity> implements ClockInService {
    @Override
    public void saveClockIn(ClockInEntity clockInEntity) {
        this.save(clockInEntity);
    }

    /**
     *
     * @param pages 分页参数
     * @param uId uid
     * @return 分页数据
     */
    @Override
    public PageUtil getClockIns(PageVo pages, String uId) {
        QueryWrapper<ClockInEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id", uId);
        String start = pages.getStart();
        String end = pages.getEnd();

        if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
            wrapper.and( w -> {
                w.between("clock_in_time", start, end);
            });
        }
        IPage<ClockInEntity> page = this.page(
                new Query<ClockInEntity>().getPage(pages),
                wrapper
        );
        return new PageUtil(page);
    }

    /**
     * 只能更新当天的打卡记录
     * @param clockInEntity 打卡项
     * @return 是否更新成功
     */
    @Override
    public boolean updateClockInByCid(ClockInEntity clockInEntity) {
        String clockInTime = clockInEntity.getClockInTime();
        String date = DateUtilSweet.dateToString(new Date(), DateUtilSweet.DATE_FORMAT_DAY);
        if (!date.equals(clockInTime)) return false;
        return this.updateById(clockInEntity);
    }

    /**
     * 学生 只能删除当天打卡的
     * @param cid cid
     * @return 是否删除
     */
    @Override
    public boolean deleteClockInByCid(String cid) {
        ClockInEntity byId = this.getById(cid);
        String clockInTime = byId.getClockInTime();
        String date = DateUtilSweet.dateToString(new Date(), DateUtilSweet.DATE_FORMAT_DAY);
        if (!date.equals(clockInTime)) return false;
        return this.removeById(cid);
    }

    @Override
    public ClockInEntity getOneClockInByTime(String uid) {
        String date = DateUtilSweet.dateToString(new Date(), DateUtilSweet.DATE_FORMAT_DAY);
        return this.getOne(new QueryWrapper<ClockInEntity>().eq("clock_in_time", date).and(w -> {
            w.eq("u_id", uid);
        }));
    }
}
