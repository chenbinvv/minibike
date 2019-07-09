package com.cb.minibike.controller;

import com.cb.minibike.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    @Autowired
    LogService logService;

    @PostMapping(value = "/log/ready")
    public String saveLog(@RequestBody String string){
        logService.saveLog(string);
        return "success";
    }
}
