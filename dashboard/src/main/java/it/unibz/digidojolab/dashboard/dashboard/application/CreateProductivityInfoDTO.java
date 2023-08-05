package it.unibz.digidojolab.dashboard.dashboard.application;

public class CreateProductivityInfoDTO {
    private Long startupId;

    private Long teamMemberId;

    private String activityType;

    public CreateProductivityInfoDTO(Long startupId, Long teamMemberId, String activityType) {
        this.startupId = startupId;
        this.teamMemberId = teamMemberId;
        this.activityType = activityType;
    }

    public Long getStartupId() {
        return startupId;
    }

    public void setStartupId(Long startupId) {
        this.startupId = startupId;
    }

    public Long getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
