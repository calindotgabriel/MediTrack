package ro.meditrack.model;
/**
 * Pharmacy class model, used to store info about one entity.
 */
public class Farmacie {


    private int id;
    private String name;
    private String[] openHours;
    private String vicinity;
    private String phNumber;
    private String url;
    private int icon = - 1;
    private double lat = -1.0;
    private double lng = -1.0;
    private int compensat;
    private boolean openNow;


    public Farmacie() {

    }

    public Farmacie(String name, String[] openHours, String vicinity, int ic_sensiblu, double lat, double lng, int compensat, String phNumber, String url, boolean openNow) {
    }

    public Farmacie(int id, String name, String[] openHours, String vicinity, int icon, double lat, double lng, int compensat, String phNumber, String url, boolean openNow) {
        this.id = id;
        this.name = name;
        this.openHours = openHours;
        this.vicinity = vicinity;
        this.icon = icon;
        this.lat = lat;
        this.lng = lng;
        this.compensat = compensat;
        this.phNumber = phNumber;
        this.url = url;
        this.openNow = openNow;
    }

    public boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getName() {
        return name;
    }

    public String[] getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String[] openHours) {
        this.openHours = openHours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isNonstop() {
        if (this.openHours[0].equals( "nonstop"))
            return true;
        if (this.openNow)
            return true;
        return false;
    }

    public Farmacie(String name, String[] openHours, String vicinity) {
        this.name = name;
        this.openHours = openHours;
        this.vicinity = vicinity;
    }



    public Farmacie(String name, String openHours[], String vicinity, int icon) {
        this.name = name;
        this.openHours = openHours;
        this.vicinity = vicinity;
        this.icon = icon;
    }

    public int getCompensat() {
        return compensat;
    }

    public void setCompensat(int compensat) {
        this.compensat = compensat;
    }

    public Farmacie(String name, String[] openHours, String vicinity, int icon, double lat, double lng) {
        this.name = name;
        this.openHours = openHours;
        this.vicinity = vicinity;
        this.icon = icon;

        this.lat = lat;
        this.lng = lng;
    }

    public Farmacie(String name, String[] openHours, String vicinity, int icon, double lat, double lng, int compensat) {
        this.name = name;
        this.openHours = openHours;
        this.vicinity = vicinity;
        this.icon = icon;
        this.lat = lat;
        this.lng = lng;
        this.compensat = compensat;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Farmacie(String name, String[] openHours, String vicinity, int icon, double lat, double lng, int compensat, boolean openNow) {
        this.name = name;
        this.openHours = openHours;
        this.vicinity = vicinity;
        this.icon = icon;
        this.lat = lat;
        this.lng = lng;
        this.compensat = compensat;
        this.openNow = openNow;
    }



    public int getOpenNowInt() {
        if (this.openNow)
           return 1;
        return 0;
    }
}
