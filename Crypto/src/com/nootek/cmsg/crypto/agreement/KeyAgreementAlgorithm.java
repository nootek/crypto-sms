package com.nootek.cmsg.crypto.agreement;

import com.nootek.cmsg.crypto.key.PrivateKeySerializer;
import com.nootek.cmsg.crypto.key.PublicKeySerializer;

import javax.crypto.KeyAgreement;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

public interface KeyAgreementAlgorithm
{
    KeyPairGenerator createKeyPairGenerator() throws NoSuchAlgorithmException,NoSuchProviderException;
    AlgorithmParameterSpec createAlgorithmParameterSpec();
    PublicKeySerializer createPublicKeySerializer();
    PrivateKeySerializer createPrivateKeySerializer();
    KeyAgreement createKeyAgreement() throws NoSuchAlgorithmException,NoSuchProviderException;
    KeyFactory createKeyFactory() throws NoSuchAlgorithmException,NoSuchProviderException;

}
