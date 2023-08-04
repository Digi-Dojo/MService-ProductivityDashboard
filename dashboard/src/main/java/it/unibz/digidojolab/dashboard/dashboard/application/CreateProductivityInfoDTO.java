package it.unibz.digidojolab.dashboard.dashboard.application;

public class CreateProductivityInfoDTO {
    private Long startup_id;

    private Long TeamMember_id;

    private String activity_type;

    public CreateProductivityInfoDTO(Long startup_id, Long teamMember_id, String activity_type) {
        this.startup_id = startup_id;
        TeamMember_id = teamMember_id;
        this.activity_type = activity_type;
    }

    public Long getStartup_id() {
        return startup_id;
    }

    public void setStartup_id(Long startup_id) {
        this.startup_id = startup_id;
    }

    public Long getTeamMember_id() {
        return TeamMember_id;
    }

    public void setTeamMember_id(Long teamMember_id) {
        TeamMember_id = teamMember_id;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }
}
