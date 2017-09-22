package com.bbva.integration.bean;

import java.math.BigInteger;

public class TfindimProcesoSubtarea {

	
	private static final long serialVersionUID = 1L;
    private String idSubtarea;
    private String idTarea;
    private String nbSubtarea;
    private String stTarea;
    private String dtSubtarea;
    private String idPredecesora;

    public TfindimProcesoSubtarea() {
    }

    public TfindimProcesoSubtarea(String idSubtarea) {
        this.idSubtarea = idSubtarea;
    }

    public TfindimProcesoSubtarea(String idSubtarea, String idTarea) {
        this.idSubtarea = idSubtarea;
        this.idTarea = idTarea;
    }

    public String getIdSubtarea() {
        return idSubtarea;
    }

    public void setIdSubtarea(String idSubtarea) {
        this.idSubtarea = idSubtarea;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    public String getNbSubtarea() {
        return nbSubtarea;
    }

    public void setNbSubtarea(String nbSubtarea) {
        this.nbSubtarea = nbSubtarea;
    }

    public String getStTarea() {
        return stTarea;
    }

    public void setStTarea(String stTarea) {
        this.stTarea = stTarea;
    }

    public String getDtSubtarea() {
        return dtSubtarea;
    }

    public void setDtSubtarea(String dtSubtarea) {
        this.dtSubtarea = dtSubtarea;
    }


    public String getIdPredecesora() {
		return idPredecesora;
	}

	public void setIdPredecesora(String idPredecesora) {
		this.idPredecesora = idPredecesora;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idSubtarea != null ? idSubtarea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TfindimProcesoSubtarea)) {
            return false;
        }
        TfindimProcesoSubtarea other = (TfindimProcesoSubtarea) object;
        if ((this.idSubtarea == null && other.idSubtarea != null) || (this.idSubtarea != null && !this.idSubtarea.equals(other.idSubtarea))) {
            return false;
        }
        return true;
    }
}
