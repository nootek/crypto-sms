package com.nootek.cmsg.crypto.certs;

public final class UserCertificate
{
    private final PrivateCertificate privateCertificate;
    private final PublicCertificate publicCertificate;

    private UserCertificate(PublicCertificate pubCert, PrivateCertificate privCert)
    {
        this.publicCertificate = pubCert;
        this.privateCertificate = privCert;
    }

    public static UserCertificate createUserCertificate()
    {
        PublicCertificate pubCert = createPublicCertificate();
        PrivateCertificate privCert = createPrivateCertificate();
        return new UserCertificate(pubCert,privCert);
    }

    private static PublicCertificate createPublicCertificate()
    {
        //TODO:create public certificate
        return null;
    }

    private static PrivateCertificate createPrivateCertificate()
    {
        //TODO:create private certificate
        return null;
    }

    public PublicCertificate getPublicCertificate()
    {
        return this.publicCertificate;
    }

    public PrivateCertificate getPrivateCertificate()
    {
        return this.privateCertificate;
    }

}
