package marc.dev.dashoard.validation;


import marc.dev.dashoard.entity.UserEntity;
import marc.dev.dashoard.exception.ApiException;

public class UserValidation {
    public static void verifyAccountStatus(UserEntity userEntity) {
        if(!userEntity.isEnabled()){
            throw  new ApiException("User is disabled");
        }
        if(!userEntity.isAccountNonExpired()){ throw  new ApiException("User is expired");}
        if(!userEntity.isAccountNonLocked()){ throw  new ApiException("User is locked");}
    }
}
