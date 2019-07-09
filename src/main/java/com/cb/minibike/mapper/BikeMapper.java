package com.cb.minibike.mapper;

import com.cb.minibike.entity.Bike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BikeMapper {
    public void saveBike(Bike bike);
}
