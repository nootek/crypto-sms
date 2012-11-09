package com.nootek.cmsg.crypto.cipher;

import com.nootek.cmsg.crypto.cipher.aes.AESVersionOneEncryptionAlgorithm;

import java.security.NoSuchAlgorithmException;

public class EncryptionAlgorithmFactory
{
    public EncryptionAlgorithm createEncryptionAlgorithmById(int id) throws NoSuchAlgorithmException
    {
        if(id == EncryptionAlgorithmIDs.AES_VERSION_1)
        {
            return new AESVersionOneEncryptionAlgorithm();
        }
        else
        {
            String exceptionMessage = String.format("No such algorithm for id \"%d\" found",id);
            throw new NoSuchAlgorithmException(exceptionMessage);
        }

    }
}
