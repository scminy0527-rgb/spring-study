package com.app.dependancy.qualifier;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ComputerTest {
    @Autowired @Qualifier("desktop")
    private Computer computer;

    @Test
    public void computerTest(){
        log.info("computer : {}",computer);
        log.info("computer screen size : {}",computer.getScreenSize());
    }
}
