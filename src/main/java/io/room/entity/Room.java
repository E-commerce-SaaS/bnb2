package io.room.entity;

import java.math.BigDecimal;

import io.lib.entity.BaseJpaEntity;
import io.lib.service.FormatUtil;
import io.orgbranch.entity.OrgBranch;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    indexes = {
            @Index(name = "idx_room_org_branch_entity_id", columnList = "orgBranchEntityId"),
            @Index(name = "idx_room_room_category_entity_id", columnList = "roomCategoryEntityId")
    }
)
public class Room extends BaseJpaEntity {
    @ManyToOne
    private OrgBranch orgBranch;

    private String orgBranchEntityId;

    @Column(unique = true, length = 100)
    private String name;

    @ManyToOne
    private RoomCategory roomCategory;

    private String roomCategoryEntityId;

    private String floorNumber;

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

    public void setRoomCategory(RoomCategory roomCategory) {
        this.roomCategory = roomCategory;
        if(this.roomCategory != null){
            this.roomCategoryEntityId = this.roomCategory.getEntityId();
        }
    }
}
