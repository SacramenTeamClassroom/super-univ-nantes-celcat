package back;

import static spark.Spark.port;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import back.celcatjson.CelcatJson;

public class App {

    public static GsonBuilder builder;
    public static Gson gson;
    
    public static void main(String[] args) {
        builder = new GsonBuilder();
        gson = builder.create();
        CelcatJson.GetAll();
        start();

    }

    public static void start() {
        var port = 4567;
        port(port);
        System.out.println("Start on port " + port);
    }

}
