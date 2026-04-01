package com.app.dependancy.qualifier;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Outback implements Restaurant {
    @Override
    public boolean isSaladBarAvailable() {
        return true;
    }

    private int newStakePrice = Restaurant.newStakePrice;


    @Override
    public int stakePrice() {
        return 100000;
    }
}
