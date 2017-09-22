package com.bbva.integration.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.util.io.Streams;

public class KeyBasedFileProcessor {
	  
	  public static byte[] decryptFile(String inputFileName, String keyFileName, char[] passwd)
			    throws NoSuchProviderException
			  {
			    InputStream in;
			    byte[] byteArray=null;
					try {
						in = new BufferedInputStream(new FileInputStream(inputFileName));
					    InputStream keyIn = new BufferedInputStream(new FileInputStream(keyFileName));
					    ByteArrayOutputStream baos = new ByteArrayOutputStream();
					    decryptFile(in, keyIn, passwd, baos);
					    byteArray = baos.toByteArray();
					    keyIn.close();
					    in.close();
					    baos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    return byteArray;
	   }
	  
	  private static void decryptFile(InputStream in, InputStream keyIn, char[] passwd, OutputStream os)
	    throws IOException, NoSuchProviderException
	  {
	    in = PGPUtil.getDecoderStream(in);
	    try
	    {
	      PGPObjectFactory pgpF = new PGPObjectFactory(in);

	      Object localObject1 = pgpF.nextObject();
	      PGPEncryptedDataList localPGPEncryptedDataList;
	      if(localObject1!=null) {
	    	  if ((localObject1 instanceof PGPEncryptedDataList))
	  	        localPGPEncryptedDataList = (PGPEncryptedDataList)localObject1;
	  	      else {
	  	        localPGPEncryptedDataList = (PGPEncryptedDataList)pgpF.nextObject();
	  	      }
	    	  Iterator<?> localIterator = localPGPEncryptedDataList.getEncryptedDataObjects();
		      PGPPrivateKey localPGPPrivateKey = null;
		      PGPPublicKeyEncryptedData localPGPPublicKeyEncryptedData = null;
		      PGPSecretKeyRingCollection localPGPSecretKeyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn));
		      while ((localPGPPrivateKey == null) && (localIterator.hasNext()))
		      {
		        localPGPPublicKeyEncryptedData = (PGPPublicKeyEncryptedData)localIterator.next();
		        localPGPPrivateKey = Utils.findSecretKey(localPGPSecretKeyRingCollection, localPGPPublicKeyEncryptedData.getKeyID(), passwd);
		      }
		      if (localPGPPrivateKey == null) {
		        throw new IllegalArgumentException("secret key for message not found.");
		      }
		      InputStream localInputStream1 = localPGPPublicKeyEncryptedData.getDataStream(localPGPPrivateKey, "BC");
		      PGPObjectFactory localPGPObjectFactory2 = new PGPObjectFactory(localInputStream1);
		      Object localObject2 = localPGPObjectFactory2.nextObject();

		      if ((localObject2 instanceof PGPCompressedData))
		      {
		        Object localObject3 = (PGPCompressedData)localObject2;
		        Object localObject4 = new PGPObjectFactory(((PGPCompressedData)localObject3).getDataStream());
		        localObject2 = ((PGPObjectFactory)localObject4).nextObject();
		      }
		      if ((localObject2 instanceof PGPLiteralData))
		      {
		        Object localObject3 = (PGPLiteralData)localObject2;

		        InputStream localInputStream2 = ((PGPLiteralData)localObject3).getInputStream();
		        BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(os);
		        Streams.pipeAll(localInputStream2, localBufferedOutputStream);
		        localBufferedOutputStream.close();
		      }
		      else
		      {
		        if ((localObject2 instanceof PGPOnePassSignatureList)) {
		          throw new PGPException("encrypted message contains a signed message - not literal data.");
		        }
		        throw new PGPException("message is not a simple encrypted file - type unknown.");
		      }
		      if (localPGPPublicKeyEncryptedData.isIntegrityProtected())
		      {
		        if (!localPGPPublicKeyEncryptedData.verify())
		          System.err.println("message failed integrity check");
		        else {
		          System.err.println("message integrity check passed");
		        }
		      }
		      else {
		        System.err.println("no message integrity check");
		      }
	      }
	    }
	    catch (PGPException localPGPException)
	    {
	      System.err.println(localPGPException);
	      if (localPGPException.getUnderlyingException() != null)
	        localPGPException.getUnderlyingException().printStackTrace();
	    }
	  }
	  


	  public static void encryptFile(String fileName, String outputFile, String encKeyFileName)
	    throws IOException, NoSuchProviderException, PGPException
	  {
	    String provider = "BC";
	    boolean armored = false;
	    boolean withIntegrityPacket = false;
	    int compLib = 1;
	    int algorithm = 9;
	    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
	    PGPPublicKey localPGPPublicKey = Utils.readPublicKey(encKeyFileName);
	    encryptFile(out, fileName, localPGPPublicKey, provider, armored, withIntegrityPacket, compLib, algorithm);
	    out.close();
	  }

	  private static void encryptFile(OutputStream outputStream, String fileName, PGPPublicKey publicKey, String providerName, boolean armored, boolean withIntegrityPacket, int compressionLib, int algorithm)
	    throws IOException, NoSuchProviderException
	  {
	    if (armored) {
	      outputStream = new ArmoredOutputStream(outputStream);
	    }
	    try
	    {
	      byte[] arrayOfByte = Utils.compressFile(fileName, compressionLib);

	      PGPEncryptedDataGenerator dataGenerator = new PGPEncryptedDataGenerator(9, withIntegrityPacket, new SecureRandom(), "BC");
	      dataGenerator.addMethod(publicKey);
	      OutputStream localOutputStream = dataGenerator.open(outputStream, arrayOfByte.length);
	      localOutputStream.write(arrayOfByte);
	      localOutputStream.close();
	      if (armored) {
	        outputStream.close();
	      }

	    }
	    catch (PGPException localPGPException)
	    {
	      System.err.println(localPGPException);
	      if (localPGPException.getUnderlyingException() != null)
	        localPGPException.getUnderlyingException().printStackTrace();
	    }
	  }
	  
}
