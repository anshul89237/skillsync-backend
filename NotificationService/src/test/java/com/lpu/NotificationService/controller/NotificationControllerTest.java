package com.lpu.NotificationService.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class NotificationControllerTest {

    @Test
    void shouldReturnRunningStatus_whenTestAccessed() {
        NotificationController controller = new NotificationController();
        assertEquals("Notification Service Running", controller.test());
    }
}
