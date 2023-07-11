package com.asif.meternotifier.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface NotificationScheduler {
    public void execute() throws JsonProcessingException;
}
