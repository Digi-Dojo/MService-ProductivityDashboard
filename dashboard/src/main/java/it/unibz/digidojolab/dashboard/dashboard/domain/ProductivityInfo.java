package it.unibz.digidojolab.dashboard.dashboard.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class ProductivityInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long startupId;

    @Column
    private Long teamMemberId;

    @Column
    private String activityType;

    @Column
    @CreationTimestamp
    private LocalDateTime timestamp;

    public ProductivityInfo() {}

    // All fields are required so only one constructor here
    public ProductivityInfo(Long startup_id, Long teamMember_ID, String activity_type) {
        this.startupId = startup_id;
        teamMemberId = teamMember_ID;
        this.activityType = activity_type;
    }

    public Long getId() {
        return id;
    }

    public Long getStartupId() {
        return startupId;
    }

    public Long getTeamMemberId() {
        return teamMemberId;
    }

    public String getActivityType() {
        return activityType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartupId(Long startupId) {
        this.startupId = startupId;
    }

    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
