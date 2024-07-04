package marc.dev.dashoard.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Document(collection = "users")
@Setter
@Getter
@Builder
public class UserEntity {
    @Id
    private String id;
    @Field("name")
    @Email(message = "Name is required")
    private String name;

    @Field("email")
    @NotNull(message = "email is required")
    @Email(message = "please enter a valid email address")
    private String email;
    @Field("password")
    @Email(message = "Password is required")
    private String password;
    @Field("city")
    private String city;
    @Field("role")
    private String role;
    @Field("state")
    private String state;
    @Field("country")
    private String country;
    @Field("occupation")
    private String occupation;
    @Field("phoneNumber")
    private String phoneNumber;
    private String imageUrl;
    private Integer loginAttempts = 0;
    private LocalDateTime lastLogin;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean enabled = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    @DBRef
    @Field("transactions")
    private List<String> transactions = new ArrayList<>();
    @Field("affiliateStats")
    private List<AffiliateStatEntity> affiliateStats = new ArrayList<>();
}
