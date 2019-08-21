package it.toping.demo.controller;

import it.toping.demo.model.Project;
import it.toping.demo.model.User;
import it.toping.demo.payload.*;
import it.toping.demo.repository.InvestmentRepository;
import it.toping.demo.repository.ProjectRepository;
import it.toping.demo.repository.UserRepository;
import it.toping.demo.security.CurrentUser;
import it.toping.demo.security.UserPrincipal;
import it.toping.demo.service.ProjectService;
import it.toping.demo.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @GetMapping
    public PagedResponse<ProjectResponse> getProjects(@CurrentUser UserPrincipal currentUser,
                                                      @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
                                                      ){
        return projectService.getAllProjects(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest){

        Project project = projectService.createProject(projectRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/{projectId}")
                .buildAndExpand(project.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Project Created Successfully!"));
    }

    @GetMapping("/{projectId}")
    public ProjectResponse getProjectById(@CurrentUser UserPrincipal currentUser,
                                          @PathVariable Long projectId) {

        return projectService.getProjectById(projectId, currentUser);

    }

    @PostMapping("/{projectId}/investments")
    @PreAuthorize("hasRole('USER')")
    public ProjectResponse doInvestment(@CurrentUser UserPrincipal currentUser,
                                        @PathVariable Long projectId,
                                        @Valid @RequestBody InvestmentRequest investmentRequest){
        return projectService.doInvestAndGetUpdateProject(projectId, investmentRequest, currentUser);
    }

}
