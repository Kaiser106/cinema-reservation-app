package com.cinema.application.auth;

import com.cinema.application.auth.commands.RegisterUserCommand;
import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.port.CustomerPort;
import com.cinema.application.port.UserPort;
import com.cinema.domain.customer.Customer;
import com.cinema.domain.user.User;
import com.cinema.domain.user.UserRole;
import com.cinema.infrastructure.auth.PasswordEncoderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegisterUserCommandHandler implements CommandHandler<RegisterUserCommand, String> {
    private final UserPort userPort;
    private final CustomerPort customerPort;
    private final PasswordEncoderService passwordEncoderService;

    public RegisterUserCommandHandler(UserPort userPort, CustomerPort customerPort, PasswordEncoderService passwordEncoderService) {
        this.userPort = userPort;
        this.customerPort = customerPort;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    @Transactional
    public String handle(RegisterUserCommand command) {
        if (userPort.findByEmail(command.email()).isPresent()) {
            throw new BusinessException(ErrorCodes.USER_ALREADY_EXISTS, "error.user.exists");
        }
        User user = new User(UUID.randomUUID(), command.email(), passwordEncoderService.encode(command.password()), UserRole.CUSTOMER, Instant.now(), true);
        userPort.save(user);

        // User + Customer are created in one transaction to guarantee consistency.
        Customer customer = new Customer(UUID.randomUUID(), user.getId(), command.fullName(), command.phone(), Instant.now());
        customerPort.save(customer);
        return user.getId().toString();
    }
}
