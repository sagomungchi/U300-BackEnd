package it.toping.demo.service;

import it.toping.demo.exception.BadRequestException;
import it.toping.demo.exception.ResourceNotFoundException;
import it.toping.demo.model.*;
import it.toping.demo.payload.InvestmentRequest;
import it.toping.demo.payload.PagedResponse;
import it.toping.demo.payload.ProjectRequest;
import it.toping.demo.payload.ProjectResponse;
import it.toping.demo.repository.InvestmentRepository;
import it.toping.demo.repository.ProjectRepository;
import it.toping.demo.repository.UserRepository;
import it.toping.demo.security.UserPrincipal;
import it.toping.demo.util.AppConstants;
import it.toping.demo.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    public PagedResponse<ProjectResponse> getAllProjects(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Project> projects = projectRepository.findAll(pageable);

        if (projects.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(),
                    projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<Long> projectIds = projects.map(Project::getId).getContent();
        Map<Long, Long> fundingInvestCntMap = getFundingInvestCntMap(projectIds);
        Map<Long, Long> projectUserInvestMap = getProjectUserInvestMap(currentUser, projectIds);
        Map<Long, User> creatorMap = getProjectCreatorMap(projects.getContent());

        List<ProjectResponse> projectResponses = projects.map(project -> {
            return ModelMapper.mapProjectToProjectResponse(project,
                    fundingInvestCntMap,
                    creatorMap.get(project.getCreatedBy()),
                    projectUserInvestMap == null ? null : projectUserInvestMap.getOrDefault(project.getId(), null));
        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(),
                projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
    }

    public PagedResponse<ProjectResponse> getProjectsCreatedBy(String teamName, UserPrincipal currentUser, int page, int size){
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByTeamName(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "teamName", teamName));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Project> projects = projectRepository.findByCreatedBy(user.getId(), pageable);

        if(projects.getNumberOfElements() == 0){
            return new PagedResponse<>(Collections.emptyList(), projects.getNumber(),
                    projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());
        }

        List<Long> projectIds = projects.map(Project::getId).getContent();
        Map<Long, Long> fundingInvestCntMap = getFundingInvestCntMap(projectIds);
        Map<Long, Long> projectUserInvestMap = getProjectUserInvestMap(currentUser, projectIds);

        List<ProjectResponse> projectResponses = projects.map(project -> {
            return ModelMapper.mapProjectToProjectResponse(project,
                    fundingInvestCntMap,
                    user,
                    projectUserInvestMap == null ? null : projectUserInvestMap.getOrDefault(project.getId(), null));
        }).getContent();

        return new PagedResponse<>(projectResponses, projects.getNumber(),
                projects.getSize(), projects.getTotalElements(), projects.getTotalPages(), projects.isLast());

    }

    public PagedResponse<ProjectResponse> getProjectsInvestedBy(String userName, UserPrincipal currentUser, int page, int size){
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userName", userName));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Long> userInvestedProjectIds = investmentRepository.findInvestedProjectIdsByUserId(user.getId(), pageable);

        if(userInvestedProjectIds.getNumberOfElements() == 0){
            return new PagedResponse<>(Collections.emptyList(), userInvestedProjectIds.getNumber(),
                    userInvestedProjectIds.getSize(), userInvestedProjectIds.getTotalElements(),
                    userInvestedProjectIds.getTotalPages(), userInvestedProjectIds.isLast());
        }

        List<Long> projectIds = userInvestedProjectIds.getContent();

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Project> projects = projectRepository.findByIdIn(projectIds, sort);

        Map<Long, Long> fundingInvestCntMap = getFundingInvestCntMap(projectIds);
        Map<Long, Long> projectUserInvestMap = getProjectUserInvestMap(currentUser, projectIds);
        Map<Long, User> creatorMap = getProjectCreatorMap(projects);

        List<ProjectResponse> projectResponses = projects.stream().map(project -> {
            return ModelMapper.mapProjectToProjectResponse(project,
                    fundingInvestCntMap,
                    creatorMap.get(project.getCreatedBy()),
                    projectUserInvestMap == null ? null : projectUserInvestMap.getOrDefault(project.getId(), null));
        }).collect(Collectors.toList());

        return new PagedResponse<>(projectResponses, userInvestedProjectIds.getNumber(),
                                   userInvestedProjectIds.getSize(), userInvestedProjectIds.getTotalElements(),
                                   userInvestedProjectIds.getTotalPages(), userInvestedProjectIds.isLast());
    }

    public Project createProject(ProjectRequest projectRequest){

        Project project = new Project();
        project.setProjectName(projectRequest.getProjectName());

        projectRequest.getFundings().forEach(fundingRequest -> {
            project.addInvest(new Funding(fundingRequest.getBalance()));
        });

        Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(projectRequest.getFundingLength().getDates()))
                .plus(Duration.ofHours(projectRequest.getFundingLength().getHours()));

        project.setExpirationDateTime(expirationDateTime);

        return projectRepository.save(project);
    }

    public ProjectResponse getProjectById(Long projectId, UserPrincipal currentUser) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project", "id", projectId));

        List<FundingInvestmentCount> investments = investmentRepository.countByProjectIdGroupByFundingId(projectId);

        Map<Long, Long> fundingInvestMap = investments.stream()
                .collect(Collectors.toMap(FundingInvestmentCount::getFundingId, FundingInvestmentCount::getInvestmentTotal));

        User creator = userRepository.findById(project.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", project.getCreatedBy()));

        Investment userInvestment = null;
        if(currentUser != null){
            userInvestment = investmentRepository.findByUserIdAndPollId(currentUser.getId(), projectId);
        }

        return ModelMapper.mapProjectToProjectResponse(project, fundingInvestMap,
                creator, userInvestment != null ? userInvestment.getFunding().getId(): null);
    }

    public ProjectResponse doInvestAndGetUpdateProject(Long projectId, InvestmentRequest investmentRequest, UserPrincipal currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        if(project.getExpirationDateTime().isBefore(Instant.now())){
            throw new BadRequestException("Sorry!! This Project has already expried");
        }

        User user = userRepository.getOne(currentUser.getId());

        Funding selectedFunding = project.getFundings().stream()
                .filter(funding -> funding.getId().equals(investmentRequest.getFundingId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice", "id", investmentRequest.getFundingId()));

        Investment investment = new Investment();
        investment.setProject(project);
        investment.setUser(user);
        investment.setFunding(selectedFunding);

        try{
            investment = investmentRepository.save(investment);
        } catch (DataIntegrityViolationException e){
            logger.info("User {} has alredy funding in project {}", currentUser.getId(), projectId);
            throw new BadRequestException("Sorry! You have already Funding in this project!");
        }

        List<FundingInvestmentCount> investments = investmentRepository.countByProjectIdGroupByFundingId(projectId);

        Map<Long, Long> fundingInvestedMap = investments.stream()
                .collect(Collectors.toMap(FundingInvestmentCount::getFundingId, FundingInvestmentCount::getInvestmentTotal));

        User creator = userRepository.findById(project.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", project.getCreatedBy()));

        return ModelMapper.mapProjectToProjectResponse(project, fundingInvestedMap, creator, investment.getFunding().getId());
    }

    private void validatePageNumberAndSize(int page, int size){
        if(page < 0){
            throw new BadRequestException("Page Number cannot be less than Zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE){
            throw new BadRequestException("Page Size must not be greater than" + AppConstants.MAX_PAGE_SIZE);
        }
    }

    private Map<Long, Long> getFundingInvestCntMap(List<Long> projectIds){

        List<FundingInvestmentCount> investments = investmentRepository.countByProjectIdInGroupByProjectId(projectIds);

        Map<Long, Long> fundingInvestsMap = investments.stream()
                .collect(Collectors.toMap(FundingInvestmentCount::getFundingId, FundingInvestmentCount::getInvestmentTotal));

        return fundingInvestsMap;
    }

    private Map<Long, Long> getProjectUserInvestMap(UserPrincipal currentUser, List<Long> projectIds) {

        Map<Long, Long> projectUserInvestMap = null;
        if(currentUser != null){
            List<Investment> userInvestment = investmentRepository.findByUserIdAndPollIdIn(currentUser.getId(), projectIds);

            projectUserInvestMap = userInvestment.stream()
                    .collect(Collectors.toMap(investment -> investment.getProject().getId(), investment -> investment.getFunding().getId()));
        }
        return projectUserInvestMap;
    }

    Map<Long, User> getProjectCreatorMap(List<Project> projects) {

        List<Long> creatorIds = projects.stream()
                .map(Project::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}
