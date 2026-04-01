package com.app.dependancy.qualifier;

public interface Restaurant {
    public boolean isSaladBarAvailable();

//    상수로도 구현 가능
    public int newStakePrice = 50000;
    public default int stakePrice() {
        return 50000;
    }
}
