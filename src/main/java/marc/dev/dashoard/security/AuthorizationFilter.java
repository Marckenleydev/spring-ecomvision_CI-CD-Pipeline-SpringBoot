package marc.dev.dashoard.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.dashoard.domain.TokenData;
import marc.dev.dashoard.domain.Token;
import marc.dev.dashoard.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import static marc.dev.dashoard.domain.ApiAuthentication.authenticated;
import java.io.IOException;

import static java.util.Arrays.asList;
import static marc.dev.dashoard.constant.Constants.PUBLIC_ROUTES;
import static marc.dev.dashoard.enumeration.TokenType.ACCESS;
import static marc.dev.dashoard.enumeration.TokenType.REFRESH;
import static marc.dev.dashoard.utils.RequestUtils.handleErrorResponse;
import static org.springframework.http.HttpMethod.OPTIONS;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var accessToken = jwtService.extractToken(request, ACCESS.getValue());
            if(accessToken.isPresent() && jwtService.getTokenData(accessToken.get(), TokenData::isValid)){
                SecurityContextHolder.getContext().setAuthentication(getAuthentication(accessToken.get(), request));

            }else {
                var refreshToken = jwtService.extractToken(request, REFRESH.getValue());
                if(refreshToken.isPresent() && jwtService.getTokenData(refreshToken.get(), TokenData::isValid)){
                    var user = jwtService.getTokenData(refreshToken.get(), TokenData::getUser);
                    SecurityContextHolder.getContext().setAuthentication(getAuthentication(jwtService.createToken(user, Token::getAccess),request));
                    jwtService.addCookie(response, user, ACCESS);

//                    log.info(String.valueOf(user.getId()));
                }else {
                    SecurityContextHolder.clearContext();
                }
            }
            filterChain.doFilter(request,response);

        } catch (Exception exception) {
            log.error(exception.getMessage());
            handleErrorResponse(request, response, exception);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //var shouldNotFilter = request.getMethod().equalsIgnoreCase(OPTIONS.name()) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());

        return request.getMethod().equalsIgnoreCase(OPTIONS.name()) || asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }

    private Authentication getAuthentication(String token, HttpServletRequest request) {
        var authentication = authenticated(jwtService.getTokenData(token, TokenData::getUser), jwtService.getTokenData(token, TokenData::getAuthorities));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }
}
