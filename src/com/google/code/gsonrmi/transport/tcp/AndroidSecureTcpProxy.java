package com.google.code.gsonrmi.transport.tcp;

import com.google.code.gsonrmi.transport.Transport;
import com.google.gson.Gson;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cristi
 * Date: 8/2/13
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class AndroidSecureTcpProxy extends TcpProxy {


    public AndroidSecureTcpProxy(List<InetSocketAddress> listeningAddresses, Transport transport, Gson serializer) throws IOException {
        super(listeningAddresses, transport, serializer);
    }

    @Override
    protected Socket buildSocket(String token, int i) {

        try {

            // Load the self-signed server certificate
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

// Create a SSLContext with the certificate
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            SSLSocket sslsocket = null;
            Socket socket= new Socket(token,i);
            sslsocket = (SSLSocket) sslSocketFactory.createSocket(socket,token,i,false);

            return sslsocket;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (KeyStoreException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (KeyManagementException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


    @Override
    protected ServerSocket buildServerSocket() throws IOException {

        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket();

        return sslserversocket;


    }
}