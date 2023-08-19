package it.unibz.digidojolab.dashboard.dashboard.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

@Service
public class ManageProductivityInfo {
    private ProductivityInfoRepository pi_repo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ManageProductivityInfo(ProductivityInfoRepository repo) {
        pi_repo = repo;
    }

    public ProductivityInfo insertPI(Long startupId, Long teamMemberId, String activityType) {
        if (!activityType.equals("login") && !activityType.equals("logout"))
            throw new IllegalArgumentException("Invalid activity_type");
        return pi_repo.save(new ProductivityInfo(startupId, teamMemberId, activityType));
    }

    private Long computeShiftDuration(ProductivityInfo shiftStart) {
        // ASSUMPTION: a shift can last no longer than 24 hours.
        // By using this implementation, we can significantly reduce
        // compute time and also allow edge cases in which the shift
        // end the night of the first day of the following month.
        LocalDateTime shiftEnd = shiftStart.getTimestamp().plusDays(1);
        List<ProductivityInfo> logout = pi_repo.findByStartupIdAndTeamMemberIdAndActivityTypeAndTimestampBetween(
                shiftStart.getStartupId(), shiftStart.getTeamMemberId(),"logout", shiftStart.getTimestamp(), shiftEnd
        );
        if (logout.size() < 1)
            throw new IllegalStateException("Unable to find relevant logout for " + shiftStart);
        return shiftStart.getTimestamp().until(logout.get(0).getTimestamp(), ChronoUnit.MINUTES);
    }

    public HashMap<Long, Long> computeWorkedTimeInPeriod(Long startupId, LocalDateTime start, LocalDateTime end) {
        List<ProductivityInfo> logins = pi_repo.findByStartupIdAndActivityTypeAndTimestampBetween(
                startupId, "login", start, end
        );
        HashMap<Long, Long> workedMinutes = new HashMap<>();
        for (ProductivityInfo pi : logins) {
            Long shiftDuration = computeShiftDuration(pi);
            if (workedMinutes.containsKey(pi.getTeamMemberId()))
                workedMinutes.put(pi.getTeamMemberId(), workedMinutes.get(pi.getTeamMemberId()) + shiftDuration);
            else
                workedMinutes.put(pi.getTeamMemberId(), shiftDuration);
        }
        return workedMinutes;
    }

    public HashMap<Long, Long> computeWorkedTimeForMemberInPeriod(Long teamMemberId, LocalDateTime start, LocalDateTime end) {
        List<ProductivityInfo> logins = pi_repo.findByTeamMemberIdAndActivityTypeAndTimestampBetween(
                teamMemberId,"login", start, end
        );
        HashMap<Long, Long> workedMinutes = new HashMap<>();
        for (ProductivityInfo pi : logins) {
            Long shiftDuration = computeShiftDuration(pi);
            if (workedMinutes.containsKey(pi.getStartupId()))
                workedMinutes.put(pi.getStartupId(), workedMinutes.get(pi.getTeamMemberId()) + shiftDuration);
            else
                workedMinutes.put(pi.getStartupId(), shiftDuration);
        }
        return workedMinutes;
    }

}
