package marc.dev.dashoard.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import marc.dev.dashoard.domain.Response;
import marc.dev.dashoard.dto.User;
import marc.dev.dashoard.dtoRequest.LoginRequest;
import marc.dev.dashoard.enumeration.LoginType;
import marc.dev.dashoard.enumeration.TokenType;
import marc.dev.dashoard.service.JwtService;
import marc.dev.dashoard.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static java.util.Map.of;
import static marc.dev.dashoard.constant.Constants.LOGIN_PATH;
import static marc.dev.dashoard.enumeration.TokenType.ACCESS;
import static marc.dev.dashoard.utils.RequestUtils.getResponse;
import static marc.dev.dashoard.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static marc.dev.dashoard.domain.ApiAuthentication.unauthenticated;

@Slf4j
public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final UserService userService;
    private final JwtService jwtService;

    public ApiAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        super(new AntPathRequestMatcher(LOGIN_PATH, POST.name()), authenticationManager);
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        try {
            log.debug("Attempting authentication");
            var user = new ObjectMapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true).readValue(request.getInputStream(), LoginRequest.class);

            userService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_ATTEMPT);
            var authentication = unauthenticated(user.getEmail(), user.getPassword());

            return getAuthenticationManager().authenticate(authentication);

        } catch (Exception exception) {
            log.error("Error during attemptAuthentication: {}", exception.getMessage(), exception);
            handleErrorResponse(request, response, exception);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        var user = (User) authentication.getPrincipal();
        log.debug("Authentication successful for user: {}", user.getEmail());
        userService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);
        var httpResponse = sendResponse(request, response, user);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(OK.value());
        var out = response.getOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out, httpResponse);
        out.flush();
    }

    private Response sendResponse(HttpServletRequest request, HttpServletResponse response, User user){
        jwtService.addCookie(response, user, ACCESS);
        jwtService.addCookie(response, user, TokenType.REFRESH);
        return getResponse(request, of("user", user), "Login Success", HttpStatus.OK);
    }


}
