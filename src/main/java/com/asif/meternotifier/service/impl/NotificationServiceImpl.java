package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.repository.NotificationRepository;
import com.asif.meternotifier.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }
    @Override
    public void save(Notification notification){
        notificationRepository.save(notification);
    }
}
