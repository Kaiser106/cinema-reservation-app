package com.cinema.application.features.auth.commands.register;

import com.cinema.application.cqrs.CommandHandler;
import com.cinema.application.features.auth.ports.PasswordHasherPort;
import com.cinema.application.shared.exception.BusinessException;
import com.cinema.application.shared.exception.ErrorCodes;
import com.cinema.domain.customer.model.Customer;
import com.cinema.domain.customer.repository.CustomerRepository;
import com.cinema.domain.user.model.User;
import com.cinema.domain.user.model.UserRole;
import com.cinema.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegisterUserCommandHandler implements CommandHandler<RegisterUserCommand, String> {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordHasherPort passwordEncoderService;

    public RegisterUserCommandHandler(UserRepository userRepository, CustomerRepository customerRepository, PasswordHasherPort passwordEncoderService) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    @Transactional
    public String handle(RegisterUserCommand command) {
        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new BusinessException(ErrorCodes.USER_ALREADY_EXISTS, "error.user.exists");
        }
        User user = new User(UUID.randomUUID(), command.email(), passwordEncoderService.encode(command.password()), UserRole.CUSTOMER, Instant.now(), true);
        userRepository.save(user);

        // User + Customer are created in one transaction to guarantee consistency.
        Customer customer = new Customer(UUID.randomUUID(), user.getId(), command.fullName(), command.phone(), Instant.now());
        customerRepository.save(customer);
        return user.getId().toString();
    }
}
