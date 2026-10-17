package com.cinema.localization;

import com.cinema.application.auth.LoginUserCommandHandler;
import com.cinema.application.auth.LogoutUserCommandHandler;
import com.cinema.application.auth.RegisterUserCommandHandler;
import com.cinema.application.common.LocalizedMessageService;
import com.cinema.application.port.SessionAuthPort;
import com.cinema.infrastructure.config.MessageSourceConfig;
import com.cinema.presentation.api.AuthController;
import com.cinema.presentation.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, MessageSourceConfig.class, LocalizedMessageService.class})
class LocalizationErrorResponseTest {
    @Autowired MockMvc mockMvc;

      RegisterUserCommandHandler registerUserCommandHandler;
      LoginUserCommandHandler loginUserCommandHandler;
      LogoutUserCommandHandler logoutUserCommandHandler;
      SessionAuthPort sessionAuthPort;

    @Test
    void shouldReturnTurkishValidationMessage() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .header("Accept-Language", "tr")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(content().string(not(containsString("Validation error"))));
    }

    @Test
    void shouldReturnEnglishValidationMessage() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .header("Accept-Language", "en")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(content().string(containsString("Validation error")));
    }
}
