package back.celcatjson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import back.model.Creneau;
import back.model.Reservation;

public class CelcatJson {

    public static Long currentTimestamp = System.currentTimeMillis();
    public static Boolean inBlock = false;
    public static ArrayList<Creneau> allCreaneau = new ArrayList<>();
    public static Creneau currentCreneau = new Creneau();

    public static String jsonCreneau = "";

    public static void GetAll() {
        try {
            currentTimestamp = System.currentTimeMillis();
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r1315.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r1316.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r1318.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r1299.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r89806.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r1300.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r89807.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r89808.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r1331.ics");
            parseIcs("https://edt.univ-nantes.fr/iut_nantes_pers/r1330.ics");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            
            jsonCreneau = gson.toJson(allCreaneau);
        } catch (Exception e) {

        }


    }


    public static void parseIcs(String url) {
        try (
            InputStream Istream = new URL(url).openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(Istream));
            Stream<String> stream = reader.lines()) {  
            stream.forEach(line->{
                // System.out.println(line);
                var i = line.indexOf(":");
                var type = line.substring(0, i);
                var value = line.substring(i+1);;
                switch(type) {
                    case "BEGIN":
                        inBlock = true;
                        currentCreneau = new Creneau();
                        break;
                    case "END":
                        inBlock = false;
                        if (currentCreneau.end > currentTimestamp) allCreaneau.add(currentCreneau);
                        break;
                    case "DTSTART":
                        currentCreneau.start = rtDateParse(value);
                        break;
                    case "DTEND":
                        currentCreneau.end = rtDateParse(value);
                        break;
                    case "SUMMARY":
                        currentCreneau.titre = value;
                        break;
                    case "LOCATION":
                        currentCreneau.salle = value;
                        break;
                    case "DESCRIPTION":
                        currentCreneau.description = value;
                        break;
                    default:
                      // code block
                }
            });
        } catch(Exception e) {
            
        }

    }

    public static long rtDateParse(String value) {
        var year = Integer.parseInt(value.substring(0, 4));
        var month = Integer.parseInt(value.substring(4, 6));
        var day = Integer.parseInt(value.substring(6, 8));
        var hour = Integer.parseInt(value.substring(9, 11));
        var minute = Integer.parseInt(value.substring(11, 13));
        var second = Integer.parseInt(value.substring(13, 15));
        var c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        c.set(year, month, day, hour, minute, second);
        return c.getTimeInMillis();
    }

    public static boolean isInConflict(Reservation resa) {
        return allCreaneau
                .stream()
                    .filter(c->{
                        if (!c.salle.equals(resa.salle)) return false;
                        if (c.start < resa.start && resa.start < c.end) return true;
                        if (c.start < resa.end && resa.end < c.end) return true;
                        return false;
                    })
                    .findFirst()
                    .isPresent();
    }
    
}
