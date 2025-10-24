package com.calculatorinsset.client;

import com.calculatorinsset.shared.ConversionService;
import com.calculatorinsset.shared.ConversionServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class CalculatorInsset implements EntryPoint {

    private final ConversionServiceAsync conversionService = GWT.create(ConversionService.class);

    // Widgets IHM (déclarations)
    private TabPanel tabPanel = new TabPanel(); 
    
    // Panneaux pour les onglets
    private VerticalPanel conversionPanel = new VerticalPanel();
    private VerticalPanel calculPanel = new VerticalPanel();
    
    // 1. Conversions
    private TextBox inputRomain = new TextBox();
    private TextBox inputEntier = new TextBox();
    private TextBox inputDate = new TextBox();
    private Button btnConvertirRomain = new Button("Romain -> Entier");
    private Button btnConvertirEntier = new Button("Entier -> Romain");
    private Button btnConvertirDate = new Button("Date (JJ/MM/AAAA) -> Romain");
    private Button btnClearConversion = new Button("Clear Conversions"); 
    private Label errorLabelRomain = new Label();
    private Label errorLabelEntier = new Label();
    private Label errorLabelDate = new Label();
    
    // 2. Calculs
    private TextBox inputDivA = new TextBox();
    private TextBox inputDivB = new TextBox();
    private TextBox inputPrixInitial = new TextBox();
    private TextBox inputPourcentage = new TextBox();
    private Button btnDivision = new Button("Faire la Division");
    private Button btnRemise = new Button("Calculer la Remise");
    private Label errorLabelDivision = new Label();
    private Label errorLabelRemise = new Label();
    
    private Button btnClear = new Button("Clear All");

    @Override
    public void onModuleLoad() {
        
        // --- Configuration Générale ---
        conversionPanel.setSpacing(10);
        calculPanel.setSpacing(10);
        
        // Configuration des labels d'erreur en rouge
        errorLabelRomain.setStyleName("errorLabel");
        errorLabelEntier.setStyleName("errorLabel");
        errorLabelDate.setStyleName("errorLabel");
        errorLabelDivision.setStyleName("errorLabel");
        errorLabelRemise.setStyleName("errorLabel");

        // Configuration des Placeholders
        inputRomain.getElement().setPropertyString("placeholder", "Entrer une valeur");
        inputEntier.getElement().setPropertyString("placeholder", "Entrer une valeur");
        inputDate.getElement().setPropertyString("placeholder", "JJ/MM/AAAA"); 
        inputDivA.getElement().setPropertyString("placeholder", "Entrer une valeur");
        inputDivB.getElement().setPropertyString("placeholder", "Entrer une valeur");
        inputPrixInitial.getElement().setPropertyString("placeholder", "Entrer une valeur");
        inputPourcentage.getElement().setPropertyString("placeholder", "Entrer une valeur");


        // =========================================================================
        // A. CONSTRUCTION DU PANNEAU DE CONVERSIONS
        // =========================================================================
        conversionPanel.add(new HTML("<h2>Conversions (Limites: [8, 2000])</h2>"));

        // Romain -> Entier
        conversionPanel.add(new Label("1. Convertir Romain vers Entier :"));
        conversionPanel.add(inputRomain);
        conversionPanel.add(errorLabelRomain); 
        conversionPanel.add(btnConvertirRomain);
        
        // Entier -> Romain
        conversionPanel.add(new Label("2. Convertir Entier vers Romain :"));
        conversionPanel.add(inputEntier);
        conversionPanel.add(errorLabelEntier);
        conversionPanel.add(btnConvertirEntier);

        // Date -> Romain
        conversionPanel.add(new Label("3. Convertir Date vers Romain :"));
        conversionPanel.add(inputDate);
        conversionPanel.add(errorLabelDate);
        conversionPanel.add(btnConvertirDate);
        
        // Bouton Clear Spécifique à cette IHM
        conversionPanel.add(btnClearConversion);
        conversionPanel.add(new HTML("<hr>"));


        // =========================================================================
        // B. CONSTRUCTION DU PANNEAU DE CALCULS
        // =========================================================================
        calculPanel.add(new HTML("<h2>Calculs (Division et Remise)</h2>"));

        // Division
        calculPanel.add(new Label("1. Division (A / B) :"));
        HorizontalPanel divPanel = new HorizontalPanel();
        divPanel.add(inputDivA);
        divPanel.add(new Label(" / "));
        divPanel.add(inputDivB);
        calculPanel.add(divPanel);
        calculPanel.add(errorLabelDivision);
        calculPanel.add(btnDivision);

        // Remise
        calculPanel.add(new Label("2. Calculer Remise (Prix - %) :"));
        HorizontalPanel remisePanel = new HorizontalPanel();
        remisePanel.add(new Label("Prix Initial: "));
        remisePanel.add(inputPrixInitial);
        remisePanel.add(new Label(" - Pourcentage: "));
        remisePanel.add(inputPourcentage);
        remisePanel.add(new Label("%"));
        calculPanel.add(remisePanel);
        calculPanel.add(errorLabelRemise);
        calculPanel.add(btnRemise);
        
        calculPanel.add(new HTML("<hr>"));
        calculPanel.add(btnClear);
        
        
        // =========================================================================
        // C. CONFIGURATION DU TABPANEL (NAVBAR)
        // =========================================================================
        tabPanel.add(conversionPanel, "1. Conversions");
        tabPanel.add(calculPanel, "2. Calculs & Remises");
        tabPanel.selectTab(0); 
        tabPanel.setWidth("700px");

        // =========================================================================
        // D. LOGIQUE DES BOUTONS (Click Handlers)
        // =========================================================================
        btnConvertirRomain.addClickHandler(e -> convertirRomain());
        btnConvertirEntier.addClickHandler(e -> convertirEntier());
        btnConvertirDate.addClickHandler(e -> convertirDate());
        btnDivision.addClickHandler(e -> faireDivision());
        btnRemise.addClickHandler(e -> calculerRemise());
        
        btnClearConversion.addClickHandler(e -> clearConversionActions()); 
        btnClear.addClickHandler(e -> clearAllActions());

        // Attacher le panneau de navigation à la page HTML
        RootPanel.get("mainContainer").add(tabPanel);
    }
    
    // --- CONVERSION LOGIC ---

    private void convertirRomain() {
        errorLabelRomain.setText("");
        String romain = inputRomain.getText().trim();
        if (romain.isEmpty()) { errorLabelRomain.setText("Veuillez entrer une valeur."); return; }

        conversionService.convertirRomainEnEntier(romain, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) { errorLabelRomain.setText(caught.getMessage()); }
            @Override
            public void onSuccess(String result) { Window.alert(result); }
        });
    }

    private void convertirEntier() {
        errorLabelEntier.setText("");
        String entierStr = inputEntier.getText().trim();
        int entier;
        
        if (entierStr.isEmpty()) { errorLabelEntier.setText("Veuillez entrer une valeur."); return; }
        
        try {
            entier = Integer.parseInt(entierStr);
        } catch (NumberFormatException e) {
            errorLabelEntier.setText("Veuillez entrer un nombre entier valide.");
            return;
        }

        conversionService.convertirEntierEnRomain(entier, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) { errorLabelEntier.setText(caught.getMessage()); }
            @Override
            public void onSuccess(String result) { Window.alert(result); }
        });
    }

    private void convertirDate() {
        errorLabelDate.setText("");
        String date = inputDate.getText().trim();
        if (date.isEmpty()) { errorLabelDate.setText("Veuillez entrer une valeur."); return; }

        conversionService.convertirDateEnRomain(date, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) { errorLabelDate.setText(caught.getMessage()); }
            @Override
            public void onSuccess(String result) { Window.alert(result); }
        });
    }

    // --- CALCUL LOGIC ---

    private void faireDivision() {
        errorLabelDivision.setText("");
        String aStr = inputDivA.getText().trim();
        String bStr = inputDivB.getText().trim();
        int a, b;

        if (aStr.isEmpty() || bStr.isEmpty()) { errorLabelDivision.setText("Veuillez entrer les deux valeurs."); return; }

        try {
            a = Integer.parseInt(aStr);
            b = Integer.parseInt(bStr);
        } catch (NumberFormatException e) {
            errorLabelDivision.setText("Veuillez entrer des nombres entiers valides.");
            return;
        }

        conversionService.calculerDivision(a, b, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) { errorLabelDivision.setText(caught.getMessage()); }
            @Override
            public void onSuccess(String result) { Window.alert(result); }
        });
    }
    
    private void calculerRemise() {
        errorLabelRemise.setText("");
        String prixStr = inputPrixInitial.getText().trim();
        String pctStr = inputPourcentage.getText().trim();
        double prix, pct;

        if (prixStr.isEmpty() || pctStr.isEmpty()) { errorLabelRemise.setText("Veuillez entrer les deux valeurs."); return; }

        try {
            prix = Double.parseDouble(prixStr);
            pct = Double.parseDouble(pctStr);
        } catch (NumberFormatException e) {
            errorLabelRemise.setText("Veuillez entrer des valeurs numériques valides.");
            return;
        }

        conversionService.calculerRemise(prix, pct, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) { errorLabelRemise.setText(caught.getMessage()); }
            @Override
            public void onSuccess(String result) { Window.alert(result); }
        });
    }

    // --- CLEAR LOGIC ---

    private void clearConversionActions() {
        // Vider les champs de la première IHM
        inputRomain.setText("");
        inputEntier.setText("");
        inputDate.setText("");
        
        errorLabelRomain.setText("");
        errorLabelEntier.setText("");
        errorLabelDate.setText("");
        
        Window.alert("Les champs de conversion et les messages d'erreur ont été effacés.");
    }
    
    private void clearAllActions() {
        clearConversionActions();
        
        // Vider les champs des calculs
        inputDivA.setText("");
        inputDivB.setText("");
        inputPrixInitial.setText("");
        inputPourcentage.setText("");
        
        errorLabelDivision.setText("");
        errorLabelRemise.setText("");
        
        Window.alert("TOUS les champs et les messages d'erreur ont été effacés.");
    }
}