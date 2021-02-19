package com.uzel.ppmtool.services;

import com.uzel.ppmtool.domain.Project;
import com.uzel.ppmtool.exceptions.ProjectIdException;
import com.uzel.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase(Locale.ROOT));
            return projectRepository.save(project);
        } catch (Exception e) {
            throw new ProjectIdException("Project identifier '"
                    + project.getProjectIdentifier().toUpperCase(Locale.ROOT) + "' is not unique");
        }
    }

    public Project findProjectByIdentifier(String projectIdentifier) {
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier);

        if (project == null) {
            throw new ProjectIdException("Project identifier '" + projectIdentifier + "' does not exist");
        }

        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String identifier) {
        identifier = identifier.toUpperCase();
        Project project = projectRepository.findByProjectIdentifier(identifier);
        if (project == null) {
            throw new ProjectIdException("Project with id '" + identifier + "' cannot be deleted because it does not exist");
        }
        projectRepository.delete(project);
    }


}
