package marc.dev.dashoard.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credentials")
@JsonInclude(NON_DEFAULT)

public class CredentialEntity {
    @Id
    private String id;
    @Field("password")
    private String password;
    @DBRef
    @Field("userId")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("userId")
    private UserEntity userEntity;
    private LocalDateTime updatedAt;

    public CredentialEntity(UserEntity userEntity, String password) {
        this.userEntity = userEntity;
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }



}
