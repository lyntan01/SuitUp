package util.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;



public class GlassFishCryptographicHelper
{
    private static final String GLASSFISH_DEFAULT_KEYSTORE_TYPE = "JKS";    
    
    // This works only if invoked on server side
    //private static final String GLASSFISH_DEFAULT_DOMAIN_CONFIG_DIRECTORY = System.getProperty("user.dir");
    // On client side, easier to hardcode the path for demonstration purpose
    private static final String GLASSFISH_DEFAULT_DOMAIN_CONFIG_DIRECTORY = "C:\\glassfish-5.1.0\\glassfish\\domains\\domain1\\config";
    
    private static final String GLASSFISH_DEFAULT_KEYSTORE_NAME = "keystore.jks";
    private static final String GLASSFISH_DEFAULT_KEYSTORE_PATH = GLASSFISH_DEFAULT_DOMAIN_CONFIG_DIRECTORY + System.getProperty("file.separator") + GLASSFISH_DEFAULT_KEYSTORE_NAME;
    private static final String GLASSFISH_DEFAULT_CERTIFICATE_ALIAS = "s1as";
    private static final char[] GLASSFISH_DEFAULT_KEYSTORE_PASSWORD = new char[]{'c', 'h', 'a', 'n', 'g', 'e', 'i', 't'};
    private static final int DEFAULT_SYMMETRIC_ENCRYPTION_KEY_LENGTH = 16;
    private static final int DEFAULT_SYMMETRIC_ENCRYPTION_IV_LENGTH = 16;
    
    private static GlassFishCryptographicHelper glassFishCryptographicHelper = null;
    
    private X509Certificate glassFishDefaultX509Certificate = null;
    private PublicKey glassFishDefaultX509CertificatePublicKey = null;
    private PrivateKey glassFishDefaultX509CertificatePrivateKey = null;
    
    private byte[] glassFishDefaultSymmetricEncryptionKey = null;
    private byte[] glassFishDefaultSymmetricEncryptionIv = null;    
    
    
    
    public GlassFishCryptographicHelper()
    {
        doInitializeGlassFishCryptographicHelper();
    }
    
    
    
    public static GlassFishCryptographicHelper getInstanceOf()
    {
        if (glassFishCryptographicHelper == null)
        {
            glassFishCryptographicHelper = new GlassFishCryptographicHelper();
        }

        return glassFishCryptographicHelper;
    }
    
    
    
    private void doInitializeGlassFishCryptographicHelper()
    {
        glassFishDefaultX509Certificate = AdvancedCryptographicHelper.getInstance().loadX509CertificateFromKeyStore(GLASSFISH_DEFAULT_KEYSTORE_TYPE, GLASSFISH_DEFAULT_CERTIFICATE_ALIAS, GLASSFISH_DEFAULT_KEYSTORE_PASSWORD, GLASSFISH_DEFAULT_KEYSTORE_PATH);
        
        KeyPair keyPair = AdvancedCryptographicHelper.getInstance().loadX509CertificateKeyPairFromKeyStore(GLASSFISH_DEFAULT_KEYSTORE_TYPE, GLASSFISH_DEFAULT_CERTIFICATE_ALIAS, GLASSFISH_DEFAULT_KEYSTORE_PASSWORD, GLASSFISH_DEFAULT_KEYSTORE_PATH);
        glassFishDefaultX509CertificatePublicKey = keyPair.getPublic();
        glassFishDefaultX509CertificatePrivateKey = keyPair.getPrivate();
        
        byte[] privateKeyByteArray = glassFishDefaultX509CertificatePrivateKey.getEncoded();
        glassFishDefaultSymmetricEncryptionKey = new byte[DEFAULT_SYMMETRIC_ENCRYPTION_KEY_LENGTH];
        glassFishDefaultSymmetricEncryptionIv = new byte[DEFAULT_SYMMETRIC_ENCRYPTION_IV_LENGTH];
        
        for (int i = 0; i < DEFAULT_SYMMETRIC_ENCRYPTION_KEY_LENGTH; i++)
        {
            glassFishDefaultSymmetricEncryptionKey[i] = privateKeyByteArray[i];
        }
        
        for (int i = 0; i < DEFAULT_SYMMETRIC_ENCRYPTION_IV_LENGTH; i++)
        {
            glassFishDefaultSymmetricEncryptionIv[i] = privateKeyByteArray[privateKeyByteArray.length - DEFAULT_SYMMETRIC_ENCRYPTION_IV_LENGTH - i];
        }       
    }

    
    
    public X509Certificate getGlassFishDefaultX509Certificate() {
        return glassFishDefaultX509Certificate;
    }

    public void setGlassFishDefaultX509Certificate(X509Certificate glassFishDefaultX509Certificate) {
        this.glassFishDefaultX509Certificate = glassFishDefaultX509Certificate;
    }

    public PublicKey getGlassFishDefaultX509CertificatePublicKey() {
        return glassFishDefaultX509CertificatePublicKey;
    }

    public void setGlassFishDefaultX509CertificatePublicKey(PublicKey glassFishDefaultX509CertificatePublicKey) {
        this.glassFishDefaultX509CertificatePublicKey = glassFishDefaultX509CertificatePublicKey;
    }

    public PrivateKey getGlassFishDefaultX509CertificatePrivateKey() {
        return glassFishDefaultX509CertificatePrivateKey;
    }

    public void setGlassFishDefaultX509CertificatePrivateKey(PrivateKey glassFishDefaultX509CertificatePrivateKey) {
        this.glassFishDefaultX509CertificatePrivateKey = glassFishDefaultX509CertificatePrivateKey;
    }

    public byte[] getGlassFishDefaultSymmetricEncryptionKey() {
        return glassFishDefaultSymmetricEncryptionKey;
    }

    public void setGlassFishDefaultSymmetricEncryptionKey(byte[] glassFishDefaultSymmetricEncryptionKey) {
        this.glassFishDefaultSymmetricEncryptionKey = glassFishDefaultSymmetricEncryptionKey;
    }

    public byte[] getGlassFishDefaultSymmetricEncryptionIv() {
        return glassFishDefaultSymmetricEncryptionIv;
    }

    public void setGlassFishDefaultSymmetricEncryptionIv(byte[] glassFishDefaultSymmetricEncryptionIv) {
        this.glassFishDefaultSymmetricEncryptionIv = glassFishDefaultSymmetricEncryptionIv;
    }
}