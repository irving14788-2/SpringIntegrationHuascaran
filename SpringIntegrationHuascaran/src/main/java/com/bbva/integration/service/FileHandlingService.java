package com.bbva.integration.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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


@Component
public class FileHandlingService {

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
    
    
    public String aggregateFiles(List<Message<File>> messages) throws IOException {
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
				
				InputStream inputStream = KeyBasedFileProcessor.decryptFile(message.getPayload().getPath(),keyFileName.getAbsolutePath(),passwd.toCharArray());
				
				//validar arhivo descifrado
				
				String result = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));				
				lines.add(result);
			}
			Files.write(folderDestino, lines, Charset.forName("UTF-8"));
		
		} catch (NoSuchProviderException e) {
			e.printStackTrace();			
		}
		return "OK";
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
   
	
}
