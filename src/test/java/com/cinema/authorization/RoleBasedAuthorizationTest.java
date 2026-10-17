package com.cinema.authorization;

import com.cinema.application.auth.LoginUserCommandHandler;
import com.cinema.application.auth.LogoutUserCommandHandler;
import com.cinema.application.auth.RegisterUserCommandHandler;
import com.cinema.application.common.LocalizedMessageService;
import com.cinema.application.port.SessionAuthPort;
import com.cinema.infrastructure.auth.SessionAuthenticationFilter;
import com.cinema.infrastructure.config.SecurityConfig;
import com.cinema.presentation.api.AdminController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@Import({SecurityConfig.class, SessionAuthenticationFilter.class})
class RoleBasedAuthorizationTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SessionAuthPort sessionAuthPort;

    @MockitoBean
    RegisterUserCommandHandler registerUserCommandHandler;

    @MockitoBean
    LoginUserCommandHandler loginUserCommandHandler;

    @MockitoBean
    LogoutUserCommandHandler logoutUserCommandHandler;

    @MockitoBean
    LocalizedMessageService localizedMessageService;

    @Test
    void customerCannotAccessAdminOperations() throws Exception {
        when(sessionAuthPort.findValidSession("t1")).thenReturn(Optional.of(new SessionAuthPort.SessionData(UUID.randomUUID(), "CUSTOMER", Instant.now().plusSeconds(1000))));
        mockMvc.perform(get("/api/admin/health").header("X-Session-Token", "t1"))
                .andExpect(status().isForbidden());
    }
}
