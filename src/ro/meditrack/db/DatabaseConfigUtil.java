package ro.meditrack.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * @author motan
 * @date 8/9/14
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    public static void main(String[] args) throws Exception{
        writeConfigFile("ormlite_config.txt");
    }
}
