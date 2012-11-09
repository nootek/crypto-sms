package com.nootek.cmsg.crypto;

import com.nootek.cmsg.crypto.cipher.aes.AESVersionOneEncryptionAlgorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

public class Test
{
    static
    {
        Security.addProvider(new BouncyCastleProvider());

    }
    public static void main(String[] args)
    {
        try
        {
            String password = "vasyabes";
            String data = "This is secret!";
            AESVersionOneEncryptionAlgorithm aes = new AESVersionOneEncryptionAlgorithm();
            byte[] encryptedData = aes.encrypt(data.getBytes("UTF-8"),password);
            System.out.println("Encrypted data length:" + encryptedData.length);
            byte[] decryptedData = aes.decrypt(encryptedData,password);
            System.out.println(new String(decryptedData,"UTF-8"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
