package com.cb.minibike.service.serviceImpl;

import com.cb.minibike.entity.Bike;
import com.cb.minibike.mapper.BikeMapper;
import com.cb.minibike.service.BikeService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BikeServiceImpl implements BikeService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    BikeMapper bikeMapper;

    @Override
    public void saveBike(Bike bike) {
        mongoTemplate.save(bike,"bike");
    }

    @Override
    public List<Bike> findAllBike() {
        return mongoTemplate.findAll(Bike.class,"bike");
    }

    @Override
    public Bike unlockBike(Bike bike) {
        //mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())),
        //        new Update().set("status", user.getStatus()).set("name", user.getName()).set("idNum", user.getIdNum()),  User.class);
        Query qrCodeMatch = new Query(Criteria.where("qrCode").is(bike.getQrCode()));//quCode符合
        //数据库中有这辆车
        if (mongoTemplate.find(qrCodeMatch, Bike.class).size() == 0) {
            Bike bikeErrorMessage = new Bike();
            bikeErrorMessage.setStatus(101);//101表示车辆不存在
            return bikeErrorMessage;
        }
            //注意！：qrCodeMatch.addCriteria返回的是同一个对象
            Query qrCodeAndstatusMatch = qrCodeMatch.addCriteria(Criteria.where("status").is(0));//status状态不是开启
        if (mongoTemplate.find(qrCodeAndstatusMatch, Bike.class).size() == 0) {
            Bike bikeErrorMessage = new Bike();
            bikeErrorMessage.setStatus(102);//102表示车辆已被使用
            return bikeErrorMessage;
        }
            Update update = new Update().set("status", bike.getStatus());
            mongoTemplate.updateFirst(qrCodeAndstatusMatch, update, Bike.class);
            List<Bike> bikes = mongoTemplate.find(new Query(Criteria.where("qrCode").is(bike.getQrCode())), Bike.class);
            return bikes.get(0);

    }


   /* @Override
    public void saveBike(Bike bike) {
        bikeMapper.saveBike(bike);
    }*/
}
