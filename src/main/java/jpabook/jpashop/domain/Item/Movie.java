package jpabook.jpashop.domain.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("M")
public class Movie extends Item {

    private String director;

    private String actor;
}
