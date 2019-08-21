package it.toping.demo.controller;


import it.toping.demo.exception.ResourceNotFoundException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private ProjectService projectService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser){
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getTeamName());
        return userSummary;
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email){
        Boolean isAvailable = !userRepository.existsByUserEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{teamname}")
    public UserProfile getUserProfile(@PathVariable(value = "teamname") String teamName){
        User user = userRepository.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "teamName", teamName));

        long projectCnt = projectRepository.countByCreatedBy(user.getId());
        long investCnt  = investmentRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUserName(), user.getTeamName(), user.getCreatedAt(), projectCnt, investCnt);

        return userProfile;
    }

    @GetMapping("/users/{username}/investments")
    public PagedResponse<ProjectResponse> getProjectsInvestedBy(@PathVariable(value = "username") String userName,
                                                                @CurrentUser UserPrincipal currentUser,
                                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
                                                                ){
        return projectService.getProjectsInvestedBy(userName, currentUser, page, size);
    }

}
