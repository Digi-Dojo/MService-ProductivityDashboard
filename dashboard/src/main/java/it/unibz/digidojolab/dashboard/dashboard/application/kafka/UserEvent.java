package it.unibz.digidojolab.dashboard.dashboard.application.kafka;

import lombok.Getter;
import lombok.Setter;

public class UserEvent {
    @Setter @Getter
    private Long startupId;

    @Setter @Getter
    private Long teamMemberId;
}
