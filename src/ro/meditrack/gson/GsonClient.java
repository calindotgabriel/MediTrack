package ro.meditrack.gson;

import android.util.Log;
import com.google.code.gsonrmi.Parameter;
import com.google.code.gsonrmi.RpcError;
import com.google.code.gsonrmi.annotations.RMI;
import com.google.code.gsonrmi.serializer.ExceptionSerializer;
import com.google.code.gsonrmi.serializer.ParameterSerializer;
import com.google.code.gsonrmi.transport.Route;
import com.google.code.gsonrmi.transport.Transport;
import com.google.code.gsonrmi.transport.rmi.Call;
import com.google.code.gsonrmi.transport.rmi.RmiService;
import com.google.code.gsonrmi.transport.tcp.AndroidSecureTcpProxy;
import com.google.code.gsonrmi.transport.tcp.TcpProxy;
import com.google.code.gsonrmi.transport.tcp.TcpProxyFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ro.meditrack.Keys;
import ro.meditrack.model.Farmacie;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;

/**
 * Created by adrian on 1/10/14.
 */
public class GsonClient {
    private static String TAG = "GsonClient";
    private static GsonClient ourInstance ;
    private Gson gson;
    private Transport transport;
    private List<Farmacie> pharmacies;



    public static GsonClient getInstance(KeyStore keyStore) {
        if (ourInstance==null){

            ourInstance = new GsonClient();
            ourInstance.gson = new GsonBuilder()
                    .registerTypeAdapter(Exception.class, new ExceptionSerializer())
                    .registerTypeAdapter(Parameter.class, new ParameterSerializer()).create();

            ourInstance.transport = new Transport();

            InetSocketAddress inetSocketAddress = new InetSocketAddress(30001);

            List<InetSocketAddress> listeningAddresses =
                                                Arrays.asList(inetSocketAddress);



            TcpProxy tcpProxy;
            tcpProxy = TcpProxyFactory.reflectTcpProxy(AndroidSecureTcpProxy.class, listeningAddresses, ourInstance.transport,
                    ourInstance.gson, keyStore);

            tcpProxy.start();


            RmiService rmiService = null;

            try {
                rmiService = new RmiService(ourInstance.transport, ourInstance.gson);

                rmiService.start();


                URI uri = null;

                try {
                    uri = new URI("rmi:service");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                Route target = new Route(uri);
                Call call = new Call(target, "register", "client", ourInstance);
                call.send(ourInstance.transport);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }


        return ourInstance;
    }


    public static GsonClient getSimpleInstance() {
        return ourInstance;
    }

    private GsonClient() {
    }


    public String sendInfo(String text, double lat, double lng){

        Route to=null;
        try {
            to=new Route(new URI("tcp://192.168.0.104:30310"),new URI("rmi:server"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI from=null;
        try {
            from=new URI("rmi:client");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        Call basic = new Call(to, "test", text, lat, lng);  // send to the method 'test' of server

        Call ack = basic.callback(from, "ack"); // 'test' returns to 'ack'


        ack.send(transport);
//          basic.send(transport);

        return null;
    }

    @RMI
    public void ack (List<Farmacie> pharmacies, RpcError rpcError){
        // acum au ajuns stringurile


        this.pharmacies = pharmacies;

        for (Farmacie pharmacy : pharmacies) {
            Log.v(TAG, pharmacy.getName() + " - " + pharmacy.getVicinity() + " - " +
                    pharmacy.getLat() + " / " + pharmacy.getLng() + " - " + pharmacy.getOpenNow()
            );
        }

}



    public List<Farmacie> getPharmacies() {
            return pharmacies;
    }





}

