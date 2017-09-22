package com.bbva.integration.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.bbva.integration.bean.TfindimProcesoBatchLog;
import com.bbva.integration.bean.TfindimProcesoBatchLogDt;
import com.bbva.integration.bean.TfindimProcesoSubtarea;
import com.bbva.integration.bean.TfindimProcesoTarea;
import com.bbva.integration.exceptions.DBException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

@Repository
public class LogDAOImpl implements LogDAO {
	
	@Autowired
    @Qualifier("jdbcFINDIM")
    private JdbcTemplate jdbcFINDIM;

	private Logger logger = Logger.getLogger(LogDAOImpl.class.getName());
	
	@SuppressWarnings("unchecked")
	@Override
	public void guardarLog(TfindimProcesoBatchLog tfindimProcesoBatchLog)  throws DBException  {
		String query = "INSERT INTO TFINDIM_PROCESO_BATCH_LOG (ID_PROCESO,CD_PROCESO,ST_PROCESO,OB_PROCESO,FH_INI_PROCESO,FH_FIN_PROCESO)"
				+ " VALUES (:idProceso,:cdProceso,:stProceso,:obProceso,:fhIniProceso,:fhFinProceso)";
//		logger.info(metodo + " " + idTransaccion + " query " + query);
//		logger.info(metodo + " " + idTransaccion + " " + descripcionError);
		try{
		@SuppressWarnings("rawtypes")
		Map parameters = new HashMap();
		parameters.put("idProceso", tfindimProcesoBatchLog.getIdProceso());
		parameters.put("cdProceso", tfindimProcesoBatchLog.getCdProceso());
		parameters.put("stProceso", tfindimProcesoBatchLog.getStProceso());
		parameters.put("obProceso", tfindimProcesoBatchLog.getObProceso());
		parameters.put("fhIniProceso", tfindimProcesoBatchLog.getFhIniProceso());
		parameters.put("fhFinProceso", tfindimProcesoBatchLog.getFhFinProceso());
		
		NamedParameterJdbcTemplate namedParameterJdbcTemplate= new NamedParameterJdbcTemplate(jdbcFINDIM);
		namedParameterJdbcTemplate.update(query,parameters);
		
		}catch(RuntimeException e){
			throw new DBException(e.getMessage());
		}
	}

	
	@Override
	public boolean validarInsertLog(String idFecha) {
		// TODO Auto-generated method stub
		String query ="SELECT cd_proceso FROM TFINDIM_PROCESO_BATCH_LOG WHERE CD_PROCESO = '"+ idFecha + "'";
		 List<String> logs = jdbcFINDIM.queryForList(query, String.class); 
		 if (logs.isEmpty()) {
		        return true;
		    } else {
		        return false;
		    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public void guardarDetalleLog(TfindimProcesoBatchLogDt tfindimProcesoBatchLogDt) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO TFINDIM_PROCESO_BATCH_LOG_DT(TAREA,PASO,ID_PROCESO,NOMBRE_ARCHIVO,OBS_ESTADO)"
				+ " VALUES (:tarea,:paso,:idProceso,:nombreArchivo,:obsEstado)";
//		logger.info(metodo + " " + idTransaccion + " query " + query);
//		logger.info(metodo + " " + idTransaccion + " " + descripcionError);
		@SuppressWarnings("rawtypes")
		Map parameters = new HashMap();
		parameters.put("tarea", tfindimProcesoBatchLogDt.getTarea());
		parameters.put("paso", tfindimProcesoBatchLogDt.getPaso());
		parameters.put("idProceso", tfindimProcesoBatchLogDt.getIdProceso());
		parameters.put("nombreArchivo", tfindimProcesoBatchLogDt.getNombreArchivo());
		parameters.put("obsEstado", tfindimProcesoBatchLogDt.getObsEstado());
		
		NamedParameterJdbcTemplate namedParameterJdbcTemplate= new NamedParameterJdbcTemplate(jdbcFINDIM);
		namedParameterJdbcTemplate.update(query,parameters);
		
	}

	@Override
	public boolean validarInsertDetalleLog(String idFechaLog, String tarea) {
		// TODO Auto-generated method stub
		String query ="SELECT ID_PROCESO_DT FROM TFINDIM_PROCESO_BATCH_LOG_DT WHERE ID_PROCESO = '"+ idFechaLog + "' and TAREA = '"+ tarea+"'";
		
		 List<String> logsdt = jdbcFINDIM.queryForList(query, String.class); 
		 if (logsdt.isEmpty()) {
		        return true;
		    } else {
		        return false;
		    }
		 
	}


	@Override
	public TfindimProcesoTarea obtenerDatosTarea(String idTarea) {
		// TODO Auto-generated method stub
		TfindimProcesoTarea tfindimProcesoTarea=null;
		try{
		tfindimProcesoTarea = jdbcFINDIM.queryForObject(
				"select id_tarea,nb_tarea,st_tarea,fh_ini_tarea,fh_fin_tarea,cd_tarea,id_predecesora"
				+ " from TFINDIM_PROCESO_TAREA WHERE cd_tarea= ? ",
			    new RowMapper<TfindimProcesoTarea>() {
					public TfindimProcesoTarea mapRow(ResultSet rs, int rowNum) throws SQLException {
						TfindimProcesoTarea beanTarea= new TfindimProcesoTarea();
						beanTarea.setIdTarea(rs.getBigDecimal("id_tarea"));
						beanTarea.setNbTarea(rs.getString("nb_tarea"));
						beanTarea.setStTarea(rs.getString("st_tarea"));
						beanTarea.setFhIniTarea(rs.getString("fh_ini_tarea"));
						beanTarea.setFhIniTarea(rs.getString("fh_fin_tarea"));
						beanTarea.setCdTarea(rs.getString("cd_tarea"));
						beanTarea.setIdPredecesora(rs.getBigDecimal("id_predecesora"));
						return beanTarea;
					}
				},
			    idTarea
			);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}	
		return tfindimProcesoTarea;
	}


	@Override
	public void actualizarLog(TfindimProcesoBatchLog tfindimProcesoBatchLog) {
		// TODO Auto-generated method stub
		String SQL = "UPDATE TFINDIM_PROCESO_BATCH_LOG SET ST_PROCESO = :stProceso WHERE CD_PROCESO = :cdProceso";
		  SqlParameterSource namedParameters = new MapSqlParameterSource();
		  ((MapSqlParameterSource) namedParameters).addValue("stProceso", tfindimProcesoBatchLog.getStProceso());
		  ((MapSqlParameterSource) namedParameters).addValue("cdProceso", tfindimProcesoBatchLog.getCdProceso());
		NamedParameterJdbcTemplate namedParameterJdbcTemplate= new NamedParameterJdbcTemplate(jdbcFINDIM);
		namedParameterJdbcTemplate.update(SQL, namedParameters);
		
	}


	@Override
	public TfindimProcesoSubtarea obtenerDatosSubTarea(String idSubtarea) {
		// TODO Auto-generated method stub
		TfindimProcesoSubtarea tfindimProcesoSubTarea=null;
		try{
			tfindimProcesoSubTarea = jdbcFINDIM.queryForObject(
				"select id_subtarea,id_tarea,nb_subtarea,st_tarea,dt_subtarea,id_predecesora"
				+ " from TFINDIM_PROCESO_SUBTAREA WHERE id_subtarea= ? ",
			    new RowMapper<TfindimProcesoSubtarea>() {
					public TfindimProcesoSubtarea mapRow(ResultSet rs, int rowNum) throws SQLException {
						TfindimProcesoSubtarea beanSubTarea= new TfindimProcesoSubtarea();
						beanSubTarea.setIdSubtarea(rs.getString("id_subtarea"));
						beanSubTarea.setIdTarea(rs.getString("id_tarea"));
						beanSubTarea.setNbSubtarea(rs.getString("nb_subtarea"));
						beanSubTarea.setStTarea(rs.getString("st_tarea"));
						beanSubTarea.setDtSubtarea(rs.getString("dt_subtarea"));
						beanSubTarea.setIdPredecesora(rs.getString("id_predecesora"));
						return beanSubTarea;
					}
				},
			    idSubtarea
			);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}	
		return tfindimProcesoSubTarea;
	}

}
