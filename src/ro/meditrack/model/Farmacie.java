package ro.meditrack.model;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


@DatabaseTable (tableName = "farmacii")
public class Farmacie  implements Serializable, ItemInterface {

    @DatabaseField
    private int id;
    @DatabaseField (id = true, canBeNull = false)
    private String placesId;
    @DatabaseField (canBeNull = false)
    private String name;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private String[] openHours;

    @DatabaseField
    private String vicinity;
    @DatabaseField
    private String phNumber;
    @DatabaseField
    private String url;
    @DatabaseField
    private int icon = - 1;
    @DatabaseField
    private double lat;
    @DatabaseField
    private double lng;
    @DatabaseField
    private int compensat_da;
    @DatabaseField
    private int compensat_nu;
    @DatabaseField
    private int openNow;


    public Farmacie() {

    }

    public String getName() {
        return name;
    }

    public String getPlacesId() {
        return placesId;
    }

    public void setPlacesId(String placesId) {
        this.placesId = placesId;
    }

    public boolean getOpenNow() {
        return openNow>0;
    }
    public void setOpenNow(int openNow) {
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

    public String getItemDescription() {
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

    public LatLng getLatLng() {
        return new LatLng(this.getLat(), this.getLng());
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isNonstop() {
        if (this.openHours[0].equals( "nonstop"))
            return true;
        if (this.openNow > 0)
            return true;
        return false;
    }

    public int getCompensatDa() {
        return compensat_da;
    }
    public void setCompensatDa(int compensat_da) {
        this.compensat_da = compensat_da;
    }

    public int getCompensatNu() {
        return compensat_nu;
    }
    public void setCompensatNu(int compensat_nu) {
        this.compensat_nu = compensat_nu;
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


}
