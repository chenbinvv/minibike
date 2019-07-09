package com.cb.minibike.service.serviceImpl;

import com.cb.minibike.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void saveLog(String log) {
        mongoTemplate.save(log,"log");
    }
}
