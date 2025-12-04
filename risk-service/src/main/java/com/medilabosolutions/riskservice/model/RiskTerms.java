package com.medilabosolutions.riskservice.model;

import java.util.ArrayList;
import java.util.List;

public final class RiskTerms {

    private RiskTerms(){}// empêche l'instanciation

    public static final List<String> listRisks = List.of(
            "hemoglobine a1c",                     // Hémoglobine A1C
            "microalbumine",
            "taille",
            "poids",
            "(?:fum(?:e(?:ur|use)|er))",           // fumeur | fumeuse | fumer
            "anormal(?:e)?",                       // anormal | anormale
            "cholesterol",                         // Cholestérol
            "vertige(?:s)?",                       // vertige | vertiges
            "rechute(?:s)?",                       // rechute | rechutes
            "reaction(?:s)?",                      // réaction | réactions
            "anticorps"                            // anticorps (invariable)
    );

}
