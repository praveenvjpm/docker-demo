package in.praveen.kt.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Book {
    private String name;
    private String author;
    private BigDecimal price;
}
