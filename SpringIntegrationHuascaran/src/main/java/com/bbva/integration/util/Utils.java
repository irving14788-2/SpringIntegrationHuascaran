package com.bbva.integration.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.util.Iterator;

import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;


public class Utils
{
  private static String digits = "0123456789abcdef";

  public static String makeBlankString(int len)
  {
    char[] buf = new char[len];

    for (int i = 0; i != buf.length; i++)
    {
      buf[i] = ' ';
    }

    return new String(buf);
  }

  public static String toHex(byte[] data, int length)
  {
    StringBuffer buf = new StringBuffer();

    for (int i = 0; i != length; i++)
    {
      int v = data[i] & 0xFF;

      buf.append(digits.charAt(v >> 4));
      buf.append(digits.charAt(v & 0xF));
    }

    return buf.toString();
  }

  public static String toHex(byte[] data)
  {
    return toHex(data, data.length);
  }

  public static byte[] compressFile(String fileName, int compressionLib)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    PGPCompressedDataGenerator localPGPCompressedDataGenerator = new PGPCompressedDataGenerator(compressionLib);
    PGPUtil.writeFileToLiteralData(localPGPCompressedDataGenerator.open(localByteArrayOutputStream), 'b', new File(fileName));
    localPGPCompressedDataGenerator.close();
    return localByteArrayOutputStream.toByteArray();
  }

  static PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection paramPGPSecretKeyRingCollection, long paramLong, char[] paramArrayOfChar) throws PGPException, NoSuchProviderException
  {
    PGPSecretKey localPGPSecretKey = paramPGPSecretKeyRingCollection.getSecretKey(paramLong);
    if (localPGPSecretKey == null) {
      return null;
    }
    return localPGPSecretKey.extractPrivateKey(paramArrayOfChar, "BC");
  }

  static PGPPublicKey readPublicKey(String paramString)
    throws IOException, PGPException
  {
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(paramString));
    PGPPublicKey localPGPPublicKey = readPublicKey(localBufferedInputStream);
    localBufferedInputStream.close();
    return localPGPPublicKey;
  }

  static PGPPublicKey readPublicKey(InputStream paramInputStream)
    throws IOException, PGPException
  {
    PGPPublicKeyRingCollection localPGPPublicKeyRingCollection = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(paramInputStream));
    Iterator localIterator1 = localPGPPublicKeyRingCollection.getKeyRings();
    while (localIterator1.hasNext())
    {
      PGPPublicKeyRing localPGPPublicKeyRing = (PGPPublicKeyRing)localIterator1.next();
      Iterator localIterator2 = localPGPPublicKeyRing.getPublicKeys();
      while (localIterator2.hasNext())
      {
        PGPPublicKey localPGPPublicKey = (PGPPublicKey)localIterator2.next();
        if (localPGPPublicKey.isEncryptionKey()) {
          return localPGPPublicKey;
        }
      }
    }
    throw new IllegalArgumentException("Can't find encryption key in key ring.");
  }

  static PGPSecretKey readSecretKey(String paramString)
    throws IOException, PGPException
  {
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(paramString));
    PGPSecretKey localPGPSecretKey = readSecretKey(localBufferedInputStream);
    localBufferedInputStream.close();
    return localPGPSecretKey;
  }

  static PGPSecretKey readSecretKey(InputStream paramInputStream)
    throws IOException, PGPException
  {
    PGPSecretKeyRingCollection localPGPSecretKeyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(paramInputStream));
    Iterator localIterator1 = localPGPSecretKeyRingCollection.getKeyRings();
    while (localIterator1.hasNext())
    {
      PGPSecretKeyRing localPGPSecretKeyRing = (PGPSecretKeyRing)localIterator1.next();
      Iterator localIterator2 = localPGPSecretKeyRing.getSecretKeys();
      while (localIterator2.hasNext())
      {
        PGPSecretKey localPGPSecretKey = (PGPSecretKey)localIterator2.next();
        if (localPGPSecretKey.isSigningKey()) {
          return localPGPSecretKey;
        }
      }
    }
    throw new IllegalArgumentException("Can't find signing key in key ring.");
  }
}