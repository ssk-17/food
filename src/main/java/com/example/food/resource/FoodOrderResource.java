package com.example.food.resource;

import com.example.food.dto.FoodOrderRequest;
import com.example.food.exceptions.OutsideHoursException;
import com.example.food.service.FoodOrderService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodOrderResource {

    @Autowired
    private FoodOrderService foodOrderService;

    @PostMapping("food/readyTime")
    public ResponseEntity fetchOrderTime(@Validated @NonNull @RequestBody
                                                 FoodOrderRequest foodOrderRequest) {
        try {
            return ResponseEntity.ok(foodOrderService.fetchOderTime(foodOrderRequest));
        } catch (OutsideHoursException ex) {
            //return bad request in case of out of working hours
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
