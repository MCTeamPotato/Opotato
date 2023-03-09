package com.teampotato.opotato.util.nec;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public final class SSLUtils {

    /** Trusts certificates in a key store on top of the ones currently trusted by wrapping the TrustManager */
    public static void trustCertificates(KeyStore keyStore) {
        try {
            // Init TFM with default trust store
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            final X509TrustManager defaultTrustManager = getX509TrustManager(trustManagerFactory);

            // Init TMF with new trust store
            trustManagerFactory.init(keyStore);
            final X509TrustManager customTrustManager = getX509TrustManager(trustManagerFactory);

            // Create a trust manager that wraps the default one
            X509TrustManager wrappingTrustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return joinArrays(defaultTrustManager.getAcceptedIssuers(), customTrustManager.getAcceptedIssuers());
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    try {
                        customTrustManager.checkServerTrusted(chain, authType);
                    } catch (CertificateException e) {
                        defaultTrustManager.checkServerTrusted(chain, authType);
                    }
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    try {
                        customTrustManager.checkClientTrusted(chain, authType);
                    } catch (CertificateException e) {
                        defaultTrustManager.checkClientTrusted(chain, authType);
                    }
                }
            };

            // Replace the default SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{wrappingTrustManager}, null);
            SSLContext.setDefault(sslContext);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static X509TrustManager getX509TrustManager(TrustManagerFactory trustManagerFactory) {
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new RuntimeException("Failed to find X509TrustManager");
    }

    private static <T> T[] joinArrays(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
