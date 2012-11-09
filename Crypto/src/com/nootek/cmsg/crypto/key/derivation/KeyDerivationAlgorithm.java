package com.nootek.cmsg.crypto.key.derivation;

public interface KeyDerivationAlgorithm
{
    byte[] deriveKeyFromPassword(String password) throws KeyDerivationException;
}
