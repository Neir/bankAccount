package com.carbonit.time;

import java.util.Date;

public class Clock implements IClock {
    public Date now() {
        return new Date();
    }
}
