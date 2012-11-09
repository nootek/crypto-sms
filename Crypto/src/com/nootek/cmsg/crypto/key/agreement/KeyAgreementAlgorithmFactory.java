package com.nootek.cmsg.crypto.key.agreement;

import java.security.NoSuchAlgorithmException;

public final class KeyAgreementAlgorithmFactory
{

    public KeyAgreementAlgorithm createKeyAgreementAlgorithmByID(int id) throws NoSuchAlgorithmException
    {
        if(id == KeyAgreementAlgorithmIDs.ECDH_VERSION_1)
        {
            return new ECVersionOneKeyAgreementAlgorithm();
        }
        else
        {
            String exceptionMessage = String.format("No such algorithm for id \"%d\" found",id);
            throw new NoSuchAlgorithmException(exceptionMessage);
        }
    }

}
