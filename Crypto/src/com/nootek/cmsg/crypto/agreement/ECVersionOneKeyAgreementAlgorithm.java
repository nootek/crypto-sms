package com.nootek.cmsg.crypto.agreement;

import com.nootek.cmsg.crypto.key.PrivateKeySerializer;
import com.nootek.cmsg.crypto.key.PublicKeySerializer;
import com.nootek.cmsg.crypto.key.ec.ECPrivateKeySerializer;
import com.nootek.cmsg.crypto.key.ec.ECPublicKeySerializer;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class ECVersionOneKeyAgreementAlgorithm implements KeyAgreementAlgorithm
{
    static
    {
        Security.addProvider(new BouncyCastleProvider());

    }

    @Override
    public KeyPairGenerator createKeyPairGenerator() throws NoSuchAlgorithmException,NoSuchProviderException
    {
        return KeyPairGenerator.getInstance("ECDH","BC");
    }

    @Override
    public AlgorithmParameterSpec createAlgorithmParameterSpec()
    {
        return ECNamedCurveTable.getParameterSpec("prime192v1");
    }

    @Override
    public PublicKeySerializer createPublicKeySerializer()
    {
        return new ECPublicKeySerializer();
    }

    @Override
    public PrivateKeySerializer createPrivateKeySerializer()
    {
        return new ECPrivateKeySerializer();
    }

    @Override
    public KeyAgreement createKeyAgreement() throws NoSuchAlgorithmException,NoSuchProviderException
    {
        return KeyAgreement.getInstance("ECDH","BC");
    }

    @Override
    public KeyFactory createKeyFactory() throws NoSuchAlgorithmException,NoSuchProviderException
    {
        return KeyFactory.getInstance("ECDH","BC");
    }
}
