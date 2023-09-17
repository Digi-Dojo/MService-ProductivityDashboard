package it.unibz.digidojolab.dashboard.dashboard.application.kafka;

import it.unibz.digidojolab.dashboard.dashboard.domain.ManageProductivityInfo;
import it.unibz.digidojolab.dashboard.dashboard.domain.ProductivityInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Consumer {
    @Autowired
    private ProductivityInfoRepository piRepo;

    @Autowired
    private ManageProductivityInfo mpi;


    @KafkaListener(
            containerFactory = "userEventKafkaListenerContainerFactory",
            topics = "${it.unibz.digidojolab.dashboard.dashboard.kafka.consumer.topics.user.logged_in}",
            groupId = "${it.unibz.digidojolab.dashboard.dashboard.kafka.consumer.group_id}"
    )
    public void storeUserLoggedIn(UserEvent logInEvent) {
        mpi.insertPI(logInEvent.getStartupId(), logInEvent.getTeamMemberId(), "login");
    }

    @KafkaListener(
            containerFactory = "userEventKafkaListenerContainerFactory",
            topics = "${it.unibz.digidojolab.dashboard.dashboard.kafka.consumer.topics.user.logged_out}",
            groupId = "${it.unibz.digidojolab.dashboard.dashboard.kafka.consumer.group_id}"
    )
    public void storeUserLoggedOut(UserEvent logInEvent) {
        mpi.insertPI(logInEvent.getStartupId(), logInEvent.getTeamMemberId(), "logout");
    }

}
