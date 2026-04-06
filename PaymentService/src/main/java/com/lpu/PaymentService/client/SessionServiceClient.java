package com.lpu.PaymentService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "sessionservice", path = "/sessions")
public interface SessionServiceClient {

    @PutMapping("/{id}/{status}")
    public Object updateStatus(@PathVariable("id") Long id, @PathVariable("status") String status);

}
