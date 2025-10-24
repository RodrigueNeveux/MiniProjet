package com.calculatorinsset.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConversionServiceAsync {

    // IHM 1
    void convertirRomainEnEntier(String romain, AsyncCallback<String> callback);
    void convertirEntierEnRomain(int entier, AsyncCallback<String> callback);
    void convertirDateEnRomain(String dateNaissance, AsyncCallback<String> callback);

    // IHM 2
    void calculerDivision(int a, int b, AsyncCallback<String> callback);
    void calculerRemise(double prixInitial, double pourcentageRemise, AsyncCallback<String> callback);
}