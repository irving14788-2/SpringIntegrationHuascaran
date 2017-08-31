package com.bbva.integration.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "contrato")
public class ContratosBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codContrato;
	private String descContrato;
	public String getCodContrato() {
		return codContrato;
	}
	public void setCodContrato(String codContrato) {
		this.codContrato = codContrato;
	}
	public String getDescContrato() {
		return descContrato;
	}
	public void setDescContrato(String descContrato) {
		this.descContrato = descContrato;
	}	
	
}
