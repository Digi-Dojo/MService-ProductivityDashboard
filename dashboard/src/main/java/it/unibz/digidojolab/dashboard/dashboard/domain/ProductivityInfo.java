package it.unibz.digidojolab.dashboard.dashboard.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
public class ProductivityInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long startup_id;

    @Column
    private Long TeamMember_id;

    @Column
    private String activity_type;

    @Column
    @CreationTimestamp
    private Date timestamp;

    // All fields are required so only one constructor here
    public ProductivityInfo(Long startup_id, Long teamMember_ID, String activity_type) {
        this.startup_id = startup_id;
        TeamMember_id = teamMember_ID;
        this.activity_type = activity_type;
    }

    public Long getId() {
        return id;
    }

    public Long getStartup_id() {
        return startup_id;
    }

    public Long getTeamMember_id() {
        return TeamMember_id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartup_id(Long startup_id) {
        this.startup_id = startup_id;
    }

    public void setTeamMember_id(Long teamMember_id) {
        TeamMember_id = teamMember_id;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
