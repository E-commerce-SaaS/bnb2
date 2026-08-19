package io.jobcard.service;

import io.jobcard.entity.JobCard;
import io.jobcard.repository.JobCardRepository;
import io.jobcard.entity.JobCardTask;
import io.jobcard.entity.JobCardTaskStatus;
import io.jobcard.repository.JobCardTaskRepository;
import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class JobCardReadService extends BaseJpaRepoReadService<JobCard, JobCardRepository> {

    private JobCardTaskRepository jobCardTaskRepository;

    public Page<JobCard> listJobCards(BaseFetchForm form){
        return repository.findAll(createSpecification(form),repository.defaultPageable(form));
    }

    private Specification<JobCard> createSpecification(BaseFetchForm form){
        var spec = repository.notDeleted();

        if(StringUtils.isNotBlank(form.getQuery())){
            spec = spec.and(repository.entityIdIs(form.getQuery()));
        }
        return spec;
    }

    public boolean allJobCardTasksAreDone(String jobCardEntityId) {
        Specification<JobCardTask> unfinishedSpec =
                jobCardTaskRepository.notDeleted()
                        .and((root, query, cb) -> cb.and(
                                cb.equal(
                                        root.get("jobCard").get("entityId"),
                                        jobCardEntityId
                                ),
                                cb.not(
                                        root.get("status").in(
                                                JobCardTaskStatus.DONE,
                                                JobCardTaskStatus.NOT_APPLICABLE
                                        )
                                )
                        ));
        return !jobCardTaskRepository.exists(unfinishedSpec);
    }

    @Autowired
    public void setJobCardTaskRepository(JobCardTaskRepository service) {

        this.jobCardTaskRepository = service;
    }

}
