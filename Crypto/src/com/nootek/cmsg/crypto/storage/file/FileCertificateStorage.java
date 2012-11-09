package com.nootek.cmsg.crypto.storage.file;

import com.nootek.cmsg.crypto.certs.PrivateCertificate;
import com.nootek.cmsg.crypto.certs.PublicCertificate;
import com.nootek.cmsg.crypto.certs.UserCertificate;
import com.nootek.cmsg.crypto.cipher.EncryptionAlgorithm;
import com.nootek.cmsg.crypto.cipher.EncryptionAlgorithmFactory;
import com.nootek.cmsg.crypto.key.PrivateKeyWithID;
import com.nootek.cmsg.crypto.key.PublicKeyWithID;
import com.nootek.cmsg.crypto.key.agreement.KeyAgreementAlgorithm;
import com.nootek.cmsg.crypto.key.agreement.KeyAgreementAlgorithmFactory;
import com.nootek.cmsg.crypto.key.serializer.PrivateKeySerializer;
import com.nootek.cmsg.crypto.key.serializer.PublicKeySerializer;
import com.nootek.cmsg.crypto.storage.CertificateLoadException;
import com.nootek.cmsg.crypto.storage.CertificateStorage;
import com.nootek.cmsg.crypto.storage.CertificateStoreException;
import com.nootek.cmsg.crypto.utils.io.StreamCloser;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class FileCertificateStorage implements CertificateStorage
{
    private final String DIR_NAME = "CryptoSMS";
    private final String PRIVATE_CERT_EXTENSION = "privCert";
    private final String PUBLIC_CERT_EXTENSION = "pubCert";
    private final File dirStoringCertificates;
    private PrivateCertificate privateCertificate;
    private PublicCertificate publicCertificate;
    private DataOutputStream output;
    private DataInputStream input;

    public FileCertificateStorage(String pathToSaveCerts) throws IOException
    {
        dirStoringCertificates = new File(pathToSaveCerts + DIR_NAME);
        boolean success = dirStoringCertificates.mkdir();
        if(!success)
        {
            String exceptionMessage = String.format("Can't create directory storing certificates by \"%s\" path",pathToSaveCerts);
            throw new IOException(exceptionMessage);
        }

    }

    @Override
    public void storeUserCertificate(UserCertificate userCert,String password) throws CertificateStoreException
    {
        if(userCert == null)
        {
            throw new IllegalArgumentException("User certificate is null");
        }
        if(password == null)
        {
            throw new IllegalArgumentException("Password is null");
        }
        storePublicCertificate(userCert.getPublicCertificate());
        storePrivateCertificate(userCert.getPrivateCertificate(),password);
    }

    @Override
    public void storePublicCertificate(PublicCertificate certificate) throws CertificateStoreException
    {
        if(certificate == null)
        {
            throw new IllegalArgumentException("Public certificate is null");
        }
        this.publicCertificate = certificate;

        String fileName = generateFileNameForPublicCertificate();
        File fileToStoreCertificate = new File(dirStoringCertificates,fileName);

        try
        {
            output = new DataOutputStream(new FileOutputStream(fileToStoreCertificate));
            storePublicCertificateData();
        }catch (Exception e)
        {
            throw new CertificateStoreException("Can't save public certificate",e);
        }
        finally {
            StreamCloser.closeStream(input);
        }
    }
    private String generateFileNameForPublicCertificate()
    {
        int id = publicCertificate.getID();
        String stringId = String.valueOf(id);
        String phone = publicCertificate.getPhone();

        String fileName = FileStoreUtils.generateFileNameFromParts('.',stringId,phone,PUBLIC_CERT_EXTENSION);
        return fileName;
    }

    private void storePublicCertificateData() throws Exception
    {
        int id = publicCertificate.getID();
        String phone = publicCertificate.getPhone();
        PublicKeyWithID keyWithID = publicCertificate.getPublicKeyWithID();
        byte keyGenerationAlgoritmID = keyWithID.getKeyGenerationAlgorithmID();
        PublicKey key = keyWithID.getPublicKey();
        Date storeDate = publicCertificate.getStoreDate();

        output.writeInt(id);

        byte[] phoneBytes = phone.getBytes("UTF-8");
        output.writeByte(phoneBytes.length);
        output.write(phoneBytes);

        output.writeByte(keyGenerationAlgoritmID);

        KeyAgreementAlgorithmFactory keyAgreementAlgorithmFactory = new KeyAgreementAlgorithmFactory();
        KeyAgreementAlgorithm keyAgreementAlgorithm = keyAgreementAlgorithmFactory.createKeyAgreementAlgorithmByID(keyGenerationAlgoritmID);
        PublicKeySerializer publicKeySerializer = keyAgreementAlgorithm.createPublicKeySerializer();

        byte[] publicKeyBytes = publicKeySerializer.serialize(key);
        output.writeInt(publicKeyBytes.length);
        output.write(publicKeyBytes);

        output.writeLong(storeDate.getTime());
    }

    private void storePrivateCertificate(PrivateCertificate certificate,String password) throws CertificateStoreException
    {
        if(certificate == null)
        {
            throw new IllegalArgumentException("Private certificate is null");
        }
        if(password == null)
        {
            throw new IllegalArgumentException("Password is null");
        }
        this.privateCertificate = certificate;

        String fileName = generateFileNameForPrivateCertificate();
        File fileToStorePublicCert = new File(dirStoringCertificates,fileName);
        try
        {
            output = new DataOutputStream(new FileOutputStream(fileToStorePublicCert));
            storePrivateCertificateData(password);
        }catch (Exception e)
        {
            throw new CertificateStoreException("Can't save private certificate",e);
        }finally {
            StreamCloser.closeStream(output);
        }
    }

    private String generateFileNameForPrivateCertificate()
    {
        int id = privateCertificate.getID();
        String phone = privateCertificate.getPhone();

        String fileName = FileStoreUtils.generateFileNameFromParts('.',Integer.toString(id),phone,PRIVATE_CERT_EXTENSION);
        return fileName;
    }

    private void storePrivateCertificateData(String password) throws Exception
    {
        int id = privateCertificate.getID();
        String phone = privateCertificate.getPhone();
        PrivateKeyWithID keyWithID = privateCertificate.getPrivateKeyWithID();
        byte keyGenerationAlgorithmID = keyWithID.getKeyGenerationAlgorithmID();
        PrivateKey key = keyWithID.getPrivateKey();
        byte encryptionAlgorithmID = privateCertificate.getEncryptionAlgortihmID();

        output.writeInt(id);

        byte[] phoneBytes = phone.getBytes("UTF-8");
        output.writeByte(phoneBytes.length);
        output.write(phoneBytes);

        output.writeByte(keyGenerationAlgorithmID);

        KeyAgreementAlgorithmFactory keyAgreementFactory = new KeyAgreementAlgorithmFactory();
        KeyAgreementAlgorithm keyAgreementAlgorithm = keyAgreementFactory.createKeyAgreementAlgorithmByID(keyGenerationAlgorithmID);
        PrivateKeySerializer serializer = keyAgreementAlgorithm.createPrivateKeySerializer();
        byte[] keyBytes = serializer.serialize(key);


        EncryptionAlgorithmFactory encryptionFactory = new EncryptionAlgorithmFactory();
        EncryptionAlgorithm encryptionAlgorithm = encryptionFactory.createEncryptionAlgorithmById(encryptionAlgorithmID);

        byte[] encryptedKey = encryptionAlgorithm.encrypt(keyBytes,password);

        output.writeByte(encryptionAlgorithmID);
        output.writeInt(encryptedKey.length);
        output.write(encryptedKey);

    }

    @Override
    public PublicCertificate loadPublicCertificateById(int id) throws CertificateLoadException
    {
        final int FILE_WITH_UNIQUE_ID = 0;
        List<File> files = FileStoreUtils.getFilesWithNameContains(dirStoringCertificates, String.valueOf(id), PRIVATE_CERT_EXTENSION);
        if(files.isEmpty())
        {
            String exceptionMessage = String.format("Can't find public certificate with \"%s\" id in \"%s\" directory", id,dirStoringCertificates);
            throw new CertificateLoadException(exceptionMessage);
        }
        PublicCertificate certificate = null;
        File file = files.get(FILE_WITH_UNIQUE_ID);
        try
        {
            input = new DataInputStream(new FileInputStream(file));
            certificate = restorePublicCertificate();

        } catch (Exception e)
        {
            String exceptionMessage = String.format("Can't load public certificate with \"%s\" id", id);
            throw new CertificateLoadException(exceptionMessage, e);
        } finally
        {
            StreamCloser.closeStream(input);
        }
        return certificate;
    }

    private PublicCertificate restorePublicCertificate() throws Exception
    {
        int id = input.readInt();

        byte phoneLength = input.readByte();
        byte[] phoneBytes = new byte[phoneLength];
        String phone = new String(phoneBytes,"UTF-8");

        byte keyGenerationAlgoritmID = input.readByte();

        int publicKeyLength = input.readInt();
        byte[] publicKeyBytes = new byte[publicKeyLength];
        input.read(publicKeyBytes);

        KeyAgreementAlgorithmFactory factory = new KeyAgreementAlgorithmFactory();
        KeyAgreementAlgorithm algorithm = factory.createKeyAgreementAlgorithmByID(keyGenerationAlgoritmID);
        PublicKeySerializer serializer = algorithm.createPublicKeySerializer();
        PublicKey key = serializer.deserialize(publicKeyBytes);

        long certStoreDateInSecs = input.readLong();
        Date certStoreDate = new Date(certStoreDateInSecs);

        PublicKeyWithID keyWithID = new PublicKeyWithID(key,keyGenerationAlgoritmID);
        return new PublicCertificate(id,phone,keyWithID,certStoreDate);
    }


    @Override
    public PublicCertificate loadPublicCertificateByPhone(String phone) throws CertificateLoadException
    {
        if(phone == null)
        {
            throw new IllegalArgumentException("Phone is null");
        }
        PublicCertificate certificateWithLastStoreDate = null;
        try
        {
            List<PublicCertificate> certificates = getPublicCertificatesWithPhone(phone);
            certificateWithLastStoreDate = getPublicCertificateWithLatestStoreDate(certificates);
        } catch (Exception e)
        {
            String exceptionMessage = String.format("Can't load public certificate with \"%s\" phone", phone);
            throw new CertificateLoadException(exceptionMessage, e);
        } finally
        {
            StreamCloser.closeStream(input);
        }
        return certificateWithLastStoreDate;
    }

    private List<PublicCertificate> getPublicCertificatesWithPhone(String phone) throws Exception
    {
        List<File> files = FileStoreUtils.getFilesWithNameContains(dirStoringCertificates,phone,PUBLIC_CERT_EXTENSION);
        if(files.isEmpty())
        {
            String exceptionMessage = String.format("Can't find public certificates with \"%s\" phone in \"%s\" directory", phone,dirStoringCertificates);
            throw new CertificateLoadException(exceptionMessage);
        }
        List<PublicCertificate> certificates = new ArrayList<>();
        for(File file : files)
        {
            input = new DataInputStream(new FileInputStream(file));
            PublicCertificate сertificate = restorePublicCertificate();
            certificates.add(сertificate);
        }
        return certificates;
    }

    private PublicCertificate getPublicCertificateWithLatestStoreDate(List<PublicCertificate> certificates)
    {
        Date latestStoreDate = null;
        PublicCertificate certificateWithLatestStoreDate = null;
        for(PublicCertificate certificate : certificates)
        {
            Date certStoreDate = publicCertificate.getStoreDate();
            if(latestStoreDate == null || latestStoreDate.after(certStoreDate))
            {
                latestStoreDate = certStoreDate;
                certificateWithLatestStoreDate = certificate;
            }
        }
        return certificateWithLatestStoreDate;
    }

    @Override
    public PrivateCertificate loadPrivateCertificateById(int id, String password) throws CertificateLoadException
    {
        if (password == null)
        {
            throw new IllegalArgumentException("Password is null");
        }
        final int FILE_WITH_UNIQUE_ID = 0;
        List<File> files = FileStoreUtils.getFilesWithNameContains(dirStoringCertificates, String.valueOf(id), PRIVATE_CERT_EXTENSION);
        if(files.isEmpty())
        {
            String exceptionMessage = String.format("Can't find private certificate with \"%s\" id in \"%s\" directory", id,dirStoringCertificates);
            throw new CertificateLoadException(exceptionMessage);
        }
        PrivateCertificate certificate = null;
        File file = files.get(FILE_WITH_UNIQUE_ID);
        try
        {
            input = new DataInputStream(new FileInputStream(file));
            certificate = restorePrivateCertificate(password);

        } catch (Exception e)
        {
            String exceptionMessage = String.format("Can't load private certificate with \"%s\" id", id);
            throw new CertificateLoadException(exceptionMessage, e);
        } finally
        {
            StreamCloser.closeStream(input);
        }
        return certificate;
    }

    private PrivateCertificate restorePrivateCertificate(String password) throws Exception
    {
        int id = input.readInt();

        byte phoneLength = input.readByte();
        byte[] phoneBytes = new byte[phoneLength];
        String phone = new String(phoneBytes,"UTF-8");

        byte keyGenerationAlgorithmID = input.readByte();

        int encryptedKeyLength = input.readInt();
        byte[] encryptedKey = new byte[encryptedKeyLength];
        input.read(encryptedKey);

        byte encryptionAlgorithmID = input.readByte();

        EncryptionAlgorithmFactory encryptionFactory = new EncryptionAlgorithmFactory();
        EncryptionAlgorithm encryptionAlgorithm = encryptionFactory.createEncryptionAlgorithmById(encryptionAlgorithmID);

        byte[] keyBytes = encryptionAlgorithm.decrypt(encryptedKey,password);


        KeyAgreementAlgorithmFactory keyAgreementAlgorithmFactory = new KeyAgreementAlgorithmFactory();
        KeyAgreementAlgorithm keyAgreementAlgorithm = keyAgreementAlgorithmFactory.createKeyAgreementAlgorithmByID(encryptionAlgorithmID);
        PrivateKeySerializer privateKeySerializer = keyAgreementAlgorithm.createPrivateKeySerializer();
        PrivateKey privateKey = privateKeySerializer.deserialize(keyBytes);


        PrivateKeyWithID keyWithID = new PrivateKeyWithID(privateKey,keyGenerationAlgorithmID);
        PrivateCertificate privateCertificate = new PrivateCertificate(id,phone,keyWithID);
        return privateCertificate;
    }

    @Override
    public PrivateCertificate loadPrivateCertificateByPhone(String phone, String password) throws CertificateLoadException
    {
        if(phone == null)
        {
            throw new IllegalArgumentException("Phone is null");
        }
        if(password == null)
        {
            throw new IllegalArgumentException("Password is null");
        }

        List<File> files = FileStoreUtils.getFilesWithNameContains(dirStoringCertificates, phone, PRIVATE_CERT_EXTENSION);
        if(files.isEmpty())
        {
            String exceptionMessage = String.format("Can't find private cert with \"%s\" phone in \"%s\" directory", phone,dirStoringCertificates);
            throw new CertificateLoadException(exceptionMessage);
        }
        PrivateCertificate privateCertificate = null;
        final int FILE_WITH_UNIQUE_ID = 0;
        File file = files.get(FILE_WITH_UNIQUE_ID);
        try
        {
            input = new DataInputStream(new FileInputStream(file));
            privateCertificate = restorePrivateCertificate(password);
        } catch (Exception e)
        {
            String exceptionMessage = String.format("Can't load private cert with \"%s\" phone", phone);
            throw new CertificateLoadException(exceptionMessage, e);
        } finally
        {
            StreamCloser.closeStream(input);
        }
        return privateCertificate;
    }
}
