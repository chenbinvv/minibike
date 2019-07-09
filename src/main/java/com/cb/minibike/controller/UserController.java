package com.cb.minibike.controller;

import com.cb.minibike.entity.User;
import com.cb.minibike.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    public static Log logger = LogFactory.getLog(UserController.class);


    @GetMapping("/{openId}")
    @ResponseBody
    public User getOpenidByPhone(@PathVariable("openId") String openId){
        User userByOpenId = userService.getUserByOpenId(openId);
        logger.debug(openId+"的信息为："+userByOpenId);
        return userByOpenId;
    }

    @PostMapping("/genVerifyCode")
    @ResponseBody
    //以这个方式获取参数前端要加header: { 'content-type': 'application/x-www-form-urlencoded'}   （GET方式默认就是这种请求格式）
    public Boolean genVerifyCode(Integer nationCode, String phoneNum){
        logger.debug("获取的nationCode:"+nationCode+"，phoneNum:"+phoneNum);
        return userService.genVerifyCode(nationCode,phoneNum);
    }

    @PostMapping("/verify")
    @ResponseBody
    public Boolean verify (String phoneNum, String verifyCode){
        logger.debug("传入的phoneNum："+phoneNum+",verifyCode："+verifyCode);
        return userService.verify(phoneNum,verifyCode);
    }

    @PostMapping("/register")
    @ResponseBody
    public Boolean register (@RequestBody User user){
        logger.debug("注册手机号的用户信息为："+user);
        return userService.register(user);
    }

    @PostMapping("/deposit")
    @ResponseBody
    public Boolean deposite (@RequestBody User user){
        logger.debug("交押金的用户信息为："+user);
        return userService.deposite(user);
    }

    @PostMapping("/identify")
    @ResponseBody
    public Boolean identify (@RequestBody User user){
        logger.debug("实名认证的用户信息为："+user);
        return userService.identify(user);
    }

    @PostMapping("/recharge")
    @ResponseBody
    public Boolean recharge (@RequestBody User user){
        logger.debug("充值账户的用户信息为："+user);
        return userService.recharge(user);
    }


    @ResponseBody
    @RequestMapping(value = "/host")
    public String getHost (){
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }

        /*//请求映射，跟浏览器中的URL请求资源对应就会调用对应的发放
    @RequestMapping("/")
    public String index() {
        //前缀 + 视图名 + 后缀
        //pages/index.html
        return "index";
    }*/
}
