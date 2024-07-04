package marc.dev.dashoard.utils;
import marc.dev.dashoard.dto.User;
import marc.dev.dashoard.entity.CredentialEntity;
import marc.dev.dashoard.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;
import static marc.dev.dashoard.constant.Constants.NINETY_DAYS;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UserUtils {
    public static UserEntity createUserEntity(String name, String email, String role) {
        return UserEntity.builder()
                .name(name)
                .email(email)
                .city(EMPTY)
                .role(role)
                .state(EMPTY)
                .country(EMPTY)
                .occupation(EMPTY)
                .phoneNumber(EMPTY)
                .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .transactions(new ArrayList<>())
                .affiliateStats(new ArrayList<>())
                .build();
    }

    public static User fromUserEntity(UserEntity userEntity, CredentialEntity credentialEntity){
        User user = new User();
        user.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        BeanUtils.copyProperties(userEntity, user);
        return user;

    }
    public static User fromUserEntity(UserEntity userEntity){
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        return user;

    }

    public static boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(now());
    }



}
