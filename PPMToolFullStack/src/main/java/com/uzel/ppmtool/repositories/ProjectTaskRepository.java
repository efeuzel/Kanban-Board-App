package com.uzel.ppmtool.repositories;

import com.uzel.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

    List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

    ProjectTask findByProjectSequence(String sequence);
}
