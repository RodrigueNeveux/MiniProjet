package com.calculatorinsset.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("conversion") // Correspond au mapping dans web.xml
public interface ConversionService extends RemoteService {

    // IHM 1 : Conversions Chiffres Romains
    String convertirRomainEnEntier(String romain) throws IllegalArgumentException;
    String convertirEntierEnRomain(int entier) throws IllegalArgumentException;
    String convertirDateEnRomain(String dateNaissance) throws IllegalArgumentException;

    // IHM 2 : Calculs de Remise
    String calculerDivision(int a, int b) throws IllegalArgumentException;
    String calculerRemise(double prixInitial, double pourcentageRemise) throws IllegalArgumentException;
}