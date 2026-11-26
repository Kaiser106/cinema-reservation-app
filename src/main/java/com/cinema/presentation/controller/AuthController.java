package com.cinema.presentation.controller;

import com.cinema.application.features.auth.commands.login.LoginUserCommand;
import com.cinema.application.features.auth.commands.login.LoginUserCommandHandler;
import com.cinema.application.features.auth.commands.logout.LogoutUserCommand;
import com.cinema.application.features.auth.commands.logout.LogoutUserCommandHandler;
import com.cinema.application.features.auth.commands.register.RegisterUserCommand;
import com.cinema.application.features.auth.commands.register.RegisterUserCommandHandler;
import com.cinema.presentation.dto.request.auth.LoginRequest;
import com.cinema.presentation.dto.request.auth.RegisterRequest;
import com.cinema.presentation.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final RegisterUserCommandHandler registerHandler;
    private final LoginUserCommandHandler loginHandler;
    private final LogoutUserCommandHandler logoutHandler;

    public AuthController(RegisterUserCommandHandler registerHandler, LoginUserCommandHandler loginHandler, LogoutUserCommandHandler logoutHandler) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
        this.logoutHandler = logoutHandler;
    }

    @PostMapping("/register")
    public ApiResponse<Map<String, String>> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest http) {
        String id = registerHandler.handle(new RegisterUserCommand(request.email(), request.password(), request.fullName(), request.phone()));
        return ApiResponse.ok(Map.of("userId", id), http.getRequestURI());
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest http) {
        String token = loginHandler.handle(new LoginUserCommand(request.email(), request.password(), http.getHeader("User-Agent"), http.getRemoteAddr()));
        return ApiResponse.ok(Map.of("sessionToken", token), http.getRequestURI());
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("X-Session-Token") String token, HttpServletRequest http) {
        logoutHandler.handle(new LogoutUserCommand(token));
        return ApiResponse.ok(null, http.getRequestURI());
    }
}
