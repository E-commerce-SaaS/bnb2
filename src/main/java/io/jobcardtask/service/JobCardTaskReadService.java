package io.jobcardtask.service;

import io.jobcardtask.entity.JobCardTask;
import io.jobcardtask.form.JobCardTaskFetchForm;
import io.jobcardtask.repository.JobCardEntityIdProjection;
import io.jobcardtask.repository.JobCardTaskRepository;
import io.lib.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class JobCardTaskReadService extends BaseJpaRepoReadService<JobCardTask, JobCardTaskRepository> {

    public Page<JobCardTask> listJobCardTasks(JobCardTaskFetchForm form) {
        return repository.findAll(createSpecification(form), repository.defaultPageable(form));
    }

    private Specification<JobCardTask> createSpecification(JobCardTaskFetchForm form) {
        var spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(repository.jobCardEntityIdIs(form.getQuery()));
        }
        return spec;
    }


    public String getJobCardEntityIdFromJobCardTask(String jobCardTaskId) {
        var spec = repository.notDeleted()
                .and(repository.jobCardTaskIdIs(jobCardTaskId));

        return repository.findBy(spec, q -> q.as(JobCardEntityIdProjection.class).stream().findFirst())
                .map(JobCardEntityIdProjection::getJobCardEntityId)
                .orElse(null);
    }


}
