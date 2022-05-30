package back.controller;

import static spark.Spark.post;

import java.util.ArrayList;
import java.util.UUID;
import back.App;
import back.celcatjson.CelcatJson;
import back.model.Reservation;
import back.response.Error;
import back.response.Success;

public class ReservationController {

    public static ArrayList<Reservation> refusedResa    = new ArrayList<>();
    public static ArrayList<Reservation> acceptedResa   = new ArrayList<>();
    public static ArrayList<Reservation> pendingResa    = new ArrayList<>();

    public static void init() {
        post("/reservation", (req, res) -> {
            var resa = App.gson.fromJson(req.body(), Reservation.class);
            var timestamp = System.currentTimeMillis();
            if (resa.end < timestamp) return Error.send(res, "Cannot reserve a in the past! ( the end date is before the current date )");
            if (CelcatJson.isInConflict(resa)) return Error.send(res, "Cannot reserve an already taken time slot");
            resa.uuid = UUID.randomUUID().toString();
            resa.timestamp = timestamp;
            pendingResa.add(resa);
            return Success.send(res, resa.uuid);
        });
    }
}
