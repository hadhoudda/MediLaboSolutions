package com.medilabosolutions.frontservice.client;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.Beans.RiskBean;
import com.medilabosolutions.frontservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client used to communicate with the Risk microservice.
 * Provides operations to retrieve risk assessment for a patient.
 */
@FeignClient(
        name = "risk-service",
        url = "${gateway.url}",
        configuration = FeignConfig.class
)
public interface RiskGatewayClient {

    /**
     * Retrieves the risk assessment for a given patient.
     *
     * @param id the patient identifier
     * @return the patient's risk assessment
     */
    @GetMapping("risk-service/api/risk/patient/{id}")
    RiskBean getRiskPatient(@PathVariable("id") int id);

}
