package marc.dev.dashoard.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "transactions")
@Setter
@Getter
public class TransactionEntity {
    @Id
    private String id;

    @Field("userId")
    private String userId;

    @Field("cost")
    private String cost;

    @DBRef
    @Field("products")
    private List<String> products = new ArrayList<>();

    @Field("createdAt")
    private Date createdAt;

    @Field("updatedAt")
    private Date updatedAt;
}
