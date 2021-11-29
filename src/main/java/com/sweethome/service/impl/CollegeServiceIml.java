package com.sweethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sweethome.dao.CollegeDao;
import com.sweethome.entities.CollegeEntity;
import com.sweethome.entities.MajorEntity;
import com.sweethome.service.CollegeService;
import com.sweethome.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollegeServiceIml extends ServiceImpl<CollegeDao, CollegeEntity> implements CollegeService {

    @Autowired
    private MajorService majorService;

    /**
     * 名字唯一
     * @param college CollegeEntity
     * @return boolean
     */
    @Override
    public boolean saveCollegeOne(CollegeEntity college) {
        String collegeName = college.getCollegeName();
        CollegeEntity co = this.getOne(new QueryWrapper<CollegeEntity>().eq("college_name", collegeName));
        Long coId = college.getCoId();
        if (coId!=null) {
            List<MajorEntity> majors = majorService.getMajorByCollegeId(coId);
            majors.forEach(majorEntity -> {
                majorEntity.setCollegeName(college.getCollegeName());
            });
            majorService.updateBatchById(majors);
        }
        return co == null && this.saveOrUpdate(college);
    }

    @Override
    public List<CollegeEntity> getCollegeList() {
        List<CollegeEntity> college = this.list();
        List<MajorEntity> majorAll = majorService.getMajorAll();
        if (college != null && majorAll != null) {
            return college.stream().peek((collegeEntity) -> {
                List<MajorEntity> majors = majorAll.stream()
                        .filter(majorEntity -> majorEntity.getCollegeId().equals(collegeEntity.getCoId()))
                        .collect(Collectors.toList());
                collegeEntity.setMajors(majors);
            }).collect(Collectors.toList());
        }
        return college;
    }

    @Override
    public boolean deleteCollegeById(Long coId) {
        if (coId==null) return false;
        List<MajorEntity> majors = majorService.getMajorByCollegeId(coId);
        if (majors.size()>0) return false;
        return this.removeById(coId);
    }
}
