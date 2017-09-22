package com.bbva.integration.DAO;

import java.util.Date;

import com.bbva.integration.bean.TfindimProcesoBatchLog;
import com.bbva.integration.bean.TfindimProcesoBatchLogDt;
import com.bbva.integration.bean.TfindimProcesoSubtarea;
import com.bbva.integration.bean.TfindimProcesoTarea;
import com.bbva.integration.exceptions.DBException;

public interface LogDAO {
	
	void guardarLog(TfindimProcesoBatchLog tfindimProcesoBatchLog) throws DBException;
	boolean validarInsertLog(String idFecha);
	void actualizarLog(TfindimProcesoBatchLog tfindimProcesoBatchLog);
	
	void guardarDetalleLog(TfindimProcesoBatchLogDt tfindimProcesoBatchLogDt);
	boolean validarInsertDetalleLog(String idFechaLog,String tarea);
	
	TfindimProcesoTarea obtenerDatosTarea(String idTarea);
	
	TfindimProcesoSubtarea obtenerDatosSubTarea(String idSubtarea);
}
