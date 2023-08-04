package it.unibz.digidojolab.dashboard.dashboard.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageProductivityInfo {
    private ProductivityInfoRepository pi_repo;

    @Autowired
    public ManageProductivityInfo(ProductivityInfoRepository repo) {
        pi_repo = repo;
    }

    public ProductivityInfo insertPI(Long startup_id, Long TeamMember_id, String activity_type) {
        if (!activity_type.equals("login") && !activity_type.equals("logout"))
            throw new IllegalArgumentException("Invalid activity_type");
        return pi_repo.save(new ProductivityInfo(startup_id, TeamMember_id, activity_type));
    }
}
