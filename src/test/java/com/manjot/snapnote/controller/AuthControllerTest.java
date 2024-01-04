package com.manjot.snapnote.controller;

import com.manjot.snapnote.dto.authentication.request.LoginRequest;
import com.manjot.snapnote.dto.authentication.request.SignupRequest;
import com.manjot.snapnote.dto.authentication.response.JwtResponse;
import com.manjot.snapnote.dto.authentication.response.MessageResponse;
import com.manjot.snapnote.model.Role;
import com.manjot.snapnote.model.User;
import com.manjot.snapnote.model.enums.ERole;
import com.manjot.snapnote.repository.RoleRepository;
import com.manjot.snapnote.repository.UserRepository;
import com.manjot.snapnote.security.jwt.JwtUtils;
import com.manjot.snapnote.security.services.UserDetailsImpl;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Validator validator;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticateUser_Success() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("test")
                .password("password")
                .build();
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl("1", "username", "email", "password", Collections.emptyList());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");

        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(JwtResponse.class, responseEntity.getBody().getClass());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
    }

    @Test
    void registerUser_Success() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("username")
                .email("test@test.com")
                .password("password")
                        .build();
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(encoder.encode("password")).thenReturn("encodedPassword");

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MessageResponse.class, responseEntity.getBody().getClass());
        verify(userRepository, times(1)).existsByUsername("username");
        verify(userRepository, times(1)).existsByEmail("test@test.com");
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(encoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerAdminUser_Success() {
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        SignupRequest signupRequest = SignupRequest.builder()
                .username("username")
                .email("test@test.com")
                .password("password")
                .role(roles)
                .build();
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(new Role(ERole.ROLE_ADMIN)));
        when(encoder.encode("password")).thenReturn("encodedPassword");

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MessageResponse.class, responseEntity.getBody().getClass());
        verify(userRepository, times(1)).existsByUsername("username");
        verify(userRepository, times(1)).existsByEmail("test@test.com");
        verify(roleRepository, times(1)).findByName(ERole.ROLE_ADMIN);
        verify(encoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerModUser_Success() {
        Set<String> roles = new HashSet<>();
        roles.add("mod");
        SignupRequest signupRequest = SignupRequest.builder()
                .username("username")
                .email("test@test.com")
                .password("password")
                .role(roles)
                .build();
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_MODERATOR)).thenReturn(Optional.of(new Role(ERole.ROLE_MODERATOR)));
        when(encoder.encode("password")).thenReturn("encodedPassword");

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MessageResponse.class, responseEntity.getBody().getClass());
        verify(userRepository, times(1)).existsByUsername("username");
        verify(userRepository, times(1)).existsByEmail("test@test.com");
        verify(roleRepository, times(1)).findByName(ERole.ROLE_MODERATOR);
        verify(encoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerDefaultUser_Success() {
        Set<String> roles = new HashSet<>();
        roles.add("default");
        SignupRequest signupRequest = SignupRequest.builder()
                .username("username")
                .email("test@test.com")
                .password("password")
                .role(roles)
                .build();
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("email")).thenReturn(false);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(encoder.encode("password")).thenReturn("encodedPassword");

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MessageResponse.class, responseEntity.getBody().getClass());
        verify(userRepository, times(1)).existsByUsername("username");
        verify(userRepository, times(1)).existsByEmail("test@test.com");
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(encoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_FailureUsernameTaken() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("username")
                .email("test@test.com")
                .password("password")
                .build();
        when(userRepository.existsByUsername("username")).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MessageResponse.class, responseEntity.getBody().getClass());
        verify(userRepository, times(1)).existsByUsername("username");
    }

}