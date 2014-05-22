package ro.meditrack.utils;

/**
 * Helper class used to help us store string arrays as strings
 * by converting using a separator.
 */
public class DbStringConvert {
    public static String strSeparator = "__,__";

    /**
     * Converts a string array to a string with separators.
     * @return ready to use converted string
     */
    public static String convertArrayToString(String[] array){

        String str = "";
        try {
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Converts the formatted string back to a arraylist.
     * @return ready to use arraylist
     */
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
