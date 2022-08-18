package com.tokenbid.services;

import com.tokenbid.models.User;
import com.tokenbid.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.mockito.Mockito.verify;

//don't have test for buildRegistrationEmail()
class UserServiceTest {
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private EmailService emailService = Mockito.mock(EmailService.class);
    private UserService userService;

    @BeforeEach
    public void initBefore() {
        userService = new UserService(userRepository,emailService);
    }
    private User createUser() {
        User user = new User();
        user.setUserId(1);
        user.setFirstName("Lu");
        user.setLastName("Chen");
        user.setUsername("lulu");
        user.setPassword("password");
        user.setEmailVerified(false);
        return user;
    }

    @Test
    public void shouldUpdateUser() {
        User user = createUser();
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        userService.update(user);
        verify(userRepository).save(user);
    }

    @Test
    public void shouldDeleteUser() {
        User user = createUser();
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        userService.delete(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    public void shouldGetUserById() {
        User user = createUser();
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        User actual = userService.getById(1);
        Assertions.assertEquals(1, actual.getUserId());
        Assertions.assertEquals("Lu", actual.getFirstName());
    }

    @Test
    public void shouldGetUserByIdNull() {
        Optional<User> user = Optional.empty();
        Mockito.when(userRepository.findById(1)).thenReturn(user);
        User actual = userService.getById(1);
        Assertions.assertEquals(null, actual);
    }

    @Test
    public void shouldGetAllUser() {
        userService.getAll();
        verify(userRepository).findAll();
    }

    @Test
    public void shouldVerifyEmailAndAddToken() {
        User user = createUser();
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Boolean actual = userService.verifyUserAndAddFreeTokens(1);
        verify(userRepository).save(user);
        Assertions.assertEquals(true,actual);
    }

    @Test
    public void shouldVerifyEmailAndAddTokenFalse() {
        User user = createUser();
        user.setEmailVerified(true);
        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Boolean actual = userService.verifyUserAndAddFreeTokens(1);
        Assertions.assertEquals(false,actual);
    }

    @Test
    public void shouldGetUserByUsername() {
        User user = createUser();
        Mockito.when(userRepository.findByUsername("lulu")).thenReturn(user);
        User actual = userService.getByUsername("lulu");
        Assertions.assertEquals("lulu", actual.getUsername());
        Assertions.assertEquals("Lu", actual.getFirstName());
    }

    @Test
    public void shouldGetUserByUsernameNull() {
        Mockito.when(userRepository.findByUsername("lulu")).thenReturn(null);
        User actual = userService.getByUsername("lulu");
        Assertions.assertEquals(null, actual);
    }
}