package jpabook.jpashop.domain.Item;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;

    private String etc;
}
