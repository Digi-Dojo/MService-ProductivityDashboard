package it.unibz.digidojolab.dashboard.dashboard.application;

import it.unibz.digidojolab.dashboard.dashboard.domain.ManageProductivityInfo;
import it.unibz.digidojolab.dashboard.dashboard.domain.ProductivityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@RestController
@RequestMapping("/productivityinfo")
public class ProductivityInfoController {
    @Autowired
    private ManageProductivityInfo mpi;

    @PostMapping
    public ProductivityInfo create(@RequestBody CreateProductivityInfoDTO dto) {
        return mpi.insertPI(dto.getStartupId(), dto.getTeamMemberId(), dto.getActivityType());
    }

    LocalDateTime getStartDate(String period, String startTimestamp) {
        if (period.equals("month")) {
            // find first day of the month as LocalDate object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            YearMonth ym = YearMonth.parse(startTimestamp, formatter);
            return ym.atDay(1).atStartOfDay();
        } else if (period.equals("week")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(startTimestamp, formatter).atStartOfDay();
        } else throw new IllegalArgumentException("period should either be week or month");
    }

    LocalDateTime getEndDate(String period, LocalDateTime startDate) {
        if (period.equals("month")) {
            return startDate.plusMonths(1);
        } else if (period.equals("week")) {
            return startDate.plusDays(7);
        } else throw new IllegalArgumentException("period should either be week or month");
    }

    @CrossOrigin(origins = "https://digidojo-productivitydashboard.onrender.com")
    @GetMapping("/startups/{startup_id}/worked_hours")
    public HashMap<Long, Long> getStartupWorkedHours(
            @PathVariable("startup_id") Long startupId,
            @RequestParam(value = "period", defaultValue = "week") String period,
            @RequestParam("start") String startTimestamp) {

        LocalDateTime startDate = getStartDate(period, startTimestamp);
        LocalDateTime endDate = getEndDate(period, startDate);

        return mpi.computeWorkedTimeInPeriod(startupId, startDate, endDate);
    }

    @CrossOrigin(origins = "https://digidojo-productivitydashboard.onrender.com")
    @GetMapping("/members/{member_id}/worked_hours")
    public HashMap<Long, Long> getMemberWorkedHours(
            @PathVariable("member_id") Long memberId,
            @RequestParam(value = "period", defaultValue = "week") String period,
            @RequestParam("start") String startTimestamp) {

        LocalDateTime startDate = getStartDate(period, startTimestamp);
        LocalDateTime endDate = getEndDate(period, startDate);

        return mpi.computeWorkedTimeForMemberInPeriod(memberId, startDate, endDate);
    }

}
