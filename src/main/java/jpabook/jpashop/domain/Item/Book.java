package jpabook.jpashop.domain.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("B")
public class Book extends Item {

    private String author;
    private String isbn;
}
