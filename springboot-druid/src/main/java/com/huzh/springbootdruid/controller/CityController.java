package com.huzh.springbootdruid.controller;

import com.huzh.springbootdruid.entity.City;
import com.huzh.springbootdruid.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName CityController
 * @Description TODO
 * @Date 2019/8/30 11:13
 * @Author huzh
 * @Version 1.0
 */
@RestController
public class CityController {

    @Autowired
    private CityRepository cityRepository;


    //http://localhost:8080/saveCity?cityName=北京&cityIntroduce=中国首都
    @GetMapping(value = "saveCity")
    public String saveCity(String cityName, String cityIntroduce) {
        City city = new City(cityName, cityIntroduce);
        cityRepository.save(city);
        return "success";
    }

    //http://localhost:8080/deleteCity?cityId=2
    @GetMapping(value = "deleteCity")
    @Transactional
    public String deleteCity(String cityId) {
        cityRepository.deleteByCityId(cityId);
        return "success";
    }

    //http://localhost:8080/updateCity?cityId=3&cityName=郑州&cityIntroduce=河南省省会
    @GetMapping(value = "updateCity")
    public String updateCity(String cityId, String cityName, String cityIntroduce) {
        City city = new City(cityId, cityName, cityIntroduce);
        cityRepository.save(city);
        return "success";
    }

    //http://localhost:8080/getCityById?cityId=3
    @GetMapping(value = "getCityById")
    public City getCityById(String cityId) {
        City city = cityRepository.findByCityId(cityId);
        return city;
    }
}
