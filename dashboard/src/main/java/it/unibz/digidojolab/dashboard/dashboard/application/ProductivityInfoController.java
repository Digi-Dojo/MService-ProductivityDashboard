package it.unibz.digidojolab.dashboard.dashboard.application;

import it.unibz.digidojolab.dashboard.dashboard.domain.ManageProductivityInfo;
import it.unibz.digidojolab.dashboard.dashboard.domain.ProductivityInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value="/stats/month/{startup_id}/{yearmonth}")
    public HashMap<Long, Long> monthStatsPerStartup(@PathVariable("startup_id") String startupId, @PathVariable("yearmonth") String yearMonth) {
        // find first day of the month as LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth ym = YearMonth.parse(yearMonth, formatter);
        LocalDateTime firstDayOfMonth = ym.atDay(1).atStartOfDay();
        LocalDateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1);
        System.out.println(firstDayOfMonth.toString());
        System.out.println(lastDayOfMonth.toString());
        return mpi.computeWorkedTimeInPeriod(Long.parseLong(startupId), firstDayOfMonth, lastDayOfMonth);
    }

    @GetMapping(value="/stats/week/{startup_id}/{yearmonthday}")
    public HashMap<Long, Long> weekStatsPerStartup(@PathVariable("startup_id") String startupId, @PathVariable("yearmonthday") String yearMonthDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime firstDayOfMonth = LocalDateTime.parse(yearMonthDay, formatter);
        LocalDateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1);
        System.out.println(firstDayOfMonth.toString());
        System.out.println(lastDayOfMonth.toString());
        return mpi.computeWorkedTimeInPeriod(Long.parseLong(startupId), firstDayOfMonth, lastDayOfMonth);
    }
}
