package com.app.dependancy.qualifier;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@Qualifier("vips") 퀄리파이어 값도 바꿀 수 있음
@Data
public class Vips implements Restaurant {
    @Override
    public boolean isSaladBarAvailable() {
        return false;
    }

    private int newStakePrice = Restaurant.newStakePrice + 20000;
}
