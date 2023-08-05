package it.unibz.digidojolab.dashboard.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductivityInfoRepository extends JpaRepository<ProductivityInfo, Long> {
    List<ProductivityInfo> findByStartupIdAndActivityTypeAndTimestampBetween(
            Long startupId, String activityType, LocalDateTime timestamp, LocalDateTime after
    );

    List<ProductivityInfo> findByStartupIdAndTeamMemberIdAndActivityTypeAndTimestampBetween(
            Long startupId, Long teamMemberId, String activityType, LocalDateTime timestamp, LocalDateTime after
    );
}
