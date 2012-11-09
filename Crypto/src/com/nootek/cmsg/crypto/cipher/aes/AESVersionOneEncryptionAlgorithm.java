package com.nootek.cmsg.crypto.cipher.aes;

import com.nootek.cmsg.crypto.cipher.DecryptionException;
import com.nootek.cmsg.crypto.cipher.EncryptionAlgorithm;
import com.nootek.cmsg.crypto.cipher.EncryptionException;
import com.nootek.cmsg.crypto.cipher.Key;
import com.nootek.cmsg.crypto.key.derivation.PBKDF2KeyDerivationAlgorithm;

import java.io.*;

public class AESVersionOneEncryptionAlgorithm implements EncryptionAlgorithm
{
    @Override
    public byte[] encrypt(byte[] data, String password) throws EncryptionException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try
        {
            DataOutputStream output = new DataOutputStream(byteStream);

            PBKDF2KeyDerivationAlgorithm keyDerivationAlgorithm = new PBKDF2KeyDerivationAlgorithm();
            byte[] strongPassword = keyDerivationAlgorithm.deriveKeyFromPassword(password);
            byte[] salt = keyDerivationAlgorithm.getSalt();
            int keyLength = keyDerivationAlgorithm.getKeyLength();
            int loopCounter = keyDerivationAlgorithm.getLoopCounter();

            output.writeInt(salt.length);
            output.write(salt);
            output.writeInt(keyLength);
            output.writeInt(loopCounter);

            AESCryptographer cryptographer = new AESCryptographer();
            byte[] encryptedData = cryptographer.encrypt(data,new Key(strongPassword));
            byte[] iv = cryptographer.getIV();

            output.writeInt(iv.length);
            output.write(iv);
            output.writeInt(encryptedData.length);
            output.write(encryptedData);
        }catch (Exception e)
        {
            throw new EncryptionException("Can't encrypt data",e);
        }
        return byteStream.toByteArray();
    }


    @Override
    public byte[] decrypt(byte[] data, String password) throws DecryptionException
    {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        byte[] decryptedData = null;
        try
        {
            DataInputStream input = new DataInputStream(byteStream);

            int saltLength = input.readInt();
            byte[] salt = new byte[saltLength];
            input.read(salt);

            int keyLength = input.readInt();
            int loopCounter = input.readInt();

            PBKDF2KeyDerivationAlgorithm keyDerivationAlgorithm = new PBKDF2KeyDerivationAlgorithm(keyLength,salt,loopCounter);
            byte[] strongPassword = keyDerivationAlgorithm.deriveKeyFromPassword(password);

            int ivLength = input.readInt();
            byte[] iv = new byte[ivLength];
            input.read(iv);

            int encryptedDataLength = input.readInt();
            byte[] encryptedData = new byte[encryptedDataLength];
            input.read(encryptedData);

            AESCryptographer cryptographer = new AESCryptographer(iv);
            decryptedData = cryptographer.decrypt(encryptedData, new Key(strongPassword));
        }catch (Exception e)
        {
            throw new DecryptionException("Can't decrypt data",e);
        }
        return decryptedData;
    }
}
