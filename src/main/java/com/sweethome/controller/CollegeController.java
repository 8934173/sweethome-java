package com.sweethome.controller;

import com.sweethome.entities.CollegeEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.CollegeService;
import com.sweethome.utils.R;
import com.sweethome.valid.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/college")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @PostMapping("/save")
    public R saveCollegeOne(@Validated({AddGroup.class}) @RequestBody CollegeEntity college) {
        boolean b = collegeService.saveCollegeOne(college);
        return b ? R.ok() : R.error(BizCodeEnum.SAVE_EXCEPTION.getCode(), BizCodeEnum.SAVE_EXCEPTION.getMsg());
    }

    @GetMapping("/getCollegeList")
    public R getCollegeList() {
        List<CollegeEntity> collegeList = collegeService.getCollegeList();
        return R.ok().put("data", collegeList);
    }

    @PostMapping("/delete")
    public R deleteCollegeById(@RequestParam("coId") Long coId) {
        try {
            boolean b = collegeService.deleteCollegeById(coId);
            return b ? R.ok():R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败！");
        } catch (RuntimeException e) {
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败！");
        }
    }
}
