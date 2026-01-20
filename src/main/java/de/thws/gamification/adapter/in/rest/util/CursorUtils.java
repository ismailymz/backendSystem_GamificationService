package de.thws.gamification.adapter.in.rest.util;



import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

public class CursorUtils {


    public static String createCursor(LocalDateTime date, UUID id) {
        String raw = date.toString() + "_" + id.toString();
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static DecodedCursor decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) return null;
        try {
            String raw = new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8);
            String[] parts = raw.split("_");
            return new DecodedCursor(LocalDateTime.parse(parts[0]), UUID.fromString(parts[1]));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid cursor format");
        }
    }

    public record DecodedCursor(LocalDateTime date, UUID id) {}
}