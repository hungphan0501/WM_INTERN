package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public Project createProject(Project project) {
       return  projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    public Project findById(Long id){
        return projectRepository.findById(id).orElse(null);
    }
    public Project findByName(String name) {
        return projectRepository.findByName(name);
    }


    @Transactional
    public void delete(Project project) {
        projectRepository.delete(project);
    }
}
