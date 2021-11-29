package com.sweethome.controller;

import com.sweethome.entities.MajorEntity;
import com.sweethome.exception.BizCodeEnum;
import com.sweethome.service.MajorService;
import com.sweethome.utils.R;
import com.sweethome.valid.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/major")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @PostMapping("/save")
    public R saveMajor(@Validated({AddGroup.class}) @RequestBody MajorEntity major) {
        majorService.saveMajor(major);
        return R.ok();
    }

    @PostMapping("/delete")
    public R deleteMajorById(@RequestParam("maId") Long maId){
        try {
            boolean b = majorService.deleteMajorById(maId);
            return b ? R.ok():R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), "删除失败!");
        }
    }
}
