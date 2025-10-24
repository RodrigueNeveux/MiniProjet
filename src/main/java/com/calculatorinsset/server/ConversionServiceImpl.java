package com.calculatorinsset.server;

import com.calculatorinsset.shared.ConversionService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConversionServiceImpl extends RemoteServiceServlet implements ConversionService {

    // Constantes pour la conversion Romain (Map utilisée par fromRoman)
    private static final Map<Character, Integer> ROMAN_MAP = new HashMap<>();
    static {
        ROMAN_MAP.put('I', 1);
        ROMAN_MAP.put('V', 5);
        ROMAN_MAP.put('X', 10);
        ROMAN_MAP.put('L', 50);
        ROMAN_MAP.put('C', 100);
        ROMAN_MAP.put('D', 500);
        ROMAN_MAP.put('M', 1000);
    }
    
    // Constantes pour la conversion Romain (Arrays utilisés par toRoman)
    private static final String[] ROMAN_NUMERALS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final int[] ROMAN_VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};


    // =========================================================================
    // LOGIQUE INTERNE DE CONVERSION ROMAIN
    // =========================================================================

    private String toRoman(int num) throws IllegalArgumentException {
        if (num < 1 || num > 3999) {
            throw new IllegalArgumentException("Erreur Interne: La valeur est hors de la plage de conversion romain [1, 3999].");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROMAN_VALUES.length; i++) {
            while (num >= ROMAN_VALUES[i]) {
                sb.append(ROMAN_NUMERALS[i]);
                num -= ROMAN_VALUES[i];
            }
        }
        return sb.toString();
    }

    private int fromRoman(String romain) throws IllegalArgumentException {
        int result = 0;
        int prevValue = 0;
        
        // --- Vérification de type (Chiffre vs. Lettre/Entier) ---
        String cleanRoman = romain.toUpperCase().trim();
        if (cleanRoman.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Erreur de type : Vous devez entrer un CHIFFRE ROMAIN (ex: XIX), pas un entier ou des chiffres.");
        }
        
        // Validation des caractères romains
        if (!cleanRoman.matches("^[IVXLCDM]+$")) {
            throw new IllegalArgumentException("Le format romain est invalide. Veuillez utiliser uniquement les caractères I, V, X, L, C, D, M.");
        }

        for (int i = cleanRoman.length() - 1; i >= 0; i--) {
            if (!ROMAN_MAP.containsKey(cleanRoman.charAt(i))) {
                throw new IllegalArgumentException("Caractère romain non reconnu.");
            }
            int currentValue = ROMAN_MAP.get(cleanRoman.charAt(i));
            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }
            prevValue = currentValue;
        }
        return result;
    }

    // =========================================================================
    // MÉTHODES PUBLIQUES (Implémentation du Service)
    // =========================================================================

    @Override
    public String convertirRomainEnEntier(String romain) throws IllegalArgumentException {
        // fromRoman gère déjà la vérification de type (Romain vs Entier)
        int entier = fromRoman(romain);
        
        if (entier < 8) {
            throw new IllegalArgumentException("La valeur est trop petite. La limite minimale est 8.");
        } else if (entier > 2000) {
            throw new IllegalArgumentException("La valeur est trop grande. La limite maximale est 2000.");
        }
        
        return "Résultat Entier : " + entier;
    }

    @Override
    public String convertirEntierEnRomain(int entier) throws IllegalArgumentException {
        // Note: L'appelant (client) doit déjà s'assurer que c'est bien un entier.
        
        if (entier < 8) {
            throw new IllegalArgumentException("Le nombre est trop petit. La limite minimale est 8.");
        } else if (entier > 2000) {
            throw new IllegalArgumentException("Le nombre est trop grand. La limite maximale est 2000.");
        }
        String romain = toRoman(entier);
        return "Résultat Romain : " + romain;
    }

    @Override
    public String convertirDateEnRomain(String dateNaissance) throws IllegalArgumentException {
        // Format attendu JJ/MM/AAAA
        Pattern pattern = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
        Matcher matcher = pattern.matcher(dateNaissance.trim());

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Erreur de format : La date doit être au format JJ/MM/AAAA (ex: 01/01/2000).");
        }
        
        try {
            int jour = Integer.parseInt(matcher.group(1));
            int mois = Integer.parseInt(matcher.group(2));
            int annee = Integer.parseInt(matcher.group(3));
            
            // Vérification du Jour et Mois (Nombres négatifs, Zéro ou trop grands)
            if (jour < 1 || jour > 31) {
                throw new IllegalArgumentException("Le jour est invalide. Il doit être entre 1 et 31.");
            }
            if (mois < 1 || mois > 12) {
                throw new IllegalArgumentException("Le mois est invalide. Il doit être entre 1 et 12.");
            }
            
            // Vérification des limites d'Année
            if (annee < 8) {
                throw new IllegalArgumentException("L'année est trop petite. La limite minimale est 8.");
            } else if (annee > 2000) {
                throw new IllegalArgumentException("L'année est trop grande. La limite maximale est 2000.");
            }
            
            String romainJour = toRoman(jour);
            String romainMois = toRoman(mois);
            String romainAnnee = toRoman(annee);
            
            return "Date en Romain : " + romainJour + "/" + romainMois + "/" + romainAnnee;
            
        } catch (NumberFormatException e) {
            // Cette erreur ne devrait pas se produire si le Pattern est correct, mais elle est là par sécurité.
            throw new IllegalArgumentException("Erreur interne lors de l'extraction des nombres de la date.");
        }
    }

    // --- IHM 2 : Calculs de Remise ---

    @Override
    public String calculerDivision(int a, int b) throws IllegalArgumentException {
        // La vérification de type (lettre vs nombre) est gérée côté client.
        if (b == 0) {
            throw new IllegalArgumentException("Erreur de calcul : La division par zéro est impossible.");
        }
        double resultat = (double) a / b;
        return "Division : " + a + " / " + b + " = " + String.format("%.2f", resultat);
    }

    @Override
    public String calculerRemise(double prixInitial, double pourcentageRemise) throws IllegalArgumentException {
        // La vérification de type (lettre vs nombre) est gérée côté client.
        if (pourcentageRemise < 0 || pourcentageRemise > 100) {
             throw new IllegalArgumentException("Le pourcentage de remise doit être entre 0 et 100.");
        }
        
        double remiseMontant = prixInitial * (pourcentageRemise / 100.0);
        double prixFinal = prixInitial - remiseMontant;
        
        String resultat = "Remise (Entier) : " + (int) remiseMontant + "\n";
        resultat += "Remise (Décimal) : " + String.format("%.2f", remiseMontant) + "\n";
        resultat += "Bénéfice (Remise) : " + String.format("%.2f", remiseMontant) + "\n"; 
        resultat += "Prix Final : " + String.format("%.2f", prixFinal);
        
        return resultat;
    }
}