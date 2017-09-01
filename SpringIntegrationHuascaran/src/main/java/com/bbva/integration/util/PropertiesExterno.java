package com.bbva.integration.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExterno {
	
	@Value( "${spring.datasource.findim.url}" )
	public String	URL_DATASOURCE_FINDIM;
	
	@Value( "${spring.datasource.findim.username}" )
	public String	USERNAME_DATASOURCE_FINDIM;
	
	@Value( "${spring.datasource.findim.password}" )
	public String	PASSWORD_DATASOURCE_FINDIM;
	
	@Value( "${spring.datasource.findim.driver.class.name}" )
	public String	DRIVER_DATASOURCE_FINDIM;
	
	@Value( "${directorio.destino.descifrado.consolidado}" )
	public String DIRECTORIO_DESTINO_DESCIFRADO_CONSOLIDADO;
	
	//********************************************************
	@Value( "${directorio.origen.telefonica.cifrados.ACBCCF}" )
	public String DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_ACBCCF;
	
	@Value( "${directorio.destino.telefonica.cifrado.AOBCCF}" )
	public String DIRECTORIO_DESTINO_TELEFONICA_CIFRADO_AOBCCF;
	
	@Value( "${entrada.archivo.telefonica.ACBCCF}" )
	public String ENTRADA_ARCHIVO_TELEFONICA_ACNCCF;
	
	@Value( "${salida.archivo.telefonica.AOBCCF}" )
	public String SALIDA_ARCHIVO_TELEFONICA_ACNCCF;
	
	//********************************************************
	@Value( "${directorio.origen.telefonica.cifrados.CFBCCF}" )
	public String DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_CFBCCF;
	
	@Value( "${directorio.destino.telefonica.cifrado.CVBCCF}" )
	public String DIRECTORIO_DESTINO_TELEFONICA_CIFRADO_CVBCCF;
	
	@Value( "${entrada.archivo.telefonica.CFBCCF}" )
	public String ENTRADA_ARCHIVO_TELEFONICA_CFBCCF;
	
	@Value( "${salida.archivo.telefonica.CVBCCF}" )
	public String SALIDA_ARCHIVO_TELEFONICA_CVBCCF;
	
	//********************************************************
	@Value( "${directorio.origen.telefonica.cifrados.CRBCCF}" )
	public String DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_CRBCCF;
	
	@Value( "${directorio.destino.telefonica.cifrado.FCBCCF}" )
	public String DIRECTORIO_DESTINO_TELEFONICA_CIFRADO_FCBCCF;

	@Value( "${entrada.archivo.telefonica.CRBCCF}" )
	public String ENTRADA_ARCHIVO_TELEFONICA_CRBCCF;
	
	@Value( "${salida.archivo.telefonica.FCBCCF}" )
	public String SALIDA_ARCHIVO_TELEFONICA_FCBCCF;
	
	//********************************************************
	@Value( "${directorio.origen.telefonica.cifrados.RDBCCF}" )
	public String DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_RDBCCF;
	
	@Value( "${directorio.destino.telefonica.cifrado.LLCBCCF}" )
	public String DIRECTORIO_DESTINO_TELEFONICA_CIFRADO_LLCBCCF;

	@Value( "${entrada.archivo.telefonica.RDBCCF}" )
	public String ENTRADA_ARCHIVO_TELEFONICA_RDBCCF;
	
	@Value( "${salida.archivo.telefonica.LLCBCCF}" )
	public String SALIDA_ARCHIVO_TELEFONICA_LLCBCCF;
	//********************************************************

	@Value( "${directorio.contratos}" )
	public String DIRECTORIO_CONTRATOS;
	
	@Value( "${directorio.contratos.fu}" )
	public String DIRECTORIO_CONTRATOS_FU;
	
	@Value( "${directorio.origen.host}" )
	public String DIRECTORIO_ORIGEN_HOST;
	
	@Value( "${directorio.llave.privada}" )
	public String DIRECTORIO_LLAVE_PRIVADA;
	
	@Value( "${directorio.llave.publica}" )
	public String DIRECTORIO_LLAVE_PUBLICA;
	
	@Value( "${password.llave}" )
	public String PASSWORD_LLAVE;
	
	@Value( "${directorio.temp.cifrado}" )
	public String DIRECTORIO_TEMP_CIFRADO;
		
	@Value( "${leer.intervalo.minutos}" )
	public int	LEER_INTERVLO_MINUTOS;
}
