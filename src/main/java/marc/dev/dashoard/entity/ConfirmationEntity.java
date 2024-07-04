package marc.dev.dashoard.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@Document(collection = "confirmations")
@Setter
@Getter
@JsonInclude(NON_DEFAULT)
public class ConfirmationEntity {
    @Id
    private String id;
    private String token;
    @DBRef
    private UserEntity userEntity;

    public ConfirmationEntity( UserEntity userEntity) {
        this.token = UUID.randomUUID().toString();
        this.userEntity = userEntity;

    }
}
