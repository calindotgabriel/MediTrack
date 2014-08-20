package ro.meditrack.gson;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;

/**
 * @author motan
 * @date 7/5/14
 */
public class GsonHandler {

    KeyStore keyStore;
    GsonClient clientInstance;

    public GsonHandler(InputStream is) throws IOException{
        setKeystore(is);
        clientInstance = GsonClient.getInstance(keyStore);
    }

    public void setKeystore(InputStream inputStream) {

        KeyStore localTrustStore;

        try {
            localTrustStore = KeyStore.getInstance("BKS");
            localTrustStore.load(inputStream, "123456".toCharArray());
            keyStore = localTrustStore;

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
