package it.unibz.digidojolab.dashboard.dashboard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductivityInfoRepository extends JpaRepository<ProductivityInfo, Long> {
    List<ProductivityInfo> findByStartupIdAndActivityTypeAndTimestampBetween(
            Long startup_id, String activity_type, LocalDateTime timestamp, LocalDateTime after
    );
}
