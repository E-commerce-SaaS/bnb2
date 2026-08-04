package io.units.entity;

import java.math.BigDecimal;

import io.lib.entity.BaseJpaEntity;
import io.orgbranch.entity.OrgBranch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class  UnitsEntity extends BaseJpaEntity {
    @Column(unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private UnitCategory category;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private OrgBranch branch;

    private String floor;

    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
}
