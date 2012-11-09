package com.nootek.cmsg.crypto.storage;

import com.nootek.cmsg.crypto.certs.PrivateCertificate;
import com.nootek.cmsg.crypto.certs.PublicCertificate;
import com.nootek.cmsg.crypto.certs.UserCertificate;

public interface CertificateStorage
{
    void storeUserCertificate(UserCertificate userCert,String password) throws CertificateStoreException;
    void storePublicCertificate(PublicCertificate pubCert) throws CertificateStoreException;

    PublicCertificate loadPublicCertificateById(int id) throws CertificateLoadException;
    PublicCertificate loadPublicCertificateByPhone(String phone) throws CertificateLoadException;
    PrivateCertificate loadPrivateCertificateById(int id,String password) throws CertificateLoadException;
    PrivateCertificate loadPrivateCertificateByPhone(String phone,String password) throws CertificateLoadException;
}
