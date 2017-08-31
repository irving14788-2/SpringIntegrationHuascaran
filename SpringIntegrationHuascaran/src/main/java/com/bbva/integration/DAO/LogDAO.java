package com.bbva.integration.DAO;

import com.bbva.integration.exceptions.DBException;

public interface LogDAO {
	
	void guardarLog(String metodo, String idTransaccion, String descripcionError) throws DBException;
	
}
