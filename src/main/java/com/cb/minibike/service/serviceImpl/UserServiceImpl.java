package com.cb.minibike.service.serviceImpl;

import com.cb.minibike.entity.User;
import com.cb.minibike.service.UserService;
import com.qcloud.sms.SmsSingleSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public static Log logger = LogFactory.getLog(UserServiceImpl.class);
    @Override
    public User getUserByOpenId(String openId) {
        //List<User> users = mongoTemplate.find(new Query(Criteria.where("id").is(openId)), User.class);\
        return mongoTemplate.findById(openId,User.class);
    }

    @Override
    public Boolean genVerifyCode(Integer nationcode, String phoneNum) {
        Integer appID = 1400223212;
        String appKey = stringRedisTemplate.opsForValue().get("appkey");
        String verifyCode = (int)((Math.random() * 9 + 1 ) * 1000)+ "";
        String msg = "感谢使用minibike，您的登录验证码是"+ verifyCode+"，请于"+2+"分钟内填写";
        try {
            SmsSingleSender ssender = new SmsSingleSender(appID, appKey);
            ssender.send(0,nationcode.toString(),phoneNum, msg,"","");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("短信发送失败");
        }
        stringRedisTemplate.opsForValue().set(phoneNum,verifyCode,120, TimeUnit.SECONDS);//设置时效2分钟
        logger.debug(phoneNum+msg);
        return true;
    }

    @Override
    public Boolean verify(String phoneNum, String code) {
        boolean flag = false;
        String verifyCodeInDB = stringRedisTemplate.opsForValue().get(phoneNum);
        if (code.equals(verifyCodeInDB)){
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean register(User user) {
        boolean flag = false;
        //验证手机号和openid非空
        if (!"".equals(user.getId()) && !"".equals(user.getPhoneNum())) {
            //身份证因为设置了非重复索引，所以多个用户插入都为null出错
            user.setIdCardNum(UUID.randomUUID().toString());
            mongoTemplate.save(user);
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean deposite(User user) {
        boolean flag = false;
        //验证手机号或则openid非空
        if (!"".equals(user.getId()) || !"".equals(user.getPhoneNum())) {
            //身份证因为设置了非重复索引，所以多个用户插入都为null出错
            user.setIdCardNum(UUID.randomUUID().toString());
            Query idMatch = new Query(Criteria.where("id").is(user.getId()));
            Update update = new Update().set("status", user.getStatus()).set("deposit", 299);
            mongoTemplate.updateFirst(idMatch, update,User.class);
            //一句话搞定!：mongoTemplate.save(user); 根据id插入数据，id存在则更新，不存在则新插入
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean identify(User user) {
        boolean flag = false;
        //验证手机号或则openid非空
        if (!"".equals(user.getId())) {
            Query idMatch = new Query(Criteria.where("id").is(user.getId()));
            Update update = new Update().set("status", user.getStatus()).set("name", user.getName()).set("idCardNum", user.getIdCardNum());
            mongoTemplate.updateFirst(idMatch, update, User.class);
            //一句话搞定!：mongoTemplate.save(user); 根据id插入数据，id存在则更新，不存在则新插入
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean recharge(User user) {
        boolean flag = false;
        //验证手机号或则openid非空
        if (!"".equals(user.getId())) {
            Query idMatch = new Query(Criteria.where("id").is(user.getId()));
            Update update = new Update().inc("balance",user.getBalance());
            mongoTemplate.updateFirst(idMatch, update, User.class);
            //一句话搞定!：mongoTemplate.save(user); 根据id插入数据，id存在则更新，不存在则新插入
            flag = true;
        }
        return flag;
    }
}
