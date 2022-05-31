package back.response;

import spark.Response;

public class Success {
    public static String send(Response res, String msg) {
        res.status(200);
        return "{\"success\":\""+msg+"\"}";
    }
}