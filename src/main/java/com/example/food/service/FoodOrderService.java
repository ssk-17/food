package com.example.food.service;

import com.example.food.dto.FoodOrderRequest;
import com.example.food.dto.FoodOrderTimeResponse;
import com.example.food.dto.ItemRequest;
import com.example.food.exceptions.OutsideHoursException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
public class FoodOrderService {

    @Value("${food.tacos.time}")
    private int tacosTime;

    @Value("${food.burritos.time}")
    private int burritosTime;

    @Value("${food.nachos.time}")
    private int nachosTime;

    @Value("${food.drinks.time}")
    private int drinksTime;

    public FoodOrderTimeResponse fetchOderTime(FoodOrderRequest foodOrderRequest) {

        int totalTime = 0;
        //iterate through every item in request
        for (ItemRequest itemRequest : foodOrderRequest.getItems()) {
            int quantity = itemRequest.getQuantity();
            //depending on item, calculate the order time.
            switch (itemRequest.getItem()) {
                case TACOS:
                    int units = (int) Math.ceil((double) quantity / 3);
                    //in case if the request came in out of working hours, throw customized exception
                    if (units > 0 && checkForOutsideHours(foodOrderRequest.getTime()))
                        throw new OutsideHoursException("The taco trucks are only open from 11AM - 6PM local time, Monday -\n" +
                                "Saturday.");
                    totalTime += units * tacosTime;
                    break;
                case NACHOS:
                    totalTime += quantity * nachosTime;
                    break;
                case BURRITOS:
                    totalTime += quantity * burritosTime;
                    break;
                case DRINKS:
                    totalTime += quantity * drinksTime;
                    break;
            }
        }
        totalTime = roundOrderTime(totalTime);
        return new FoodOrderTimeResponse(totalTime);
    }

    private int roundOrderTime(int orderTime) {
        //get the next increment
        return orderTime % 10 == 0 ? orderTime : orderTime + (10 - (orderTime % 10));
    }

    private boolean checkForOutsideHours(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                localDateTime.getHour() < 11 || localDateTime.getHour() > 18;
    }
}
