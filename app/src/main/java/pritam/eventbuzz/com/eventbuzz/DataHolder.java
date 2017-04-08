package pritam.eventbuzz.com.eventbuzz;

/**
 * Created by pritam on 6/4/17.
 */

public class DataHolder {
    private static String email;
    public static String getEmail(){ return email; }
    public static void setEmail(String user_email){ email = user_email; }

    private static final DataHolder dataHolder = new DataHolder();
    public static DataHolder getInstance() { return dataHolder; }
}
