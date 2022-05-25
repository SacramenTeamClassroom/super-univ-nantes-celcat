package back.controller;

import static spark.Spark.get;

import static back.celcatjson.CelcatJson.jsonCreneau;

public class CelcatController {
    
    public static void init() {
        get("/celcat", (req, res) -> {
            return jsonCreneau;
        });
    }
}
