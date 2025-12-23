package com.medilabosolutions.frontservice.integration;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.Beans.RiskBean;
import com.medilabosolutions.frontservice.client.PatientGatewayClient;
import com.medilabosolutions.frontservice.client.RiskGatewayClient;
import com.medilabosolutions.frontservice.controller.PatientController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientGatewayClient patientGatewayClient;

    @Autowired
    private RiskGatewayClient riskGatewayClient;

    private PatientBean samplePatient;
    private RiskBean sampleRisk;

    @BeforeEach
    void setup() {
        samplePatient = new PatientBean();
        samplePatient.setId(1);
        samplePatient.setFirstName("John");
        samplePatient.setLastName("Doe");

        sampleRisk = new RiskBean();
        sampleRisk.setPatientId(1);
        sampleRisk.setRiskLevel("Borderline");
    }

    @Test
    void testListPatients_returnsPatientListView() throws Exception {
        when(patientGatewayClient.getListPatient()).thenReturn(List.of(samplePatient));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-list"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", hasSize(1)));
    }

    @Test
    void testGetPatientDetails_returnsPatientDetails() throws Exception {
        when(patientGatewayClient.getPatientById(1)).thenReturn(samplePatient);
        when(riskGatewayClient.getRiskPatient(1)).thenReturn(sampleRisk);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-details"))
                .andExpect(model().attribute("patient", samplePatient))
                .andExpect(model().attribute("risk", sampleRisk));
    }

    @Test
    void testGetPatientDetails_patientNotFound() throws Exception {
        when(patientGatewayClient.getPatientById(anyInt()))
                .thenThrow(new feign.FeignException.NotFound(
                        "Not Found",
                        null,
                        null,
                        null
                ));

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error-page"))
                .andExpect(model().attribute("errorMessage", containsString("n'existe pas")));
    }

    @Test
    void testShowRegisterForm_returnsPatientCreateView() throws Exception {
        mockMvc.perform(get("/patients/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-create"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    void testSavePatient_redirectsAfterCreation() throws Exception {
        mockMvc.perform(post("/patients/add")
                        .param("firstName", "Alice")
                        .param("lastName", "Smith"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        verify(patientGatewayClient, times(1)).createPatient(any(PatientBean.class));
    }

    @Test
    void testShowEditForm_returnsPatientEditView() throws Exception {
        when(patientGatewayClient.getPatientById(1)).thenReturn(samplePatient);

        mockMvc.perform(get("/patients/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-edit"))
                .andExpect(model().attribute("patient", samplePatient));
    }

    @Test
    void testEditPatient_redirectsAfterUpdate() throws Exception {
        mockMvc.perform(post("/patients/1/edit")
                        .param("firstName", "Updated")
                        .param("lastName", "User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/1"));

        verify(patientGatewayClient, times(1)).updatePatient(eq(1), any(PatientBean.class));
    }

    // --- Test configuration to inject mocks ---
    static class TestConfig {
        @Bean
        public PatientGatewayClient patientGatewayClient() {
            return Mockito.mock(PatientGatewayClient.class);
        }

        @Bean
        public RiskGatewayClient riskGatewayClient() {
            return Mockito.mock(RiskGatewayClient.class);
        }
    }
}
