package com.bbva.integration.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bbva.integration.exceptions.DBException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.apache.log4j.Logger;

@Repository
public class LogDAOImpl implements LogDAO {
	
	@Autowired
    @Qualifier("jdbcFINDIM")
    private JdbcTemplate jdbcFINDIM;

	private Logger logger = Logger.getLogger(LogDAOImpl.class.getName());
	
	
	@Override
	public void guardarLog(String metodo,String idTransaccion, String descripcionError)  throws DBException  {
		
		String query = "insert into logIntegracion (idTransaccion,descripcionError)";
		
		logger.info(metodo + " " + idTransaccion + " query " + query);
		logger.info(metodo + " " + idTransaccion + " " + descripcionError);
		try{
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("idTransaccion", idTransaccion);
		parameters.addValue("descripcionError", descripcionError);
		
		NamedParameterJdbcTemplate namedParameterJdbcTemplate= new NamedParameterJdbcTemplate(jdbcFINDIM);
		namedParameterJdbcTemplate.update(query,parameters);
		}catch(RuntimeException e){
			throw new DBException(e.getMessage());
		}
	}

}
