package com.luizjacomn.billreminder.controller.v1;

import com.luizjacomn.billreminder.BillReminderApplicationTests;
import com.luizjacomn.billreminder.model.dto.BillRequest;
import com.luizjacomn.billreminder.model.dto.BillResponse;
import com.luizjacomn.billreminder.model.enumeration.AppellantType;
import com.luizjacomn.billreminder.model.enumeration.Category;
import com.luizjacomn.billreminder.util.DateUtil;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = { "/data/delete.sql", "/data/bill-controller-test.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class BillV1ControllerTest extends BillReminderApplicationTests {

    private static final String BASE_URL = "/api/v1/bills";

    @Order(1)
    @SneakyThrows
    @Test
    void registerBill_shouldCreateNewBill() {
        var request = new BillRequest(
            "Conta de Energia",
            AppellantType.MONTHLY,
            null,
            28,
            null,
            Boolean.TRUE,
            Set.of(Category.HOUSING)
        );

        var jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.endsWith("/2")));
    }

    @Order(2)
    @SneakyThrows
    @Test
    void listBills_shouldReturnAllBills() {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andDo(result -> {
                    var bills = objectMapper.readValue(result.getResponse().getContentAsString(), BillResponse[].class);
                    Assertions.assertEquals(2, bills.length);

                    var contaTelefone = bills[0];
                    Assertions.assertEquals("Conta de Telefone", contaTelefone.title());
                    Assertions.assertTrue(contaTelefone.isAppellant());
                    Assertions.assertEquals(AppellantType.MONTHLY, contaTelefone.appellantType());
                    Assertions.assertEquals("Os pagamentos são realizados no 5° dia útil do mês", contaTelefone.frequency());
                    Assertions.assertEquals(DateUtil.businessDateFor(5, YearMonth.now().plusMonths(1)), contaTelefone.nextPaymentDate());
                    Assertions.assertTrue(contaTelefone.categories().contains(Category.SUBSCRIPTIONS));

                    var contaEnergia = bills[1];
                    Assertions.assertEquals("Conta de Energia", contaEnergia.title());
                    Assertions.assertTrue(contaEnergia.isAppellant());
                    Assertions.assertEquals(AppellantType.MONTHLY, contaEnergia.appellantType());
                    Assertions.assertEquals("Os pagamentos são realizados todos os dias 28 do mês", contaEnergia.frequency());
                    Assertions.assertEquals(LocalDate.now().plusMonths(1).withDayOfMonth(28), contaEnergia.nextPaymentDate());
                    Assertions.assertTrue(contaEnergia.categories().contains(Category.HOUSING));
                });
    }

    @Order(3)
    @SneakyThrows
    @Test
    void listBills_shouldReturnBillsFilteringByTitle() {
        mockMvc.perform(get(BASE_URL).param("title", "energia"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    var bills = objectMapper.readValue(result.getResponse().getContentAsString(), BillResponse[].class);
                    Assertions.assertEquals(1, bills.length);

                    var contaEnergia = bills[0];
                    Assertions.assertEquals("Conta de Energia", contaEnergia.title());
                    Assertions.assertTrue(contaEnergia.isAppellant());
                    Assertions.assertEquals(AppellantType.MONTHLY, contaEnergia.appellantType());
                    Assertions.assertEquals("Os pagamentos são realizados todos os dias 28 do mês", contaEnergia.frequency());
                    Assertions.assertEquals(LocalDate.now().plusMonths(1).withDayOfMonth(28), contaEnergia.nextPaymentDate());
                    Assertions.assertTrue(contaEnergia.categories().contains(Category.HOUSING));
                });
    }

    @Order(4)
    @SneakyThrows
    @Test
    void listBills_shouldReturnBillsFilteringByCategories() {
        mockMvc.perform(get(BASE_URL).param("categories", "HOUSING"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    var bills = objectMapper.readValue(result.getResponse().getContentAsString(), BillResponse[].class);
                    Assertions.assertEquals(1, bills.length);

                    var contaEnergia = bills[0];
                    Assertions.assertEquals("Conta de Energia", contaEnergia.title());
                    Assertions.assertTrue(contaEnergia.isAppellant());
                    Assertions.assertEquals(AppellantType.MONTHLY, contaEnergia.appellantType());
                    Assertions.assertEquals("Os pagamentos são realizados todos os dias 28 do mês", contaEnergia.frequency());
                    Assertions.assertEquals(LocalDate.now().plusMonths(1).withDayOfMonth(28), contaEnergia.nextPaymentDate());
                    Assertions.assertTrue(contaEnergia.categories().contains(Category.HOUSING));
                });
    }

    @Order(5)
    @SneakyThrows
    @Test
    void listBills_shouldReturnBillsFilteringByDueDayOfMonth() {
        mockMvc.perform(get(BASE_URL).param("dueDayOfMonth", "28"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    var bills = objectMapper.readValue(result.getResponse().getContentAsString(), BillResponse[].class);
                    Assertions.assertEquals(1, bills.length);

                    var contaEnergia = bills[0];
                    Assertions.assertEquals("Conta de Energia", contaEnergia.title());
                    Assertions.assertTrue(contaEnergia.isAppellant());
                    Assertions.assertEquals(AppellantType.MONTHLY, contaEnergia.appellantType());
                    Assertions.assertEquals("Os pagamentos são realizados todos os dias 28 do mês", contaEnergia.frequency());
                    Assertions.assertEquals(LocalDate.now().plusMonths(1).withDayOfMonth(28), contaEnergia.nextPaymentDate());
                    Assertions.assertTrue(contaEnergia.categories().contains(Category.HOUSING));
                });
    }

    @Order(6)
    @SneakyThrows
    @Test
    void listBills_shouldReturnBillsFilteringByBusinessDayOfMonth() {
        mockMvc.perform(get(BASE_URL).param("businessDayOfMonth", "5"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    var bills = objectMapper.readValue(result.getResponse().getContentAsString(), BillResponse[].class);
                    Assertions.assertEquals(1, bills.length);

                    var contaTelefone = bills[0];
                    Assertions.assertEquals("Conta de Telefone", contaTelefone.title());
                    Assertions.assertTrue(contaTelefone.isAppellant());
                    Assertions.assertEquals(AppellantType.MONTHLY, contaTelefone.appellantType());
                    Assertions.assertEquals("Os pagamentos são realizados no 5° dia útil do mês", contaTelefone.frequency());
                    Assertions.assertEquals(DateUtil.businessDateFor(5, YearMonth.now().plusMonths(1)), contaTelefone.nextPaymentDate());
                    Assertions.assertTrue(contaTelefone.categories().contains(Category.SUBSCRIPTIONS));
                });
    }

}
