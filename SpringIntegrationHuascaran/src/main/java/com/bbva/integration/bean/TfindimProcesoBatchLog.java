/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbva.integration.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ppazos
 */
public class TfindimProcesoBatchLog implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal idProceso;
    private String cdProceso;
    private String stProceso;
    private String obProceso;
    private String idTpProceso;
    private Date fhIniProceso;
    private Date fhFinProceso;

    public TfindimProcesoBatchLog() {
    }

    public TfindimProcesoBatchLog(BigDecimal idProceso) {
        this.idProceso = idProceso;
    }

    public BigDecimal getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(BigDecimal idProceso) {
        this.idProceso = idProceso;
    }

    public String getCdProceso() {
        return cdProceso;
    }

    public void setCdProceso(String cdProceso) {
        this.cdProceso = cdProceso;
    }

    public String getStProceso() {
        return stProceso;
    }

    public void setStProceso(String stProceso) {
        this.stProceso = stProceso;
    }

    public String getObProceso() {
        return obProceso;
    }

    public void setObProceso(String obProceso) {
        this.obProceso = obProceso;
    }

    public String getIdTpProceso() {
        return idTpProceso;
    }

    public void setIdTpProceso(String idTpProceso) {
        this.idTpProceso = idTpProceso;
    }

    public Date getFhIniProceso() {
        return fhIniProceso;
    }

    public void setFhIniProceso(Date fhIniProceso) {
        this.fhIniProceso = fhIniProceso;
    }

    public Date getFhFinProceso() {
        return fhFinProceso;
    }

    public void setFhFinProceso(Date fhFinProceso) {
        this.fhFinProceso = fhFinProceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProceso != null ? idProceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TfindimProcesoBatchLog)) {
            return false;
        }
        TfindimProcesoBatchLog other = (TfindimProcesoBatchLog) object;
        if ((this.idProceso == null && other.idProceso != null) || (this.idProceso != null && !this.idProceso.equals(other.idProceso))) {
            return false;
        }
        return true;
    }

    
}
