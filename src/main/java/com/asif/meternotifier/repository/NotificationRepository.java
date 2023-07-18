package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
