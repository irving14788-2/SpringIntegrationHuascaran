package com.bbva.integration.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.util.Iterator;

import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

public class KeyBasedFileProcessor {
	  
	
	  public static InputStream decryptFile(String inputFileName, String keyFileName, char[] passwd)
			    throws IOException, NoSuchProviderException
			  {
			    InputStream in = new BufferedInputStream(new FileInputStream(inputFileName));
			    InputStream keyIn = new BufferedInputStream(new FileInputStream(keyFileName));
			    InputStream out =  decryptFile(in, keyIn, passwd);
			    keyIn.close();
			    in.close();
			    return out;
			  }
	  
	  private static InputStream decryptFile(InputStream in, InputStream keyIn, char[] passwd)
	    throws IOException, NoSuchProviderException
	  {
		InputStream rpta = null;
	    in = PGPUtil.getDecoderStream(in);
	    try
	    {
	      PGPObjectFactory pgpF = new PGPObjectFactory(in);

	      Object localObject1 = pgpF.nextObject();
	      PGPEncryptedDataList localPGPEncryptedDataList;
	      if ((localObject1 instanceof PGPEncryptedDataList))
	        localPGPEncryptedDataList = (PGPEncryptedDataList)localObject1;
	      else {
	        localPGPEncryptedDataList = (PGPEncryptedDataList)pgpF.nextObject();
	      }
	      Iterator localIterator = localPGPEncryptedDataList.getEncryptedDataObjects();
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
	        //BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(stream);
	        //Streams.pipeAll(localInputStream2, localBufferedOutputStream);
	        //localBufferedOutputStream.close();
	        rpta = localInputStream2;
	      }
	      else
	      {
	        if ((localObject2 instanceof PGPOnePassSignatureList)) {
	          throw new PGPException("encrypted message contains a signed message - not literal data.");
	        }
	        throw new PGPException("message is not a simple encrypted file - type unknown.");
	      }
	      Object localObject3;
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
	    catch (PGPException localPGPException)
	    {
	      System.err.println(localPGPException);
	      if (localPGPException.getUnderlyingException() != null)
	        localPGPException.getUnderlyingException().printStackTrace();
	    }
	    return rpta;
	  }
	  
	  
}
