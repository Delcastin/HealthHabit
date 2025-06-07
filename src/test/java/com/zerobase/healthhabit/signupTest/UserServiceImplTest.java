package com.zerobase.healthhabit.signupTest;


import com.zerobase.healthhabit.dto.SignUpRequest;
import com.zerobase.healthhabit.dto.UserResponse;
import com.zerobase.healthhabit.entity.ExerciseType;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.entity.UserRole;
import com.zerobase.healthhabit.exception.UserInfoDoesNotMatchException;
import com.zerobase.healthhabit.repository.UserRepository;
import com.zerobase.healthhabit.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    // 사용자 회원가입 기능 Test

    @Test
    void signUp_WhenValidRequest_SavesEncodedUser() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("Delcastin@gmail.com")
                .username("tester")
                .password("plainword")
                .bankName("국민은행")
                .accountHolder("원길동")
                .accountNumber("1234567890")
                .preferExercise(ExerciseType.CARDIO)
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded Password");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        userService.signUp(request);

        // then
        verify(userRepository).existsByEmail(request.getEmail());
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("Delcastin@gmail.com", savedUser.getEmail());
        assertEquals("tester", savedUser.getUsername());
        assertEquals("encoded Password", savedUser.getPassword());
        assertEquals("국민은행", savedUser.getBankName());
        assertEquals("원길동", savedUser.getAccountHolder());
        assertEquals("1234567890", savedUser.getAccountNumber());
        assertEquals(ExerciseType.CARDIO, savedUser.getPreferExercise());
    }

    @Test
    void signUp_WhenEmailAlreadyExists_ThrowsException() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("Delcastin@gmail.com")
                .username("tester")
                .password("plainword")
                .bankName("국민은행")
                .accountHolder("원길동")
                .accountNumber("1234567890")
                .preferExercise(ExerciseType.CARDIO)
                .build();

        // 중복 이메일 존재 설정
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.signUp(request)
        );

        assertEquals("해당 이메일 사용시 중복됩니다.", exception.getMessage());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class)); // 저장이 호출되지 않아야 함
    }

    // 관리자 승인 기능 Test

    @Test
    void approveUser_WhenValidRequest_SuccessfullyChangedRoleToAdmin() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("tester@gmail.com")
                .username("tester")
                .password("plainword")
                .role(UserRole.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when

        User approvedUser = userService.approveUser(userId);
        assertEquals(UserRole.ADMIN, approvedUser.getRole());
        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @Test
    void approveUser_ThrowsException_WhenUserNotFound(){
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.approveUser(userId));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    // 사용자 마이페이지 조회 기능
    @Test
    void getMyPage_whenUserExists_thenSuccess(){
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@zerobase.com")
                .preferExercise(ExerciseType.CARDIO)
                .role(UserRole.USER)
                .username("test")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        UserResponse result = UserResponse.from(userService.getUserById(userId));

        assertThat(result.getEmail()).isEqualTo("test@zerobase.com");
        assertThat(result.getUserName()).isEqualTo("test");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
        assertThat(result.getPreferExercise()).isEqualTo(ExerciseType.CARDIO);

    }

    @Test
    void getMyPage_whenUserInfoDoesNotMatch_thenThrowsException(){
        // given
        Long userId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@zerobase.com")
                .preferExercise(ExerciseType.STRENGTH)
                .role(UserRole.USER)
                .username("test")
                .build();

        UserResponse fakeUser = UserResponse.builder()
                .id(userId)
                .email("test@zerobase.com")
                .preferExercise(ExerciseType.STRENGTH)
                .role(UserRole.USER)
                .userName("Fake User")
                .build();
        // when

        // then
        assertThrows(UserInfoDoesNotMatchException.class, () ->
                validateUserInfo(user, fakeUser));

    }

    private void validateUserInfo(User user, UserResponse fakeUser) {
        if (!Objects.equals(user.getId(), fakeUser.getId()) ||
                !Objects.equals(user.getEmail(), fakeUser.getEmail()) ||
                !Objects.equals(user.getUsername(), fakeUser.getUserName()) ||
                !Objects.equals(user.getRole(), fakeUser.getRole()) ||
                !Objects.equals(user.getPreferExercise(), fakeUser.getPreferExercise())) {
            throw new UserInfoDoesNotMatchException();
        }
    }


}
