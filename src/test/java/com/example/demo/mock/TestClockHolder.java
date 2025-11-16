package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final long time;

    @Override
    public long mills() {
        return time;
    }
}
