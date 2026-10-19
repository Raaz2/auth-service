
package com.auth.authservice.service.impl;

import com.auth.authservice.dto.UserDto;
import com.auth.authservice.model.Provider;
import com.auth.authservice.model.Role;
import com.auth.authservice.model.User;
import com.auth.authservice.repository.RoleRepository;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "ROLE_GUEST";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        String email = (userDto.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "User with email already exists: " + email
            );
        }

        User user = modelMapper.map(userDto, User.class);

        user.setEmail(email);
        user.setProvider(
                userDto.getProvider() != null
                        ? userDto.getProvider()
                        : Provider.LOCAL
        );

        // Assign the default role.
        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException(
                        "Default role not found: " + DEFAULT_ROLE
                ));

        user.getRoles().add(defaultRole);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) throws Exception {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception(
                        "User not found with email: " + email
                ));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, String userId) throws Exception {
        UUID id = UUID.fromString(userId);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "User not found with ID: " + userId
                ));

        if (userDto == null) {
            throw new IllegalArgumentException("User data is required");
        }

        // Check email uniqueness only when the email is being changed.
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            String newEmail = userDto.getEmail();

            if (!newEmail.equalsIgnoreCase(existingUser.getEmail())
                    && userRepository.existsByEmail(newEmail)) {
                throw new IllegalArgumentException(
                        "User with email already exists: " + newEmail
                );
            }

            existingUser.setEmail(newEmail);
        }

        /*
         * ModelMapper updates the existing entity instead of creating
         * a new entity. Configure it to skip null properties so partial
         * updates do not erase existing values.
         *
         * Email, provider, roles and ID are handled separately.
         */
        modelMapper.map(userDto, existingUser);

        // Restore fields that must not be overwritten by the DTO.
        existingUser.setId(id);

        // Prevent an omitted provider from resetting the current provider.
        if (userDto.getProvider() == null) {
            // Keep the provider already present on the entity.
            // See the ModelMapper configuration below.
        }

        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) throws Exception {
        UUID id = UUID.fromString(userId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "User not found with ID: " + userId
                ));

        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) throws Exception {
        UUID id = parseUserId(userId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "User not found with ID: " + userId
                ));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
