package back;

import static spark.Spark.port;

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
            CelcatJson.GetAll();
            Password.getFromFile();
        } catch (Exception e) {

        }

        start();

    }

    public static void start() {
        var port = 4597;
        port(port);
        AdminController.init();
        CelcatController.init();
        ReservationController.init();
        System.out.println("Start on port " + port);
    }

}
