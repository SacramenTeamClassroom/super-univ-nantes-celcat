package back;

import static spark.Spark.port;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App {

    public static GsonBuilder builder;
    public static Gson gson;
    
    public static void main(String[] args) {
        builder = new GsonBuilder();
        gson = builder.create();
        start();

    }

    public static void start() {
        var port = 4567;
        port(port);
        System.out.println("Start on port " + port);
    }

}