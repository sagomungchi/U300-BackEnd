package it.toping.demo.repository;

import it.toping.demo.model.FundingInvestmentCount;
import it.toping.demo.model.Investment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    @Query("SELECT NEW it.toping.demo.model.FundingInvestmentCount(i.funding.id, count(i.id)) FROM Investment i WHERE i.project.id in :projectIds GROUP BY i.funding.id")
    List<FundingInvestmentCount> countByProjectIdInGroupByProjectId(@Param("projectIds") List<Long> projectIds);

    @Query("SELECT NEW it.toping.demo.model.FundingInvestmentCount(i.funding.id, count(i.id)) FROM Investment i WHERE i.project.id = :projectId GROUP BY i.funding.id")
    List<FundingInvestmentCount> countByProjectIdGroupByFundingId(@Param("projectId") Long projectId);

    @Query("SELECT i FROM Investment i where i.user.id = :userId and i.project.id in :projectIds")
    List<Investment> findByUserIdAndPollIdIn(@Param("userId") Long userId, @Param("projectIds") List<Long> projectIds);

    @Query("SELECT i FROM Investment i where i.user.id = :userId and i.project.id = :projectId")
    Investment findByUserIdAndPollId(@Param("userId") Long userId, @Param("projectId") Long projectIds);

    @Query("SELECT COUNT(i.id) from Investment i where i.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT i.project.id FROM Investment i WHERE i.user.id = :userId")
    Page<Long> findInvestedProjectIdsByUserId(@Param("userId") Long userId, Pageable pageable);
}
