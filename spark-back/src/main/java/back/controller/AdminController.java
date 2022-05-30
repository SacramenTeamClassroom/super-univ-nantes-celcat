package back.controller;

import static spark.Spark.post;


import java.util.ArrayList;
import java.util.Optional;

import back.App;
import back.model.Password;
import back.model.Reservation;
import back.response.Error;
import back.response.Success;
import spark.Request;
import spark.Response;

public class AdminController {
    
    public static void init() {

        post("/admin/reservation/pending/all", (req, res) -> {
            if (!guard(req, res)) return guardFail(res);
            return App.gson.toJson(ReservationController.pendingResa);
        });

        post("/admin/reservation/accepted/all", (req, res) -> {
            if (!guard(req, res)) return guardFail(res);
            return App.gson.toJson(ReservationController.acceptedResa);
        });

        post("/admin/reservation/refused/all", (req, res) -> {
            if (!guard(req, res)) return guardFail(res);
            return App.gson.toJson(ReservationController.refusedResa);
        });

        post("/admin/reservation/refuse/:uuid", (req, res) -> {
            if (!guard(req, res)) return guardFail(res);
            var uuid = req.params(":uuid");
            var removed = removeByUuid(ReservationController.pendingResa, uuid);
            if (!removed.isPresent()) return Error.send(res, "No item with "+uuid+" to remove !");
            ReservationController.refusedResa.add(removed.get());
            return Success.send(res, "reservation successfully refused");
        });

        post("/admin/reservation/accept/:uuid", (req, res) -> {
            if (!guard(req, res)) return guardFail(res);
            var uuid = req.params(":uuid");
            var removed = removeByUuid(ReservationController.pendingResa, uuid);
            if (!removed.isPresent()) return Error.send(res, "No item with "+uuid+" to remove !");
            ReservationController.acceptedResa.add(removed.get());
            return Success.send(res, "reservation successfully accepted");
        });

    }

    public static boolean guard(Request req, Response res) {
        var password = req.headers("Authorization");
        return Password.verify(password);
    }

    
    public static String guardFail(Response res) {
        return Error.send(res, "Invalid password!");
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
