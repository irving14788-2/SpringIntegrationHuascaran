/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbva.integration.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ppazos
 */
public class TfindimProcesoBatchLogDt implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal idProcesoDt;
    private String tarea;
    private String paso;
    private String idProceso;
    private String nombreArchivo;
    private String obsEstado;

    public TfindimProcesoBatchLogDt() {
    }

    public TfindimProcesoBatchLogDt(BigDecimal idProcesoDt) {
        this.idProcesoDt = idProcesoDt;
    }

    public BigDecimal getIdProcesoDt() {
        return idProcesoDt;
    }

    public void setIdProcesoDt(BigDecimal idProcesoDt) {
        this.idProcesoDt = idProcesoDt;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getPaso() {
        return paso;
    }

    public void setPaso(String paso) {
        this.paso = paso;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }
    

    public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getObsEstado() {
		return obsEstado;
	}

	public void setObsEstado(String obsEstado) {
		this.obsEstado = obsEstado;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idProcesoDt != null ? idProcesoDt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TfindimProcesoBatchLogDt)) {
            return false;
        }
        TfindimProcesoBatchLogDt other = (TfindimProcesoBatchLogDt) object;
        if ((this.idProcesoDt == null && other.idProcesoDt != null) || (this.idProcesoDt != null && !this.idProcesoDt.equals(other.idProcesoDt))) {
            return false;
        }
        return true;
    }
    
}
