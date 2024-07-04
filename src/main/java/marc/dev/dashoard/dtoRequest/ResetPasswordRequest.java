package marc.dev.dashoard.dtoRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {
    @NotEmpty(message = "User ID cannot be empty or null")
    private String id;
    @NotEmpty(message = "New Password cannot be empty or null")
    private String newPassword;
    @NotEmpty(message = "Confirm Password cannot be empty or null")
    private String confirmNewPassword;
}
