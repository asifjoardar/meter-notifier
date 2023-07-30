package com.asif.meternotifier.service.impl;

import com.asif.meternotifier.entity.MeterAccountDetails;
import com.asif.meternotifier.entity.Notification;
import com.asif.meternotifier.exception.MeterNotFoundException;
import com.asif.meternotifier.repository.NotificationRepository;
import com.asif.meternotifier.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification findByEmail(String email) {
        Optional<Notification> notification = notificationRepository.findByEmail(email);
        notification.orElseThrow(() -> new MeterNotFoundException("notification service is not found."));
        return notification.get();
    }

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }
}
