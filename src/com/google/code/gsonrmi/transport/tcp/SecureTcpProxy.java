package com.google.code.gsonrmi.transport.tcp;

import com.google.code.gsonrmi.transport.Transport;
import com.google.gson.Gson;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SecureTcpProxy extends TcpProxy {


    public SecureTcpProxy(List<InetSocketAddress> listeningAddresses, Transport transport, Gson serializer) throws IOException {
        super(listeningAddresses, transport, serializer);
    }

    @Override
    protected Socket buildSocket(String token, int i) throws IOException {

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        Socket socket = new Socket(token,i);
        SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(socket,token, i,false);

        return sslsocket;
    }

    @Override
    protected ServerSocket buildServerSocket() throws IOException {

        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket();

        return sslserversocket;

    }

}
