package io.jobcard.service;

import io.jobcard.entity.JobCardTask;
import io.jobcard.repository.JobCardTaskRepository;
import io.lib.form.BaseFetchForm;
import io.lib.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobCardTaskReadService extends BaseJpaRepoReadService<JobCardTask, JobCardTaskRepository> {

    public Page<JobCardTask> listJobCardTasks(BaseFetchForm form) {
        return repository.findAll(createSpecification(form), repository.defaultPageable(form));
    }

    private Specification<JobCardTask> createSpecification(BaseFetchForm form) {
        var spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(repository.jobCardEntityIdIs(form.getQuery()));
        }
        return spec;
    }

    public List<String> findAllJobCardTasksEntityIdsByJobCardId(String jobCardEntityId) {

        Specification<JobCardTask> specification =
                repository.notDeleted()
                        .and(repository.jobCardEntityIdIs(jobCardEntityId));

        return repository.findAll(specification)
                .stream()
                .map(jobCardTask -> jobCardTask.getTask().getEntityId())
                .toList();
    }

}
