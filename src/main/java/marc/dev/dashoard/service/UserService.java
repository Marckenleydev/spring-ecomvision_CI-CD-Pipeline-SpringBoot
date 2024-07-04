package marc.dev.dashoard.service;

import marc.dev.dashoard.dto.User;
import marc.dev.dashoard.dto.UserPerformanceDto;
import marc.dev.dashoard.entity.CredentialEntity;
import marc.dev.dashoard.entity.UserEntity;
import marc.dev.dashoard.enumeration.LoginType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface UserService {
    void createUser(String name, String email, String password);
    User getUserById(String userId);
    CredentialEntity getUserCredentialById(String id);
    void verifyAccount(String token);
    void updateLoginAttempt(String email, LoginType loginType);
    User getUserByEmail(String email);
    void resetPassword(String email);
    User verifyPasswordKey(String key);
    void updatePassword(String userId, String newPassword, String confirmNewPassword);
    User getUserByUserId(String userId);
    List<UserEntity> getUsersAdmin();
    List<UserEntity> getUsers();
    List<Map<String, Object>> getUsersGeography();
    Page<UserEntity> getUsers(int page, int size, String name);
    User getUser(String id);

    UserPerformanceDto getUserPerformance(String userId);
}
