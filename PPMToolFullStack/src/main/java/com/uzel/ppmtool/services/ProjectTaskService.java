package com.uzel.ppmtool.services;

import com.uzel.ppmtool.domain.Backlog;
import com.uzel.ppmtool.domain.ProjectTask;
import com.uzel.ppmtool.exceptions.ProjectNotFoundException;
import com.uzel.ppmtool.repositories.BacklogRepository;
import com.uzel.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
        //PTs to be added to a specific project, every PT have to be in a BL
        //set the BL to PT, set the project sequence
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            if (projectTask.getPriority() == null) {
                projectTask.setPriority(3);
            }
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TODO");
            }
            return projectTaskRepository.save(projectTask);
        } catch (Exception e) {
            throw new ProjectNotFoundException(String.format("Project with id \"%s\" does not exist", projectIdentifier));
        }


    }

    public List<ProjectTask> findBacklogById(String id) {
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {

        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if (backlog == null) {
            throw new ProjectNotFoundException(String.format("Project with id '%s' does not exist", backlog_id));
        }

        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if (projectTask == null) {
            throw new ProjectNotFoundException(String.format("Project task with id '%s' does not exist", pt_id));
        }

        if (!projectTask.getBacklog().getProjectIdentifier().equals(backlog_id)) {
            throw new ProjectNotFoundException(String.format("Project task with id '%s' does not exist in project '%s"
                    , pt_id, backlog_id));

        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }


    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        projectTaskRepository.delete(projectTask);
    }
}
