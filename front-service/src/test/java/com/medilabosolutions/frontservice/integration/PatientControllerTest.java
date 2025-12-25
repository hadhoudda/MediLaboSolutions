package com.medilabosolutions.frontservice.integration;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.Beans.RiskBean;
import com.medilabosolutions.frontservice.client.PatientGatewayClient;
import com.medilabosolutions.frontservice.client.RiskGatewayClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientGatewayClient patientGatewayClient;

    @MockBean
    private RiskGatewayClient riskGatewayClient;

    private PatientBean patient;
    private RiskBean riskPatient;

    @BeforeEach
    void setup() {
        patient = new PatientBean();
        patient.setId(1);
        patient.setFirstName("John");
        patient.setLastName("Doe");

        riskPatient = new RiskBean();
        riskPatient.setPatientId(1);
        riskPatient.setRiskLevel("Borderline");
    }

    /**
     * Test that GET /patients returns the list of patients view with the correct model attribute.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testListPatients_returnsPatientListView() throws Exception {
        // GIVEN: the patient service returns a list containing the sample patient
        when(patientGatewayClient.getListPatient()).thenReturn(List.of(patient));

        // WHEN: a GET request is performed to /patients
        // THEN: the response status is OK, the view is correct, and model contains patients
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-list"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", hasSize(1)));
    }

    /**
     * Test that GET /patients/{id} returns the patient details view with patient and risk in model.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetPatientDetails_returnsPatientDetailsView() throws Exception {
        // GIVEN: the patient and risk services return sample data
        when(patientGatewayClient.getPatientById(1)).thenReturn(patient);
        when(riskGatewayClient.getRiskPatient(1)).thenReturn(riskPatient);

        // WHEN: a GET request is performed to /patients/1
        // THEN: the response status is OK, view is "patient-details", model contains patient and risk
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-details"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("risk"));
    }

    /**
     * Test that GET /patients/add returns the patient creation form view.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowRegisterForm_returnsPatientCreateView() throws Exception {
        // GIVEN: no preconditions are required

        // WHEN: a GET request is performed to /patients/add
        // THEN: response status is OK, view is "patient-create", model contains a patient attribute
        mockMvc.perform(get("/patients/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-create"))
                .andExpect(model().attributeExists("patient"));
    }

    /**
     * Test that POST /patients/add redirects to /patients after creating a new patient.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testSavePatient_redirectsToPatientList() throws Exception {
        // GIVEN: a form submission with firstName and lastName parameters

        // WHEN: a POST request is performed to /patients/add
        // THEN: redirect to /patients and createPatient is invoked
        mockMvc.perform(post("/patients/add")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        Mockito.verify(patientGatewayClient).createPatient(Mockito.any(PatientBean.class));
    }

    /**
     * Test that GET /patients/{id}/edit returns the patient edit form view.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowEditForm_returnsPatientEditView() throws Exception {
        // GIVEN: the patient service returns the sample patient
        when(patientGatewayClient.getPatientById(1)).thenReturn(patient);

        // WHEN: a GET request is performed to /patients/1/edit
        // THEN: response status is OK, view is "patient-edit", model contains patient attribute
        mockMvc.perform(get("/patients/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-edit"))
                .andExpect(model().attributeExists("patient"));
    }

    /**
     * Test that POST /patients/{id}/edit redirects to the patient details page after update.
     */
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testEditPatient_redirectsToPatientDetails() throws Exception {
        // GIVEN: a form submission with firstName and lastName parameters

        // WHEN: a POST request is performed to /patients/1/edit
        // THEN: redirect to /patients/1 and updatePatient is invoked
        mockMvc.perform(post("/patients/1/edit")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/1"));

        Mockito.verify(patientGatewayClient).updatePatient(Mockito.eq(1), Mockito.any(PatientBean.class));
    }
}
