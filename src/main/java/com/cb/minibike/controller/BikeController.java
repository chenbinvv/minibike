package com.cb.minibike.controller;

import com.cb.minibike.entity.Bike;
import com.cb.minibike.service.BikeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bike")
public class BikeController {

    @Autowired
    BikeService bikeService;
    public static Log logger = LogFactory.getLog(BikeController.class);

    @ResponseBody
    @PostMapping(value = "/bikeStore")
    public String testRegist ( @RequestBody Bike bike){
        System.out.println(bike);
        bikeService.saveBike(bike);
        return "success";
    }
    @ResponseBody
    @GetMapping(value = "/bikes")
    public List<Bike> findAllBike (){
        return bikeService.findAllBike();
    }

    @ResponseBody
    @PutMapping("/unlock")
    public Bike unlockBike (@RequestBody Bike bike){
        logger.debug("解锁的车辆传入信息为："+ bike);
        bike =  bikeService.unlockBike(bike);
        logger.debug("解锁的车辆输出信息为："+ bike);
        return bike;
    }

    @RequestMapping(value = "/bike_list")
    public String toList() {
        return "bike/list";
    }

        /*@GetMapping(value = "/bike")
    public String regist ( Bike bike){
        System.out.println(bike);
        bikeService.saveBike(bike);
        return "success";
    }*/
}
