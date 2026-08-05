package io.room.entity;

import java.math.BigDecimal;

import io.lib.entity.BaseJpaEntity;
import io.lib.service.FormatUtil;
import io.orgbranch.entity.OrgBranch;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Room extends BaseJpaEntity {
    @ManyToOne
    private OrgBranch orgBranch;

    private String orgBranchEntityId;

    @Column(unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private RoomCategory roomCategory;

    private String floor;

    @Column(precision = FormatUtil.BIG_DECIMAL_PRECISION, scale = FormatUtil.BIG_DECIMAL_SCALE)
    private BigDecimal pricePerNight;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.AVAILABLE;

    public void setOrgBranch(OrgBranch orgBranch) {
        this.orgBranch = orgBranch;
        if(this.orgBranch != null){
            this.orgBranchEntityId = this.orgBranch.getEntityId();
        }
    }
}
