package ro.meditrack.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ro.meditrack.model.Farmacie;
import ro.meditrack.model.Medicament;
import ro.meditrack.utils.DbStringConvert;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHandler class, core DB class powered by SQLITE.
 * @author motan
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler INSTANCE;

    private static final int DATABASE_VERSION = 47;

    private static final String DATABASE_NAME = "meditrack";

    // Contacts table name
    private static final String TABLE_FARMACII = "farmacii";
    private static final String TABLE_MEDICAMENTE = "medicamente";

    // Contacts Table Columns names
    private static final String F_KEY_ID = "id";
    private static final String F_KEY_NAME = "name";
    private static final String F_KEY_ORAR = "orar";
    private static final String F_KEY_ADRESA = "adresa";
    private static final String F_KEY_ICON = "icon";
    private static final String F_KEY_LAT = "lat";
    private static final String F_KEY_LNG = "lng";
    private static final String F_KEY_CMP = "compensat";
    private static final String F_KEY_PH = "numar";
    private static final String F_KEY_URL = "url";
    private static final String F_KEY_ON = "opennow";

    private static final String M_KEY_ID = "id";
    private static final String M_KEY_NAME = "name";
    private static final String M_KEY_DESCRIERE = "descriere";


    /**
     * Instance provider of class, making sure you don't accidentally leak an Activity's context.
     *
     * @param context application context
     * @return db instance
     */
    public static DatabaseHandler getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHandler(context.getApplicationContext());
        }
        return INSTANCE;
    }

    /**
     * Class constructor
     * @param context the activity context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Here we create pharmacy and medicamente tables
     *
     * @param db desired database
     * @return created tables.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FARMACII_TABLE = "CREATE TABLE " + TABLE_FARMACII + "("
                + F_KEY_ID + " INTEGER PRIMARY KEY, " + F_KEY_NAME + " TEXT, "
                + F_KEY_ORAR + " TEXT, " + F_KEY_ADRESA + " TEXT, "
                + F_KEY_ICON + " INTEGER, "
                + F_KEY_LAT + " REAL, " + F_KEY_LNG + " REAL, "
                + F_KEY_CMP + " INTEGER, " + F_KEY_PH + " TEXT, "
                + F_KEY_URL + " TEXT, " + F_KEY_ON + " INTEGER " + " )";

        String CREATE_MEDICAMENTE_TABLE = "CREATE TABLE " + TABLE_MEDICAMENTE + " ("
                + M_KEY_ID + " INTEGER PRIMARY KEY, " + M_KEY_NAME + " TEXT, "
                + M_KEY_DESCRIERE + " TEXT" + ")";

        db.execSQL(CREATE_FARMACII_TABLE);
        db.execSQL(CREATE_MEDICAMENTE_TABLE);
    }


    /**
     * Add new pharmacy to db.
     *
     * @param farmacie, Farmacie type object
     */
    public void addFarmacie(Farmacie farmacie) {
        SQLiteDatabase db = this.getWritableDatabase();

        int oNow;

        ContentValues values = new ContentValues();
        values.put(F_KEY_NAME, farmacie.getName());
        values.put(F_KEY_ORAR, DbStringConvert.convertArrayToString(farmacie.getOpenHours())); // transform String array in String
        values.put(F_KEY_ADRESA, farmacie.getVicinity());
        values.put(F_KEY_ICON, farmacie.getIcon());
        values.put(F_KEY_LAT, farmacie.getLat());
        values.put(F_KEY_LNG, farmacie.getLng());
        values.put(F_KEY_CMP, farmacie.getCompensat());
        values.put(F_KEY_PH, farmacie.getPhNumber());
        values.put(F_KEY_URL, farmacie.getUrl());

        if (farmacie.getOpenNow())
            oNow = 1;
        else
            oNow = 0;

        values.put(F_KEY_ON, oNow);

        // Inserting Row
        db.insert(TABLE_FARMACII, null, values);
        db.close(); // Closing database connection
    }


    /**
     * Add new drug to db.
     *
     * @param medicament, Medicament type object
     */
    public void addMedicament(Medicament medicament) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(M_KEY_NAME, medicament.getName());
        values.put(M_KEY_DESCRIERE, medicament.getDescirere());

        db.insert(TABLE_MEDICAMENTE, null, values);
        db.close();
    }

    /**
     * Get a list of all farmacies in table.
     */
    public List<Farmacie> getAllFarmacii() {
        List<Farmacie> farmaciiList = new ArrayList<Farmacie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FARMACII;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Farmacie farmacie = new Farmacie();
                farmacie.setId(Integer.parseInt(cursor.getString(0)));
                farmacie.setName(cursor.getString(1));
                farmacie.setOpenHours(DbStringConvert.convertStringToArray(cursor.getString(2)));
                farmacie.setVicinity(cursor.getString(3));
                farmacie.setIcon(cursor.getInt(4));
                farmacie.setLat(cursor.getDouble(5));
                farmacie.setLng(cursor.getDouble(6));
                farmacie.setCompensat(cursor.getInt(7));
                farmacie.setPhNumber(cursor.getString(8));
                farmacie.setUrl(cursor.getString(9));

                boolean openNow = false;
                if (cursor.getInt(10) == 1)
                    openNow = true;


                farmacie.setOpenNow(openNow);


                farmaciiList.add(farmacie);

            } while (cursor.moveToNext());
        }

        return farmaciiList;
    }


    /**
     * Get a list of all drugs in table.
     */
    public List<Medicament> getAllMedicamente() {
        List<Medicament> medicamenteList = new ArrayList<Medicament>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MEDICAMENTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Medicament medicament = new Medicament();
                medicament.setId(Integer.parseInt(cursor.getString(0)));
                medicament.setName(cursor.getString(1));
                medicament.setDescirere(cursor.getString(2));

                medicamenteList.add(medicament);
            } while (cursor.moveToNext());
        }

        return medicamenteList;
    }


    /**
     * Checks if we already have items in our pharmacies table.
     */
    public boolean isFarmaciiTableEmpty() {
        String query = "SELECT * FROM " + TABLE_FARMACII;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0)
           return false;
        return true;
    }

    /**
     * Checks if we already have items in our drugs table.
     */
    public boolean isMedicamenteTableEmpty() {
        String query = "SELECT * FROM " + TABLE_MEDICAMENTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0)
           return false;
        return true;
    }


    /**
     * If the db is upgraded we need to drop it and make a new one.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMACII);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICAMENTE);
        // Create tables again
        onCreate(db);
    }

    public void dropDB () {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMACII);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICAMENTE);
        onCreate(db);
    }

    /**
     * If the db is downgraded we need to drop it and make a new one.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARMACII);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICAMENTE);
        // Create tables again
        onCreate(db);
    }
}
