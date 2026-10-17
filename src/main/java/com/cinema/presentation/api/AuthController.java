package com.cinema.presentation.api;

import com.cinema.application.auth.LoginUserCommandHandler;
import com.cinema.application.auth.LogoutUserCommandHandler;
import com.cinema.application.auth.RegisterUserCommandHandler;
import com.cinema.application.auth.commands.LoginUserCommand;
import com.cinema.application.auth.commands.LogoutUserCommand;
import com.cinema.application.auth.commands.RegisterUserCommand;
import com.cinema.presentation.request.auth.LoginRequest;
import com.cinema.presentation.request.auth.RegisterRequest;
import com.cinema.presentation.response.ApiResponse;
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
