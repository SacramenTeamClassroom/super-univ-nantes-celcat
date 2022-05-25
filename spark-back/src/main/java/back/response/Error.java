package back.response;

import spark.Response;

public class Error {
    public static String send(Response res, String msg) {
        res.status(400);
        return "{\"error\":\""+msg+"\"}";
    }
}
