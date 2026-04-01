package com.app.dependancy.qualifier;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Laptop implements Computer {
    @Override
    public int getScreenSize() {
        return 1920;
    }
}
