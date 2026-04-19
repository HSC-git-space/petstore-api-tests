package constants;

public class Endpoints {

    public static final String PET_BY_ID      = "/pet/{petId}";
    public static final String PET_ADD         = "/pet";
    public static final String PET_UPDATE      = "/pet";
    public static final String PET_DELETE      = "/pet/{petId}";
    public static final String PET_BY_STATUS   = "/pet/findByStatus";

    public static final String ORDER_BY_ID     = "/store/order/{orderId}";
    public static final String ORDER_PLACE     = "/store/order";
    public static final String STORE_INVENTORY = "/store/inventory";

    public static final String USER_CREATE     = "/user";
    public static final String USER_BY_NAME    = "/user/{username}";
    public static final String USER_LOGIN      = "/user/login";
}