package marc.dev.dashoard.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.dashoard.domain.ApiAuthentication;
import marc.dev.dashoard.domain.UserPrincipal;
import marc.dev.dashoard.exception.ApiException;
import marc.dev.dashoard.service.UserService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

import static marc.dev.dashoard.domain.ApiAuthentication.authenticated;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        var apiAuthentication = authenticationFunction.apply(authentication);
        var user = userService.getUserByEmail(apiAuthentication.getEmail());
        log.info("this email from ApiAuth {}:",user.getEmail());

        if(user != null) {

            var userCredential = userService.getUserCredentialById(user.getId());
            //if(userCredential.getUpdatedAt().minusDays(NINETY_DAYS).isAfter(now())) { throw new ApiException("Credentials are expired. Please reset your password"); }
            //if(!user.isCredentialsNonExpired()) { throw new ApiException("Credentials are expired. Please reset your password"); }
            var userPrincipal = new UserPrincipal(user, userCredential);
            validAccount.accept(userPrincipal);
            if(encoder.matches(apiAuthentication.getPassword(), userCredential.getPassword())) {
                return authenticated(user, userPrincipal.getAuthorities());
            } else throw new BadCredentialsException("Email and/or password incorrect. Please try again");
        } throw new ApiException("Unable to authenticate");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if(!userPrincipal.isAccountNonLocked()) { throw new LockedException("Your account is currently locked"); }
        if(!userPrincipal.isEnabled()) { throw new DisabledException("Your account is currently disabled"); }
        if(!userPrincipal.isCredentialsNonExpired()) { throw new CredentialsExpiredException("Your password has expired. Please update your password"); }
        if(!userPrincipal.isAccountNonExpired()) { throw new DisabledException("Your account has expired. Please contact administrator"); }
    };
}