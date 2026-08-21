package io.customer.entity;

import io.lib.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer extends BaseJpaEntity {

    @Column(length = 100)
    private String name;

    @Column(unique = true, length = 20)
    private String phoneNumber;

}
