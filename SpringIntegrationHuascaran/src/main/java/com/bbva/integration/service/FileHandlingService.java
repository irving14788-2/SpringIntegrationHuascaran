package com.bbva.integration.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.bbva.integration.DAO.LogDAO;
import com.bbva.integration.bean.TfindimProcesoBatchLog;
import com.bbva.integration.bean.TfindimProcesoBatchLogDt;
import com.bbva.integration.bean.TfindimProcesoSubtarea;
import com.bbva.integration.bean.TfindimProcesoTarea;
import com.bbva.integration.exceptions.DBException;
import com.bbva.integration.util.KeyBasedFileProcessor;
import com.bbva.integration.util.PropertiesExterno;



@Component
public class FileHandlingService {
	
	 private static final Logger LOG = LoggerFactory.getLogger(FileHandlingService.class);
	
	@Autowired
	PropertiesExterno propertiesExterno;
	
	
	@Autowired
	LogDAO logDAO;
	
	@CorrelationStrategy
    public String correlateBy(@Header("id") String id) {
        return "1";
    }

	
	public File formatoCorreo(String lineaArchivo,List<String> nombreArchivos) throws IOException {
		String archivoCrear=propertiesExterno.DIRECTORIO_CONTRATOS+"\\nombreArchivoEnvioMotor.txt";
		File fichero = new File(archivoCrear);
    	String[] partes = lineaArchivo.split(Pattern.quote("|"));
		BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCrear));
		
		 bw.write("<R>J6J63CVTX2GK20170725010120             |PLT00000"+"\n");
		 bw.write("<T>Contrato Préstamo Personal ­ OCB Telefonía ­ "+partes[4]+"\n");
		 bw.write("<D>"+"teresa.gagliuffi@bbva.com"+"\n"); //TODO CAMBIAR POR CORREO CLIENTE
		 bw.write("<D>"+"oscar.caldas.villanueva@gmail.com"+"\n");   
		 bw.write("<O>"+"percy1409@gmail.com"+"\n"); 
		 
		 for (String string : nombreArchivos) {
			 bw.write("<A>"+string+    "| "+nombreArchivos.get(0)+                     "|PLT00000"+"\n");                                                                                                                
		 }
		 
		 bw.write("<M>Estimados Sres."+"\n");                                                                                                                                                                                          
		 bw.write("<M>"+"\n");                                                                                                                                                                                                         
		 bw.write("<M>Estimado(a): "+partes[4]+"\n");
		 bw.write("<M>Remitimos el Contrato Préstamo Personal ­ OCB Telefonía N° "+partes[1]+", junto con la Hoja Resumen Informativa"+"\n");
		 bw.write("<M>(HRI), Cronograma y Certificado del Seguro de Desgravamen correspondientes al financiamiento del equipo móvil"+"\n");
		 bw.write("<M>adquirido en Telefónica del Perú."+"\n");
		 bw.write("<M>Le recordamos las principales condiciones de su financiamiento:"+"\n");
		 bw.write("<M>. El capital de su crédito es de S/."+ partes[13] +"\n");
		 bw.write("<M>. Con "+partes[8]+" cuotas de S/."+partes[9]+" mensuales"+"\n");
		 bw.write("<M>. Disfrutando de una tasa de interés (TEA) del "+partes[15]+"% y una tasa de interés efectiva anual (TCEA) del "+partes[16]+"%."+"\n");
		 bw.write("<M>Para su comodidad, las cuotas a pagar serán incluidas en su recibo de teléfono y su pago se efectuará junto con el plan"+"\n");
		 bw.write("<M>telefónico adquirido."+"\n");
		 bw.write("<M>Atentamente,"+"\n");
		 bw.write("<M>BBVA CONTINENTAL"+"\n");
		 bw.write("<M>Dirección: Av. República de Panamá 3055 - San Isidro, Lima - Perú"+"\n");
		 bw.write("<M>"+"\n");  
		 

		 DateFormat hourdateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
		 Calendar calendar = Calendar.getInstance();
		 bw.write("<M>"+hourdateFormat.format(calendar.getTime())+"\n");   
		 bw.close();
		
		 return fichero;
	}
    @ReleaseStrategy
    public boolean release(List<Message<File>> messages) throws DBException {
    		Calendar fechaDia = Calendar.getInstance();
        	String cd_proceso=""+fechaDia.get(Calendar.YEAR)+(fechaDia.get(Calendar.MONTH) + 1)+fechaDia.get(Calendar.DATE);
        	if(logDAO.validarInsertLog(cd_proceso)) {
        		TfindimProcesoBatchLog batchLog = new TfindimProcesoBatchLog();
            	batchLog.setIdProceso(new BigDecimal(1));
            	batchLog.setCdProceso(cd_proceso);
            	batchLog.setIdTpProceso("TELEF");//TODO obten"+"\n");de properties
            	batchLog.setStProceso("EN PROCESO");//TODO obtener de properties
            	batchLog.setFhIniProceso(fechaDia.getTime());
            	logDAO.guardarLog(batchLog);
            	
        	}
        	String directory = "";
            String name = (messages.get(0)!=null)?messages.get(0).getPayload().getName():"";
            
            if(name.startsWith(propertiesExterno.ENTRADA_ARCHIVO_TELEFONICA_ACNCCF)) {
            	directory = propertiesExterno.DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_ACBCCF;
            }else if(name.startsWith(propertiesExterno.ENTRADA_ARCHIVO_TELEFONICA_CFBCCF)) {
            	directory = propertiesExterno.DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_CFBCCF;
            }else if(name.startsWith(propertiesExterno.ENTRADA_ARCHIVO_TELEFONICA_CRBCCF)) {
            	directory = propertiesExterno.DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_CRBCCF;
            }else if(name.startsWith(propertiesExterno.ENTRADA_ARCHIVO_TELEFONICA_RDBCCF)) {
            	directory = propertiesExterno.DIRECTORIO_ORIGEN_TELEFONICA_CIFRADOS_RDBCCF;
            }
            
        	boolean b = validarCantidadArchivos(directory,messages.size());
        	return b;
    }
    
    public boolean validarCantidadArchivos(String directorio, int cantArchivos) {
    	int waitingFileNumber = 0;        
        long now = System.currentTimeMillis();
        long nowMinusMinutes = now - TimeUnit.MINUTES.toMillis(propertiesExterno.LEER_INTERVLO_MINUTOS);
        
        File[] folderOrigen = new File(directorio)
        		.listFiles(file-> {
					try {
						return Files.readAttributes(file.toPath(),BasicFileAttributes.class).creationTime().to(TimeUnit.MILLISECONDS)>=nowMinusMinutes;
					} catch (IOException e) {
						return false;
					}
				});
                
        waitingFileNumber = (folderOrigen!=null)?folderOrigen.length:0;
        if(cantArchivos == waitingFileNumber) {
        	TfindimProcesoTarea tarea =logDAO.obtenerDatosTarea("TAR001");//guardar en properties
        	for (File file2 : folderOrigen) {
//        		por cada archivo que lea hace un insert: en detalle
        		String nombreArchivo = file2.getPath().substring(file2.getPath().lastIndexOf('\\')+1);
        		insertarDetalleLog(tarea,nombreArchivo,"Archivo encontrado: "+nombreArchivo,"20001"/* obtener de properties */);
    		}
        	return true;
        }else {
        	return false;
        }
    	
    }
    public void insertarDetalleLog(TfindimProcesoTarea tarea,String nombreArchivo,String obs,String subtarea) {//id incremental
		TfindimProcesoBatchLogDt tfindimProcesoBatchLogDt = new TfindimProcesoBatchLogDt();
		tfindimProcesoBatchLogDt.setIdProceso("1");//obtener EL DE LA FECHA
		tfindimProcesoBatchLogDt.setTarea(tarea.getIdTarea().toString());
		tfindimProcesoBatchLogDt.setPaso(subtarea);
		tfindimProcesoBatchLogDt.setNombreArchivo(nombreArchivo);//TODO obtener simbolo de properties '\' o '//'
		tfindimProcesoBatchLogDt.setObsEstado(obs);
		logDAO.guardarDetalleLog(tfindimProcesoBatchLogDt);
    }
    @SuppressWarnings("resource")
	public String aggregateFiles(List<Message<File>> messages) throws IOException {
    	//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    	LOG.info("" +messages.size());
    	long now = System.currentTimeMillis();
    	String destino = propertiesExterno.DIRECTORIO_DESTINO_DESCIFRADO_CONSOLIDADO+"\\compilado"+String.valueOf(now)+".txt";
    	
    	Path folderDestino = Paths.get(destino);
        
        File keyFileName = new File(propertiesExterno.DIRECTORIO_LLAVE_PRIVADA);
        String passwd = propertiesExterno.PASSWORD_LLAVE;
        List<String> lines = new ArrayList<>();
        
		try {
			for(Message<File> message : messages){
				System.out.println("message.getPayload().getPath() " + message.getPayload().getPath());
				System.out.println("keyFileName.getAbsolutePath() " + keyFileName.getAbsolutePath());
				
				byte[] decryptFile = KeyBasedFileProcessor.decryptFile(message.getPayload().getPath(),keyFileName.getAbsolutePath(),passwd.toCharArray());
				String result="";
				TfindimProcesoTarea tarea =logDAO.obtenerDatosTarea("TAR001");//guardar en properties
				String nombreArchivo = message.getPayload().getPath().substring(message.getPayload().getPath().lastIndexOf('\\')+1);
				if(decryptFile.length==0) {
					//error al desencriptar el archivo
					insertarDetalleLog(tarea,nombreArchivo,"Error Al desencriptar el archivo: "+nombreArchivo,"20002");
				}else {
					if(message.getPayload().getPath().contains("origenCFBCCF")) {
						
						InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
						result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
						insertarDetalleLog(tarea,nombreArchivo,"Archivo Desencriptado ok: "+nombreArchivo,"20002");
						lines.add(result);
						
						
						
					}else if(message.getPayload().getPath().contains("origenCRBCCF")) {
						
						InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
						result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
						insertarDetalleLog(tarea,nombreArchivo,"Archivo Desencriptado ok: "+nombreArchivo,"20002");
						lines.add(result);
						
						
					}else if(message.getPayload().getPath().contains("origenACBCCF")) {//TODO parametrizar o properties caperta origen
						
						InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
						result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
						insertarDetalleLog(tarea,nombreArchivo,"Archivo Desencriptado ok: "+nombreArchivo,"20002");
						//VALIDACION MONTOS
						String[] parts =result.split("\n");
						if(validarMontosArchivoAC(parts)) {
							insertarDetalleLog(tarea,nombreArchivo,"Archivo AC correcto: "+nombreArchivo,"20003");
							lines.add(result);
						}else {
							insertarDetalleLog(tarea,nombreArchivo,"Archivo AC montos no coinciden : "+nombreArchivo,"20003");
						}
						
					}
					else if(message.getPayload().getPath().contains("origenRDBCCF")){//TODO parametrizar o properties caperta origen
							
						InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
							result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
							insertarDetalleLog(tarea,nombreArchivo,"Archivo Desencriptado ok: "+nombreArchivo,"20002");
							String[] parts =result.split("\n");
							
							if(validarMontosArchivoRD(parts)) {
								insertarDetalleLog(tarea,nombreArchivo,"Archivo RD correcto: "+nombreArchivo,"20003");
								lines.add(result); 
							}else {
								insertarDetalleLog(tarea,nombreArchivo,"Archivo RD montos no coinciden : "+nombreArchivo,"20003");
							}
							
					}
					
					Files.write(folderDestino, lines, Charset.forName("UTF-8"));
					insertarDetalleLog(tarea,nombreArchivo,"Archivo AGREGADO : "+nombreArchivo,"20004");
				}
				
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return "OK";
	}				
    
	
   private boolean validarMontosArchivoRD(String[] parts) {
		// TODO Auto-generated method stub
        Double montoTotal=0.00;
        BigDecimal montoAcumulado = new BigDecimal("0.00");
        
        Double montoSumaContratos=0.00;
        BigDecimal montoSuma = new BigDecimal("0.00");
        
	    String monto="";
	    String signo="";
	    int line=1;
	    
	    
	    
		for (String string : parts) {
			if(line==1) {
		  		  monto= string.substring(48,61)+"."+string.substring(61,63);
		  		  montoTotal = Double.parseDouble(monto);
		  		  
		  		  montoAcumulado = new BigDecimal(montoTotal);
		  		  montoAcumulado = montoAcumulado.setScale(2, RoundingMode.HALF_UP);
		  		  
		  	  }else if(string.length()>5){
		  		  monto= string.substring(115,125)+"."+string.substring(125,127);
		  		  signo=string.substring(114,115);
		  		  if(signo.equals("+")) {
		  			  
		  			montoSumaContratos = montoSumaContratos + Double.parseDouble(monto);
		  			montoSuma= new BigDecimal(montoSumaContratos);
		  			montoSuma = montoSuma.setScale(2, RoundingMode.HALF_UP);
		  			
		  		  }else if (signo.equals("-")) {
		  			  
		  			montoSumaContratos = montoSumaContratos - Double.parseDouble(monto);
		  			montoSuma = new BigDecimal(montoSumaContratos);
		  			montoSuma = montoSuma.setScale(2, RoundingMode.HALF_UP);
		  		  }
		  	  }
			line++;
		}
		    
		    if(montoAcumulado.equals(montoSuma)) {
		    	return true;
		    }else {
		    	return false;
		    }	
	}

	private boolean validarMontosArchivoAC(String[] parts) throws NumberFormatException, IOException {
			// TODO Auto-generated method stub
		Double montoTotal=null;
		BigDecimal montoAcumulado = new BigDecimal("0.00");
		
	    Double montoSumaContratos=0.00;
	    BigDecimal montoSuma = new BigDecimal("0.00");
	    
	    
	    String monto="";
	    String signo="";
	    int line=1;
	    
		for (String string : parts) {
			if(line==1) {
		  		  monto= string.substring(40,53)+"."+string.substring(53,55);
		  		  montoTotal = Double.parseDouble(monto);
		  		  
		  		  montoAcumulado = new BigDecimal(montoTotal);
		  		  montoAcumulado = montoAcumulado.setScale(2, RoundingMode.HALF_UP);
		  		  
		  	  }else if(string.length()>5){
		  		monto= string.substring(42,52)+"."+string.substring(52,54);
		  		signo=string.substring(41,42);  
		  		if(signo.equals("+")) {
		  			montoSumaContratos = montoSumaContratos + Double.parseDouble(monto);
		  		}else if(signo.equals("-")) {
		  			montoSumaContratos = montoSumaContratos - Double.parseDouble(monto);
		  		}
		  		montoSuma= new BigDecimal(montoSumaContratos);
	  			montoSuma = montoSuma.setScale(2, RoundingMode.HALF_UP);
		  	  }
			line++;
		}
		    
		    if(Math.abs(montoTotal) == (Math.abs(montoSumaContratos))) {
		    	return true;
		    }else {
		    	return false;
		    }	
	}

	public List<String> splitContratos(File file){
		List<String> lista = new ArrayList<>();
		try {
		      FileReader fr = new FileReader(file);
		      BufferedReader br = new BufferedReader(fr);
		 
		      String linea;
		      while((linea = br.readLine()) != null) {
		    		  lista.add(linea);
		      }
		      fr.close();
		    }
		    catch(Exception e) {
		      System.out.println("Excepcion leyendo fichero "+ file + ": " + e);
		    }
	   return lista;
	}
   
    public String direccionarFileUnico(String obj) throws IOException{
	   //mover file unico
	   if(obj!=null) {
		   System.out.println("");
		   
		   List<File> archivos = obtenerArchivosPorContrato(obj.substring(2,20));
		   moverFileUnico(archivos);
	   }
	   //armar request
	   return obj;
    }
    
    public void contratoCorreo(String lineaArchivo) throws IOException {
    	String[] partesLinea = lineaArchivo.split(Pattern.quote("|"));
    	List<File> archivosPdfContrato = obtenerArchivosPorContrato(partesLinea[1]);
    	List<String> nombreArchivos = new ArrayList<>();
    	for (File file : archivosPdfContrato) {
    		nombreArchivos.add(file.getName());
		}
    	File archivoCorreo = formatoCorreo(lineaArchivo, nombreArchivos);
    	archivosPdfContrato.add(archivoCorreo);
    	moverArchivosCarpetaMotorCorreos(archivosPdfContrato);
    	
    }
   
   


	private void moverArchivosCarpetaMotorCorreos(List<File> archivosPdfContrato) throws IOException {
		// TODO Auto-generated method stub
		String sDirectorioContratos = propertiesExterno.DIRECTORIO_CONTRATOS;
    	String sDirectorioFileUnico = propertiesExterno.DIRECTORIO_MOTOR_CONTRATOS;
    	
    	for (File file : archivosPdfContrato) {
			
    		String origen = sDirectorioContratos+"\\"+ file.getName();
    		String destino = sDirectorioFileUnico+"\\"+file.getName();

            Path FROM = Paths.get(origen);
            Path TO = Paths.get(destino);
            //sobreescribir el fichero de destino, si existe, y copiar
            // los atributos, incluyendo los permisos rwx
            CopyOption[] options = new CopyOption[]{
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.COPY_ATTRIBUTES
            }; 
            Files.copy(FROM, TO, options);
            
		}
		
	}


	private void moverFileUnico(List<File> archivos) throws IOException {
		// TODO Auto-generated method stub
    	String sDirectorioContratos = propertiesExterno.DIRECTORIO_CONTRATOS;
    	String sDirectorioFileUnico = propertiesExterno.DIRECTORIO_CONTRATOS_FU;
    	
    	for (File file : archivos) {
			
    		String origen = sDirectorioContratos+"\\"+ file.getName();
    		String destino = sDirectorioFileUnico+"\\"+file.getName();

            Path FROM = Paths.get(origen);
            Path TO = Paths.get(destino);
            //sobreescribir el fichero de destino, si existe, y copiar
            // los atributos, incluyendo los permisos rwx
            CopyOption[] options = new CopyOption[]{
              StandardCopyOption.REPLACE_EXISTING,
              StandardCopyOption.COPY_ATTRIBUTES
            }; 
            Files.copy(FROM, TO, options);
            
		}
		
	}

	private List<File> obtenerArchivosPorContrato(String obj) {
		// TODO Auto-generated method stub
		List<File> archivosPorContrato= new ArrayList<>();
		
		String codigoContrato= obj;//por definir donde vendra el codigo de contrato
		String sDirectorio = propertiesExterno.DIRECTORIO_CONTRATOS;
		File f = new File(sDirectorio);
		File[] ficheros = f.listFiles();
		for (int x=0;x<ficheros.length;x++){
		  if(ficheros[x].getName().contains(codigoContrato) && ficheros[x].getName().contains(".pdf")) {
			  archivosPorContrato.add(ficheros[x]);
		  }
		}
		
		return archivosPorContrato;
	}

	public File cifrarContrato(File file) throws IOException {
	   	String name = FilenameUtils.getBaseName(file.getName());
	    File keyFileName = new File(propertiesExterno.DIRECTORIO_LLAVE_PUBLICA);
	    File fileCifrado = null;
	    try {
			KeyBasedFileProcessor.encryptFile(file.getAbsolutePath(), propertiesExterno.DIRECTORIO_TEMP_CIFRADO+name+".pgp", keyFileName.getAbsolutePath());
			fileCifrado = new File(propertiesExterno.DIRECTORIO_TEMP_CIFRADO+name+".pgp");
		} catch (NoSuchProviderException | PGPException e) {
			e.printStackTrace();
		}
		return fileCifrado;
    }
   
//   public static void main(String[] args) throws Exception {
//	byte[] bytes = KeyBasedFileProcessor.decryptFile("D:\\directorios\\origenRDBCCF\\RDBCCF17041301.pgp",
//			"D:\\directorios\\llave\\llave_privada_test_BBVACF.key", "P455w0rd".toCharArray());
//	System.out.println(new String(bytes));
//}
//	
}
