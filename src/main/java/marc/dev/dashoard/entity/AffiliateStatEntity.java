package marc.dev.dashoard.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "affiliatestats")
@Setter
@Getter
public class AffiliateStatEntity {
    @Id
    private String id;
    private  String username;
    @DBRef
    @Field("affiliateSales")
    private List<String> affiliateSales;  // This field contains the IDs of the transactions
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
