package back.controller;

import static spark.Spark.post;

import java.util.ArrayList;
import java.util.Optional;

import back.App;
import back.model.Reservation;
import back.response.Error;
import back.response.Success;

public class AdminController {

    static String passwordHash = "";
    static String salt = "";

    public static boolean verifyPassword(String password) {
        
        return true;
    }
    
    public static void init() {
        post("/admin/reservation/pending/all", (req, res) -> {
            return App.gson.toJson(ReservationController.pendingResa);
        });
        post("/admin/reservation/accepted/all", (req, res) -> {
            return App.gson.toJson(ReservationController.acceptedResa);
        });
        post("/admin/reservation/refused/all", (req, res) -> {
            return App.gson.toJson(ReservationController.refusedResa);
        });
        post("/admin/reservation/refuse/:uuid", (req, res) -> {
            var uuid = req.params(":uuid");
            var removed = removeByUuid(ReservationController.pendingResa, uuid);
            if (!removed.isPresent()) return Error.send(res, "No item with "+uuid+" to remove !");
            ReservationController.refusedResa.add(removed.get());
            return Success.send(res, "reservation successfully refused");
        });
        post("/admin/reservation/accept/:uuid", (req, res) -> {
            var uuid = req.params(":uuid");
            var removed = removeByUuid(ReservationController.pendingResa, uuid);
            if (!removed.isPresent()) return Error.send(res, "No item with "+uuid+" to remove !");
            ReservationController.acceptedResa.add(removed.get());
            return Success.send(res, "reservation successfully accepted");
        });
    }

    public static Optional<Reservation> removeByUuid(ArrayList<Reservation> list, String uuid) {
        return list
                .stream()
                    .filter(r -> r.uuid.equals(uuid))
                    .findFirst()
                    .map(r -> {
                        ReservationController.pendingResa.remove(r);
                        return r;
                    });
    }
}
