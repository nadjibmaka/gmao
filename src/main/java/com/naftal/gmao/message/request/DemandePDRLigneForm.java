package com.naftal.gmao.message.request;

import lombok.Data;

@Data
public class DemandePDRLigneForm {


    private Long idLigne;


    private Long idPdr ;

    private int quantiteDemande;
    private int quantiteAccorde;
    private int quantiteUtilise;


}
