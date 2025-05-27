package com.defi.auth.user.service.impl;

import com.defi.auth.group.service.UserInGroupService;
import com.defi.auth.role.service.UserHasRoleService;
import com.defi.auth.session.entity.Session;
import com.defi.auth.session.service.SessionService;
import com.defi.auth.user.dto.CreateUserRequest;
import com.defi.auth.user.dto.LockedUserDto;
import com.defi.auth.user.dto.LoginUserRequest;
import com.defi.auth.user.dto.UserResponse;
import com.defi.auth.user.entity.User;
import com.defi.auth.user.entity.UserCredential;
import com.defi.auth.user.entity.UserCredentialId;
import com.defi.auth.user.entity.UserCredentialType;
import com.defi.auth.user.mapper.UserMapper;
import com.defi.auth.user.repository.UserCredentialRepository;
import com.defi.auth.user.repository.UserRepository;
import com.defi.auth.user.service.AdminUserService;
import com.defi.auth.user.service.LoginFailureService;
import com.defi.auth.user.service.PublicUserService;
import com.defi.common.api.BaseResponse;
import com.defi.common.api.CommonMessage;
import com.defi.common.config.JwtConfig;
import com.defi.common.token.entity.TokenType;
import com.defi.common.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicUserServiceImpl implements PublicUserService {
    private final UserCredentialRepository userCredentialRepository;
    private final UserRepository userRepository;
    private final AdminUserService adminUserService;
    private final TokenService tokenService;
    private final SessionService sessionService;
    private final UserHasRoleService userHasRoleService;
    private final UserInGroupService userInGroupService;
    private final JwtConfig jwtConfig;
    private final LoginFailureService loginFailureService;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(CreateUserRequest req, String ipAddress, String userAgent) {
        User user = adminUserService.createUser(req);
        Session session = sessionService.createSession(user.getId(), ipAddress, userAgent, null);
        if(session == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, CommonMessage.LIMIT);
        }
        List<Long> roles = new LinkedList<>();
        List<Long> groups = new LinkedList<>();

        List<String> empty = new LinkedList<>();

        String accessToken = tokenService.generateToken(
                String.valueOf(session.getId()),
                TokenType.ACCESS_TOKEN,
                String.valueOf(user.getId()),
                user.getUserName(),
                empty,
                empty,
                jwtConfig.getAccessTokenTimeToLive().toSeconds()
        );
        String refreshToken = tokenService.generateToken(
                String.valueOf(session.getId()),
                TokenType.REFRESH_TOKEN,
                String.valueOf(user.getId()),
                user.getUserName(),
                empty,
                empty,
                jwtConfig.getRefreshTokenTimeToLive().toSeconds()
        );
        return mapper.toResponse(user, groups,
                roles,
                accessToken, refreshToken);
    }

    @Override
    @Transactional
    public ResponseEntity<?> login(LoginUserRequest req, String ipAddress, String userAgent) {
        // 1. Lấy user theo username
        User user = userRepository.findByUserNameOrPhoneOrEmail(req.getUserName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, CommonMessage.UNAUTHORIZED));
        long now = Instant.now().getEpochSecond();

        if (user.isLocked()) {
            if (user.getLockedUntil() < 0 || user.getLockedUntil() > now) {
                LockedUserDto lockedUserDto = LockedUserDto.builder()
                        .locked(user.isLocked())
                        .lockedUntil(user.getLockedUntil())
                        .build();
                BaseResponse<?> response = BaseResponse.of(HttpStatus.FORBIDDEN.value(),
                        CommonMessage.LOCKED, lockedUserDto);
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
        }

        // 3. Lấy credential (PASSWORD)
        UserCredential credential = userCredentialRepository.findById(
                        new UserCredentialId(user.getId(), UserCredentialType.PASSWORD.getValue()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, CommonMessage.UNAUTHORIZED));

        // 4. Kiểm tra password (salt + password, so với hash)
        String input = credential.getSalt() + req.getPassword();
        if (!passwordEncoder.matches(input, credential.getSecretData())) {
            handleLoginFailure(user);
            BaseResponse<?> response = BaseResponse.of(HttpStatus.BAD_REQUEST.value(),
                    CommonMessage.INVALID);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else{
            loginFailureService.onSuccess(user.getUserName());
        }

        // 5. Tạo session
        Session session = sessionService.createSession(user.getId(), ipAddress, userAgent, null);
        if(session == null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, CommonMessage.LIMIT);
        }

        // 6. Lấy roles và groups
        List<Long> roles = userHasRoleService.findRoleIdsByUserId(user.getId());
        List<Long> groups = userInGroupService.findGroupIdsByUserId(user.getId());

        // 7. Sinh token
        String accessToken = tokenService.generateToken(
                String.valueOf(session.getId()),
                TokenType.ACCESS_TOKEN,
                String.valueOf(user.getId()),
                user.getUserName(),
                roles.stream().map(String::valueOf).toList(),
                groups.stream().map(String::valueOf).toList(),
                jwtConfig.getAccessTokenTimeToLive().toSeconds()
        );
        String refreshToken = tokenService.generateToken(
                String.valueOf(session.getId()),
                TokenType.REFRESH_TOKEN,
                String.valueOf(user.getId()),
                user.getUserName(),
                roles.stream().map(String::valueOf).toList(),
                groups.stream().map(String::valueOf).toList(),
                jwtConfig.getRefreshTokenTimeToLive().toSeconds()
        );

        // 8. Map sang UserResponse
        BaseResponse<UserResponse> response =  BaseResponse.of(mapper.toResponse(user, roles, groups, accessToken, refreshToken));
        return ResponseEntity.ok(response);
    }

    private void handleLoginFailure(User user) {
        long time = loginFailureService.onFailure(user.getUserName());
        if(time > 0) {
            user.setLocked(true);
            user.setLockedUntil(Instant.now().getEpochSecond() + time);
            userRepository.save(user);
        }
    }
}
