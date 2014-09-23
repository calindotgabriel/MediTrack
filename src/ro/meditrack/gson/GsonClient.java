package ro.meditrack.gson;

import android.content.Context;
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
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import ro.meditrack.Keys;
import ro.meditrack.db.DbHelper;
import ro.meditrack.exception.GsonInstanceNullException;
import ro.meditrack.model.Farmacie;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;

public class GsonClient {
    private static String TAG = "GsonClient";
    private static GsonClient ourInstance ;
    private Gson gson;
    private Transport transport;

    private List<Farmacie> pharmacies;

    private String SERVER_IP = "calindotgabriel.ddns.net";
    private String SERVER_PORT = "30310";

    private Context mContext;

    private DbHelper dbHelper;
    
/*    public static void registerKeystore() {

    }*/


    public static GsonClient getInstance(KeyStore keyStore) {
        if (ourInstance == null){

            ourInstance = new GsonClient();
            ourInstance.gson = new GsonBuilder()
                    .registerTypeAdapter(Exception.class, new ExceptionSerializer())
                    .registerTypeAdapter(Parameter.class, new ParameterSerializer()).create();

            ourInstance.transport = new Transport();

            InetSocketAddress inetSocketAddress = new InetSocketAddress(30001);

            List<InetSocketAddress> listeningAddresses =
                                                Arrays.asList(inetSocketAddress);


            TcpProxy tcpProxy;
            tcpProxy = TcpProxyFactory.reflectTcpProxy
                    (AndroidSecureTcpProxy.class,
                    listeningAddresses,
                    ourInstance.transport,
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


    public static GsonClient getSimpleGsonInstance() throws GsonInstanceNullException{
        if (ourInstance == null)
           throw new GsonInstanceNullException();
        return ourInstance;
    }

    private GsonClient() {
    }

    private Route to;
    private URI from;

    public void processURIs () {

        try {
            to = new Route
                    (new URI("tcp://" + SERVER_IP + ":" + SERVER_PORT),
                            new URI("rmi:server"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            from = new URI("rmi:client");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void getPharmaciesFromSv(String text, double lat, double lng){
        processURIs();

        Call basic = new Call(to, "test", text, lat, lng);  // send to the method 'test' of server
        Call ack = basic.callback(from, "ack"); // 'test' returns to 'ack'

        ack.send(transport);
    }

    public void contactCompensatField(String googleId, boolean state) {
        processURIs();

        Call basic = new Call(to, "setCompensatField", googleId, state);
        Call response = basic.callback(from, "gotCompensatResponse");

        response.send(transport);
    }


    @RMI
    public void ack (List<Farmacie> pharmacies, RpcError rpcError){

        this.pharmacies = pharmacies;

        for (Farmacie f : pharmacies) {
            Log.v(TAG, f.getName() + " - " + f.getVicinity() + " - " +
                    f.getLat() + " / " + f.getLng() + " - " + f.getOpenNow()
                            + " - " + f.getPlacesId()
            );
        }
}

    @RMI
    public void gotCompensatResponse(Farmacie f, RpcError rpcError) {

        RuntimeExceptionDao<Farmacie, Integer> mDao =
                getHelper().getRuntimeDao();

        mDao.update(f);

    }


    public List<Farmacie> getPharmacies() {
            return pharmacies;
    }

    public void clearPharmacies() {
        pharmacies = null;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    private DbHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(mContext, DbHelper.class);
        }
        return dbHelper;
    }


    public String getServerIp() {
        return SERVER_IP;
    }
    public void setServerIp(String SERVER_IP) {
        this.SERVER_IP = SERVER_IP;
    }

    public String getServerPort() {
        return SERVER_PORT;
    }
    public void setServerPort(String SERVER_PORT) {
        this.SERVER_PORT = SERVER_PORT;
    }


}

