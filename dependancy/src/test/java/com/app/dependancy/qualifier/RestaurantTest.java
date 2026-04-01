package com.app.dependancy.qualifier;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class RestaurantTest {

    @Autowired @Qualifier("vips")
    private Restaurant vips;

    @Autowired @Qualifier("outback")
    private Restaurant outback;

    @Test
    public void restaurantTest(){
        log.info("vips : {}",vips);
        log.info("outback : {}",outback);
        log.info("vips salad bar available : {}",vips.isSaladBarAvailable());
        log.info("outback salad bar available : {}",outback.isSaladBarAvailable());
        log.info("vips stake price : {}",vips.stakePrice());
        log.info("outback stake price : {}",outback.stakePrice());
    }
}
