package com.medilabosolutions.riskservice.model;

import java.util.List;

/**
 * Utility class containing predefined medical risk-related terms used for risk assessment.
 * <p>
 * This class is final and cannot be instantiated.
 * The list of terms is used to scan clinical notes to identify potential risk factors.
 */

public final class RiskTerms {

    private RiskTerms(){} // Private constructor to prevent instantiation

    /**
     * List of risk-related terms to be detected in patient notes.
     * <p>
     * Each term can be a plain word or a regex pattern to capture variations.
     * Examples:
     * - "(?:fum(?:e(?:ur|use)|er))" matches "fumeur", "fumeuse", or "fumer"
     * - "anormal(?:e)?" matches "anormal" or "anormale"
     */
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
