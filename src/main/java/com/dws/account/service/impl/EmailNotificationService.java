package com.dws.account.service.impl;

import com.dws.account.service.NotificationService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class EmailNotificationService  implements NotificationService {
    @Override
    public void sendNotification(Long accountId, String transferDescription) {
        log.info("Sending email notification to account {} with description {}", accountId, transferDescription);
    }
}
