package marc.dev.dashoard.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "products")
@Setter
@Getter
public class ProductEntity {
    @Id
    private String id;
    @Field("name")
    private String name;
    @Field("price")
    private double price;
    @Field("description")
    private String description;
    @Field("category")
    private String category;
    @Field("rating")
    private double rating;
    @Field("supply")
    private int supply;
    @Field("createdAt")
    private LocalDateTime createdAt;
    @Field("updatedAt")
    private LocalDateTime updatedAt;

    @Transient
    private List<ProductStatEntity> productStats;
}
