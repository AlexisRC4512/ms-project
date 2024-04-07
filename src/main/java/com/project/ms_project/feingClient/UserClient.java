package com.project.ms_project.feingClient;

import com.project.ms_project.aggregates.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "MS-SECURITY")
public interface UserClient {
    @GetMapping("/api/v1/user/get/{id}")
    UserResponse getUser(@PathVariable("id") Long id);
    @PostMapping("/api/v1/autenticacion/validateToken")
    boolean validateApiToken(@RequestParam String token);

    @GetMapping("/api/v1/user/getForEmail")
    UserResponse getUserForEmail(@RequestParam String email);
}
