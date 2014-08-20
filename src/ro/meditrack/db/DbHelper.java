package ro.meditrack.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ro.meditrack.R;
import ro.meditrack.model.Farmacie;

import java.sql.SQLException;

/**
 * @author motan
 * @date 8/9/14
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "meditrack.db";
    private static final int DATABASE_VERSION = 2;

    private Dao<Farmacie, Integer> farmaciiDao = null;
    private RuntimeExceptionDao<Farmacie, Integer> farmaciiRuntimeDao = null;


    public DbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            Log.i(DbHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Farmacie.class);
        } catch (Exception e) {
            Log.e(DbHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int i, int i2) {
        try {
            Log.i(DbHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Farmacie.class, true);
            onCreate(db, connectionSource);
        } catch (Exception e) {
            Log.e(DbHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public void resetDb() {
        try {
            TableUtils.dropTable(getConnectionSource(), Farmacie.class, true);
            onCreate(getWritableDatabase(), getConnectionSource());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Dao<Farmacie, Integer> getDao() throws SQLException {
        if (farmaciiDao == null) {
            farmaciiDao = getDao(Farmacie.class);
        }
        return farmaciiDao;
    }

    public RuntimeExceptionDao<Farmacie, Integer> getRuntimeDao() {
        if (farmaciiRuntimeDao == null) {
            farmaciiRuntimeDao = getRuntimeExceptionDao(Farmacie.class);
        }
        return farmaciiRuntimeDao;
    }


    @Override
    public void close() {
        super.close();
        farmaciiDao = null;
        farmaciiRuntimeDao = null;
    }
}
