package com.sweethome.controller;

import com.sweethome.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/third")
public class ThirdPartyController {
    @Resource
    private RestTemplate restTemplate;

    @Value("${sina.http}")
    private String sina;

    @Value("${tencent.http}")
    private String tencent;

    @Value("${vaccines.http}")
    private String vaccines;

    @Value("${classifyByVaccine.http}")
    private String classifyByVaccine;

    @GetMapping("/epidemic")
    public R epidemic () {
        return restTemplate.getForObject(sina, R.class);
    }

    @GetMapping("/epidemicByTencent")
    public R epidemicByTencent() {
        return restTemplate.getForObject(tencent, R.class);
    }

    @GetMapping("/vaccines")
    public R vaccines() {
        return restTemplate.getForObject(vaccines, R.class);
    }

    @GetMapping("/VaccineSituationData/{modules}")
    public R classifyByVaccine(@PathVariable("modules") String modules) {
        return restTemplate.postForObject(classifyByVaccine+"?modules="+modules, null, R.class);
    }
}
