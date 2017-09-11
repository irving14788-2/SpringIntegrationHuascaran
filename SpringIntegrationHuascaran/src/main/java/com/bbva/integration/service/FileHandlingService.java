package com.bbva.integration.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bbva.integration.util.KeyBasedFileProcessor;
import com.bbva.integration.util.PropertiesExterno;

import ch.qos.logback.core.net.SyslogOutputStream;


@Component
public class FileHandlingService {
	
	 private static final Logger LOG = LoggerFactory.getLogger(FileHandlingService.class);
	
	@Autowired
	PropertiesExterno propertiesExterno;
	
	@CorrelationStrategy
    public String correlateBy(@Header("id") String id) {
        return "1";
    }

    @ReleaseStrategy
    public boolean release(List<Message<File>> messages) {
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
    	return cantArchivos == waitingFileNumber;
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
				if(message.getPayload().getPath().contains("origenCFBCCF")) {
					InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
					result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
					lines.add(result);
				}else if(message.getPayload().getPath().contains("origenCRBCCF")) {
					
					InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
					result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
					lines.add(result);
				}else if(message.getPayload().getPath().contains("origenACBCCF")) {//TODO parametrizar o properties caperta origen
					InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
					result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
					//VALIDACION MONTOS
					String[] parts =result.split("\n");
					if(validarMontosArchivoAC(parts)) {
						lines.add(result);
					}
				}
				else if(message.getPayload().getPath().contains("origenRDBCCF")){//TODO parametrizar o properties caperta origen
						InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(decryptFile),"utf8");
						result = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
						String[] parts =result.split("\n");
						if(validarMontosArchivoRD(parts)) {
							lines.add(result); 
						}
				}
			}
			Files.write(folderDestino, lines, Charset.forName("UTF-8"));
		
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
	    Double montoSumaContratos=0.00;
	    String monto="";
	    int line=1;
		for (String string : parts) {
			if(line==1) {
		  		  monto= string.substring(40,53)+"."+string.substring(53,55);
		  		  montoTotal = Double.parseDouble(monto);
		  	  }else if(string.length()>5){
		  		  monto= string.substring(42,52)+"."+string.substring(52,54);
		  		  montoSumaContratos = montoSumaContratos + Double.parseDouble(monto);
		  	  }
			line++;
		}
		    
		    if(montoTotal.equals(montoSumaContratos)) {
		    	return true;
		    }else {
		    	return false;
		    }	
	}

	public List<String> splitContratos(File file){
	   
	   List<String> lista = new ArrayList<>();
	   lista.add("A");
	   lista.add("B");
	   lista.add("C");
	   
	   return lista;
	}
   
    public String direccionarFileUnico(String obj){
	   //mover file unico
	   
	   //armar request
	   return "OK";
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
