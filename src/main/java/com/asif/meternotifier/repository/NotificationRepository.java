package com.asif.meternotifier.repository;

import com.asif.meternotifier.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "select * from notification where email_to_send_notification = ?1", nativeQuery = true)
    Optional<Notification> findByEmail(String email);
}
