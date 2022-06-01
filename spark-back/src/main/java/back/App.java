package back;

import static spark.Spark.port;
import static spark.Spark.before;
import static spark.Spark.options;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import back.celcatjson.CelcatJson;
import back.controller.AdminController;
import back.controller.CelcatController;
import back.controller.ReservationController;
import back.model.Password;

public class App {

    public static GsonBuilder builder;
    public static Gson gson;
    
    public static void main(String[] args) {

        try {
            builder = new GsonBuilder();
            gson = builder.create();
            Password.generateHashAndSalt("admin");
            CelcatJson.GetAll();
            Password.getFromFile();
        } catch (Exception e) {

        }

        start();

    }

    public static void start() {
        var port = 4597;
        port(port);
        addCors();
        AdminController.init();
        CelcatController.init();
        ReservationController.init();
        System.out.println("Start on port " + port);
    }

    public static void addCors() {
        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
    
            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
    
            return "OK";
        });
    
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "*");
            res.type("application/json");
        });
    }

}
