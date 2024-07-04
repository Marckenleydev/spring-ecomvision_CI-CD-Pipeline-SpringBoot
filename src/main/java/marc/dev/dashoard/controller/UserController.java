package marc.dev.dashoard.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import marc.dev.dashoard.domain.Response;
import marc.dev.dashoard.dto.User;
import marc.dev.dashoard.dto.UserPerformanceDto;
import marc.dev.dashoard.dtoRequest.EmailRequest;
import marc.dev.dashoard.dtoRequest.ResetPasswordRequest;
import marc.dev.dashoard.dtoRequest.UserRequest;
import marc.dev.dashoard.entity.CredentialEntity;
import marc.dev.dashoard.entity.UserEntity;
import marc.dev.dashoard.handle.ApiLogoutHandler;
import marc.dev.dashoard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static marc.dev.dashoard.utils.RequestUtils.getResponse;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApiLogoutHandler apiLogoutHandler;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(user.getName(), user.getEmail(), user.getPassword());
        return ResponseEntity.created(getUri()).body(getResponse(request, emptyMap(), "Account created. Check your email to enable your account.", CREATED));
    }
    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        apiLogoutHandler.logout(request, response, authentication);

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "You've logged out successfully ", OK));
    }
    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("key") String key, HttpServletRequest request) throws InterruptedException {

        userService.verifyAccount(key);
        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Account verified.", OK));
    }
    @GetMapping("/{userId}/credentials")
    public CredentialEntity getUserCredentialById(@PathVariable("userId") String userId) {
        return userService.getUserCredentialById(userId);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('user:read') or hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> profile(@AuthenticationPrincipal User userPrincipal, HttpServletRequest request) {
        var user = userService.getUserByUserId(userPrincipal.getId());
        return ResponseEntity.ok().body(getResponse(request,  of("user", user), "Profile retrieve.", OK));
    }
    // Start - reset password when user is NOT logged in
    @PostMapping("/resetpassword")
    public ResponseEntity<Response> resetPassword(@RequestBody @Valid EmailRequest emailRequest, HttpServletRequest request) {
        userService.resetPassword(emailRequest.getEmail());

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "We sent you an email to reset your Password. ", OK));
    }

    @GetMapping("/verify/password")
    public ResponseEntity<Response> verifyPassword(@RequestParam("key") String key, HttpServletRequest request) {
        var user = userService.verifyPasswordKey(key);

        return ResponseEntity.ok().body(getResponse(request, of("user", user), "Enter new Password. ", OK));
    }
    @PostMapping("/resetpassword/reset")
    public ResponseEntity<Response> doResetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
        userService.updatePassword(resetPasswordRequest.getId(), resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmNewPassword());

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Password reset successfully ", OK));
    }
    // End - reset password when user is not logged in
    @GetMapping
    public ResponseEntity<List<UserEntity> > getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size) {
        return new ResponseEntity<>(userService.getUsers(), OK) ;
    }
    @GetMapping("/admins")
    public ResponseEntity<List<UserEntity> > getAllUsersAdmin(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size) {
        return new ResponseEntity<>(userService.getUsersAdmin(), OK) ;
    }
    @GetMapping("/search")
    public Page<UserEntity> getUsersByName(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "20") int size , @RequestParam String name) {
        return userService.getUsers(page, size, name);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId){
        return new ResponseEntity<>(userService.getUser(userId), OK);
    }

    @GetMapping("/geography")
    public ResponseEntity<List<Map<String, Object>>> getGeography() {
        try {
            List<Map<String, Object>> formattedLocations = userService.getUsersGeography();
            return ResponseEntity.ok(formattedLocations);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(List.of(Map.of("message", e.getMessage())));
        }
    }

    @GetMapping("/user-performance/{userId}")
    public UserPerformanceDto getUserPerformance(@PathVariable String userId) {
        return userService.getUserPerformance(userId);
    }

    protected URI getUri() {
        return URI.create("");
    }
}
