package com.google.code.gsonrmi.transport.tcp;

import com.google.code.gsonrmi.transport.Transport;
import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cristi
 * Date: 8/2/13
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class TcpProxyFactory {

    public static TcpProxy reflectTcpProxy(Class className,List<InetSocketAddress> inetSocketAddressList, Transport transport
            ,Gson gson,KeyStore keyStore )  {

        try {
            Class myClass = Class.forName(className.getName());

        Class[] argTypes = {int.class};
            Constructor[] constructors = myClass.getConstructors();
            Object[] arguments = {inetSocketAddressList,transport,gson};
            Constructor constructor = constructors[0];
            TcpProxy instance = (TcpProxy) constructor.newInstance(arguments);
            instance.setKeyStore(keyStore);

            return instance;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }
}
