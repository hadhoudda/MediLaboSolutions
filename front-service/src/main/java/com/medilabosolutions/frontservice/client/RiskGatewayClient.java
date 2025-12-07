package com.medilabosolutions.frontservice.client;

import com.medilabosolutions.frontservice.Beans.PatientBean;
import com.medilabosolutions.frontservice.Beans.RiskBean;
import com.medilabosolutions.frontservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "api-risk",
        configuration = FeignConfig.class)
public interface RiskGatewayClient {

    @GetMapping("/api/risk/patient/{id}")
    RiskBean getRiskPatient(@PathVariable("id") int id);

}
