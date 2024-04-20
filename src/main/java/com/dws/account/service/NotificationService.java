package com.dws.account.service;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void sendNotification(Long accountId,String transferDescription);
}
