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

    @GetMapping(value="/stats/month/{startup_id}/{yearmonth}")
    public HashMap<Long, Long> monthStatsPerStartup(@PathVariable("startup_id") String startupId, @PathVariable("yearmonth") String yearMonth) {
        // find first day of the month as LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth ym = YearMonth.parse(yearMonth, formatter);
        LocalDateTime firstDayOfMonth = ym.atDay(1).atStartOfDay();
        LocalDateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1);
        return mpi.computeWorkedTimeInPeriod(Long.parseLong(startupId), firstDayOfMonth, lastDayOfMonth);
    }

    @GetMapping(value="/stats/month/{startup_id}/{yearmonth}/aggregated")
    public Long aggregatedMonthStatsPerStartup(@PathVariable("startup_id") String startupId, @PathVariable("yearmonth") String yearMonth) {
        // find first day of the month as LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth ym = YearMonth.parse(yearMonth, formatter);
        LocalDateTime firstDayOfMonth = ym.atDay(1).atStartOfDay();
        LocalDateTime lastDayOfMonth = firstDayOfMonth.plusMonths(1);
        return mpi.computeAggregatedWorkedTimeInPeriod(Long.parseLong(startupId), firstDayOfMonth, lastDayOfMonth);
    }

    @GetMapping(value="/stats/week/{startup_id}/{yearmonthday}")
    public HashMap<Long, Long> weekStatsPerStartup(@PathVariable("startup_id") String startupId, @PathVariable("yearmonthday") String yearMonthDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime firstDay = LocalDate.parse(yearMonthDay, formatter).atStartOfDay();
        LocalDateTime lastDay = firstDay.plusDays(7);
        return mpi.computeWorkedTimeInPeriod(Long.parseLong(startupId), firstDay, lastDay);
    }

    @GetMapping(value="/stats/week/{startup_id}/{yearmonthday}/aggregated")
    public Long aggregatedWeekStatsPerStartup(@PathVariable("startup_id") String startupId, @PathVariable("yearmonthday") String yearMonthDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime firstDay = LocalDate.parse(yearMonthDay, formatter).atStartOfDay();
        LocalDateTime lastDay = firstDay.plusDays(7);
        return mpi.computeAggregatedWorkedTimeInPeriod(Long.parseLong(startupId), firstDay, lastDay);
    }
}
