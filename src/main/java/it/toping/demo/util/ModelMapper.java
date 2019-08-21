package it.toping.demo.util;

import it.toping.demo.model.Project;
import it.toping.demo.model.User;
import it.toping.demo.payload.FundingResponse;
import it.toping.demo.payload.ProjectResponse;
import it.toping.demo.payload.UserSummary;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapper {

    public static ProjectResponse mapProjectToProjectResponse(Project project, Map<Long, Long> fundingInvestMap, User creator, Long userInvestment){

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setProjectName(project.getProjectName());
        projectResponse.setCreationDateTime(project.getCreatedAt());
        projectResponse.setExpirationDateTime(project.getExpirationDateTime());
        Instant now = Instant.now();
        projectResponse.setExpired(project.getExpirationDateTime().isBefore(now));

        List<FundingResponse> fundingResponses = project.getFundings().stream().map(funding -> {
            FundingResponse fundingResponse = new FundingResponse();
            fundingResponse.setId(funding.getId());
            fundingResponse.setBalance(funding.getBalance());

            if(fundingInvestMap.containsKey(funding.getId())) {
                fundingResponse.setInvestmentCnt(fundingInvestMap.get(funding.getId()));
            } else {
                fundingResponse.setInvestmentCnt(0);
            }
            return fundingResponse;
        }).collect(Collectors.toList());

        projectResponse.setFundings(fundingResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUserName(), creator.getTeamName());
        projectResponse.setCreatedBy(creatorSummary);

        if(userInvestment != null){
            projectResponse.setSelectedFunding(userInvestment);
        }

        long totalInvestment = projectResponse.getFundings().stream().mapToLong(FundingResponse::getInvestmentCnt).sum();
        projectResponse.setTotalInvestment(totalInvestment);

        return projectResponse;
    }
}
