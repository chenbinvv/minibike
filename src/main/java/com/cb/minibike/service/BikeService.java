package com.cb.minibike.service;

import com.cb.minibike.entity.Bike;

import java.util.List;

public interface BikeService {
    public void saveBike (Bike bike);

    public List<Bike> findAllBike ();

    Bike unlockBike(Bike bike);
}
