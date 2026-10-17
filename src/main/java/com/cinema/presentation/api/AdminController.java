package com.cinema.presentation.api;

import com.cinema.presentation.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/health")
    public ApiResponse<String> health(HttpServletRequest req) {
        return ApiResponse.ok("admin-ok", req.getRequestURI());
    }
}
