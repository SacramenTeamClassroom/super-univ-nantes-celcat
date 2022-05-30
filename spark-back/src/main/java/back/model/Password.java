package back.model;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.security.SecureRandom;
import java.util.HexFormat;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import back.App;

public class Password {
    public String hash;
    public String salt;

    
    public Password(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }


    public static void generateHashAndSalt(String p) {
        try {
            var random = new SecureRandom();
            var saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            var spec = new PBEKeySpec(p.toCharArray(), saltBytes, 65536, 128);
            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            var hex = HexFormat.of();
            var hash = hex.formatHex(factory.generateSecret(spec).getEncoded());
            var salt =  hex.formatHex(saltBytes);
            password = new Password(hash, salt);
            saveHashAndSalt(password);
            
        } catch (Exception e) {

        }
    }

    public static void saveHashAndSalt(Password p) throws IOException {
        var writer = new BufferedWriter(new FileWriter("passwordHash.json"));
        writer.write(App.gson.toJson(p));
        writer.close();
    }

    public static Password password;

    public static void getFromFile() {
        try {
            password = App.gson.fromJson(new FileReader("passwordHash.json"), Password.class);
        } catch (Exception e) {

        }
    }

    public static boolean verify(String p) {
        try {
            var hex = HexFormat.of();
            var spec = new PBEKeySpec(p.toCharArray(), hex.parseHex(password.salt), 65536, 128);
            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            var hash = hex.formatHex(factory.generateSecret(spec).getEncoded());
            return hash.equals(password.hash);
                    
        } catch (Exception e) {
            return false;
        }
    }

}
