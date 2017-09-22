package com.bbva.integration.bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author ppazos
 */
public class TfindimProcesoTarea implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal idTarea;
    private String nbTarea;
    private String stTarea;
    private String fhIniTarea;
    private String fhFinTarea;
    private String cdTarea;
    private BigDecimal idPredecesora;

    public TfindimProcesoTarea() {
    }

    public TfindimProcesoTarea(BigDecimal idTarea) {
        this.idTarea = idTarea;
    }

    public BigDecimal getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(BigDecimal idTarea) {
        this.idTarea = idTarea;
    }

    public String getNbTarea() {
        return nbTarea;
    }

    public void setNbTarea(String nbTarea) {
        this.nbTarea = nbTarea;
    }

    public String getStTarea() {
        return stTarea;
    }

    public void setStTarea(String stTarea) {
        this.stTarea = stTarea;
    }

    public String getCdTarea() {
        return cdTarea;
    }

    public void setCdTarea(String cdTarea) {
        this.cdTarea = cdTarea;
    }


    public BigDecimal getIdPredecesora() {
		return idPredecesora;
	}

	public void setIdPredecesora(BigDecimal idPredecesora) {
		this.idPredecesora = idPredecesora;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idTarea != null ? idTarea.hashCode() : 0);
        return hash;
    }
    


	public String getFhIniTarea() {
		return fhIniTarea;
	}

	public void setFhIniTarea(String fhIniTarea) {
		this.fhIniTarea = fhIniTarea;
	}

	public String getFhFinTarea() {
		return fhFinTarea;
	}

	public void setFhFinTarea(String fhFinTarea) {
		this.fhFinTarea = fhFinTarea;
	}

	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TfindimProcesoTarea)) {
            return false;
        }
        TfindimProcesoTarea other = (TfindimProcesoTarea) object;
        if ((this.idTarea == null && other.idTarea != null) || (this.idTarea != null && !this.idTarea.equals(other.idTarea))) {
            return false;
        }
        return true;
    }

    
}
