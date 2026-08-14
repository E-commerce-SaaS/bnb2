package io.jobcard.service;

import io.jobcard.entity.JobCard;
import io.jobcard.form.JobCardFetchForm;
import io.jobcard.repository.JobCardRepository;
import io.jobcardtask.entity.JobCardTask;
import io.jobcardtask.entity.JobCardTaskStatus;
import io.jobcardtask.repository.JobCardTaskRepository;
import io.lib.service.BaseJpaRepoReadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class JobCardReadService extends BaseJpaRepoReadService<JobCard, JobCardRepository> {

    private JobCardTaskRepository jobCardTaskRepository;

    public Page<JobCard> listJobCards(JobCardFetchForm form){
        return repository.findAll(createSpecification(form),repository.defaultPageable(form));
    }

    private Specification<JobCard> createSpecification(JobCardFetchForm form){
        var spec = repository.notDeleted();

        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.entityIdIs(form.getQuery()));
        }
        return spec;
    }

    public boolean allJobCardTasksAreDone(String jobCardEntityId) {
        Specification<JobCardTask> totalSpec = (root, query, cb) ->
                cb.equal(root.get("jobCard").get("entityId"), jobCardEntityId);

        long totalTasks = jobCardTaskRepository.count(totalSpec);
        if (totalTasks == 0) return false;

        Specification<JobCardTask> unfinishedSpec = (root, query, cb) -> cb.and(
                cb.equal(root.get("jobCard").get("entityId"), jobCardEntityId),
                cb.not(root.get("status").in(JobCardTaskStatus.DONE, JobCardTaskStatus.NOT_APPLICABLE))
        );

        long unfinishedTasks = jobCardTaskRepository.count(unfinishedSpec);

        return unfinishedTasks == 0;
    }

    @Autowired
    public void setJobCardTaskRepository(JobCardTaskRepository service) {

        this.jobCardTaskRepository = service;
    }

}
