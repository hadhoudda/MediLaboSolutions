package com.medilabosolutions.front_app.controller;

import com.medilabosolutions.front_app.Beans.PatientBean;
import com.medilabosolutions.front_app.proxies.MicroservicePatientProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Controller
public class PatientController {

    @Value("${app.gateway.prefix}")
    private String gatewayPrefix;

    private final MicroservicePatientProxy patientProxy;

    public PatientController(MicroservicePatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    @RequestMapping("/patients")
    public String patients(Model model) {
        ResponseEntity<List<PatientBean>> response = patientProxy.getListPatient();
        List<PatientBean> patients = response.getBody(); // récupère la liste réelle
        model.addAttribute("patients", patients);
        model.addAttribute("gatewayPrefix", gatewayPrefix);
        return "patients";
    }

    @RequestMapping("/patients/{id}")
    public String detailPatient(@PathVariable("id") int id, Model model) {

        ResponseEntity<PatientBean> response = patientProxy.getPatientById(id);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            model.addAttribute("patient", response.getBody());
        } else {
            // si pas trouvé, tu peux rediriger vers une page d’erreur ou afficher un message
            model.addAttribute("error", "Patient non trouvé");
        }

        return "detailPatient"; // nom du template Thymeleaf
    }
//    private final WebClient webClient;
//
//    public PatientController(WebClient.Builder webClientBuilder) {
//        // "lb://" = load balancer (via Eureka)
//        this.webClient = webClientBuilder.baseUrl("lb://back-patient").build();
//    }
//
//    @GetMapping("/patients")
//    public String afficherPatients(Model model) {
//        List<PatientBean> patients = webClient.get()
//                .uri("/patients")
//                .retrieve()
//                .bodyToFlux(PatientBean.class)
//                .collectList()
//                .block();
//
//        model.addAttribute("patients", patients);
//        return "patients";
//    }
}
