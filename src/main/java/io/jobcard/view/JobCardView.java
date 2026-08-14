package io.jobcard.view;

import io.internaluser.entity.InternalUser;
import io.jobcard.entity.JobCard;
import io.jobcard.entity.JobCardStatus;
import io.lib.view.BaseView;
import io.room.entity.Room;

public class JobCardView extends BaseView<JobCard> {

    public JobCardView(JobCard entity) {
        super(entity);
    }

    public InternalUser getStaff(){

        return entity.getStaff();
    }

    public Room getRoom() {
        return entity.getRoom();
    }

    public JobCardStatus getStatus() {return entity.getStatus();}

}
