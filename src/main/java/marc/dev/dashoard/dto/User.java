package marc.dev.dashoard.dto;

import lombok.Data;
import marc.dev.dashoard.entity.AffiliateStatEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String imageUrl;
    private String role;
    private String occupation;
    private String createdAt;
    private String updatedAt;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private boolean enabled;
    private boolean mfa;
    private List<String> transactions = new ArrayList<>();
    private List<AffiliateStatEntity> affiliateStats = new ArrayList<>();
}