package it.unibz.digidojolab.dashboard;

import it.unibz.digidojolab.dashboard.dashboard.domain.ManageProductivityInfo;
import it.unibz.digidojolab.dashboard.dashboard.domain.ProductivityInfo;
import it.unibz.digidojolab.dashboard.dashboard.domain.ProductivityInfoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DashboardIntegrationTest {
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private ProductivityInfoRepository piRepo;

    @Autowired
    private ManageProductivityInfo mpi;

    private void ensureEmptyDatabase() {
        piRepo.deleteAll();
    }

    @BeforeEach
    public void setup() {
        baseUrl = baseUrl + ":" + port + "/productivityinfo";

        ensureEmptyDatabase();
    }

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    public ProductivityInfo productivityInfoCreationPost(ProductivityInfo sample) {
        return restTemplate.postForObject(baseUrl, sample, sample.getClass());
    }

    @Test
    public void itCreatesLogin() {
        ProductivityInfo testLogin = new ProductivityInfo(1L, 3L, "login");
        ProductivityInfo response = productivityInfoCreationPost(testLogin);
        assertThat(testLogin.getId()).isNull();
        assertThat(response)
                .isNotNull()
                .isInstanceOf(ProductivityInfo.class);
        assertThat(response.getId())
                .isNotNull();
        assertThat(response.getStartupId()).isEqualTo(testLogin.getStartupId());
        assertThat(response.getTeamMemberId()).isEqualTo(testLogin.getTeamMemberId());
        assertThat(response.getActivityType()).isEqualTo(testLogin.getActivityType());
    }

    @Test
    public void itCreatesLogout() {
        ProductivityInfo testLogout = new ProductivityInfo(2L, 5L, "logout");
        ProductivityInfo response = productivityInfoCreationPost(testLogout);
        assertThat(testLogout.getId()).isNull();
        assertThat(response)
                .isNotNull()
                .isInstanceOf(ProductivityInfo.class);
        assertThat(response.getId())
                .isNotNull();
        assertThat(response.getStartupId()).isEqualTo(testLogout.getStartupId());
        assertThat(response.getTeamMemberId()).isEqualTo(testLogout.getTeamMemberId());
        assertThat(response.getActivityType()).isEqualTo(testLogout.getActivityType());
    }

    @Test
    public void itDoesNotCreateUnknownType() {
        ProductivityInfo testWrong = new ProductivityInfo(1L, 3L, "whatever");
        assertThatThrownBy(() -> restTemplate.postForObject(baseUrl, testWrong, testWrong.getClass()))
                .isInstanceOf(HttpClientErrorException.class);
    }

    @Test
    public void singleTeamMemberWeeklyComputationIsCorrect() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(15));
        piRepo.save(testLogout);
        HashMap output = restTemplate.getForEntity(
                baseUrl + "/stats/week/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HashMap.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output.get("2")).isEqualTo(75);
    }

    @Test
    public void singleTeamMemberWeeklyComputationIgnoresDataOutsideCurrentWeek() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(15));
        piRepo.save(testLogout);
        ProductivityInfo testLogin2 = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout2 = mpi.insertPI(1L, 2L, "logout");
        testLogin2.setTimestamp(testLogin2.getTimestamp().plusDays(10));
        piRepo.save(testLogin2);
        testLogout2.setTimestamp(testLogin2.getTimestamp().plusHours(1).plusMinutes(15));
        piRepo.save(testLogout2);
        HashMap output = restTemplate.getForEntity(
                baseUrl + "/stats/week/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HashMap.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output.get("2")).isEqualTo(75);
    }

    @Test
    public void multipleTeamMembersWeeklyComputationIsCorrect() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(10));
        piRepo.save(testLogout);
        ProductivityInfo testLogin2 = mpi.insertPI(1L, 3L, "login");
        ProductivityInfo testLogout2 = mpi.insertPI(1L, 3L, "logout");
        testLogout2.setTimestamp(testLogin2.getTimestamp().plusHours(2).plusMinutes(30));
        piRepo.save(testLogout2);
        HashMap output = restTemplate.getForEntity(
                baseUrl + "/stats/week/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HashMap.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output.get("2")).isEqualTo(70);
        assertThat(output.get("3")).isEqualTo(150);
    }

    @Test
    public void multipleStartupsWeeklyComputationIsCorrect() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(5));
        piRepo.save(testLogout);
        ProductivityInfo testLogin2 = mpi.insertPI(2L, 2L, "login");
        ProductivityInfo testLogout2 = mpi.insertPI(2L, 2L, "logout");
        testLogout2.setTimestamp(testLogin2.getTimestamp().plusHours(2).plusMinutes(45));
        piRepo.save(testLogout2);
        HashMap output = restTemplate.getForEntity(
                baseUrl + "/stats/week/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HashMap.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output.get("2")).isEqualTo(65);
        HashMap output2 = restTemplate.getForEntity(
                baseUrl + "/stats/week/2/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                HashMap.class
        ).getBody();
        assertThat(output2).isNotNull();
        assertThat(output2.get("2")).isEqualTo(165);
    }

    @Test
    public void singleTeamMemberMonthlyAggregatedComputationIsCorrect() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(15));
        piRepo.save(testLogout);
        Long output = restTemplate.getForEntity(
                baseUrl + "/stats/month/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMM")) + "/aggregated",
                Long.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output).isEqualTo(75);
    }

    @Test
    public void multipleTeamMembersMonthlyAggregatedComputationIsCorrect() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(5));
        piRepo.save(testLogout);
        ProductivityInfo testLogin2 = mpi.insertPI(1L, 4L, "login");
        ProductivityInfo testLogout2 = mpi.insertPI(1L, 4L, "logout");
        testLogout2.setTimestamp(testLogin2.getTimestamp().plusHours(2).plusMinutes(45));
        piRepo.save(testLogout2);
        Long output = restTemplate.getForEntity(
                baseUrl + "/stats/month/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMM")) + "/aggregated",
                Long.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output).isEqualTo(230);
    }

    @Test
    public void singleTeamMemberWeeklyAggregatedComputationIsCorrect() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(15));
        piRepo.save(testLogout);
        Long output = restTemplate.getForEntity(
                baseUrl + "/stats/week/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/aggregated",
                Long.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output).isEqualTo(75);
    }

    @Test
    public void multipleTeamMembersWeeklyAggregatedComputationIsCorrect() {
        ProductivityInfo testLogin = mpi.insertPI(1L, 2L, "login");
        ProductivityInfo testLogout = mpi.insertPI(1L, 2L, "logout");
        testLogout.setTimestamp(testLogin.getTimestamp().plusHours(1).plusMinutes(5));
        piRepo.save(testLogout);
        ProductivityInfo testLogin2 = mpi.insertPI(1L, 4L, "login");
        ProductivityInfo testLogout2 = mpi.insertPI(1L, 4L, "logout");
        testLogout2.setTimestamp(testLogin2.getTimestamp().plusHours(2).plusMinutes(45));
        piRepo.save(testLogout2);
        Long output = restTemplate.getForEntity(
                baseUrl + "/stats/week/1/" + testLogin.getTimestamp().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/aggregated",
                Long.class
        ).getBody();
        assertThat(output).isNotNull();
        assertThat(output).isEqualTo(230);
    }

}
