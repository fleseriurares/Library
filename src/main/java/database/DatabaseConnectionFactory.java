package database;

public class DatabaseConnectionFactory {
    public static final String SCHEMA = "library";
    public static final String TEST_SCHEMA = "test_library";

    public static JDBConnectionWrapper getConnectionWrapper(boolean test){
        if(test){
            return new JDBConnectionWrapper(TEST_SCHEMA);
        }
        else{
            return new JDBConnectionWrapper(SCHEMA);
        }
    }
}
