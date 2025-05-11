package brt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/brt")
public class BrtController {

    private final Map<String, String> subscribers = new HashMap<>();

    @PostMapping("/register")
    public ResponseEntity<?> registerSubscriber(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String tariff = request.get("tariff");

        if (subscribers.containsKey(phone)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already registered");
        }

        subscribers.put(phone, tariff);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/tariff")
    public ResponseEntity<?> changeTariff(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String newTariff = request.get("newTariff");

        if (!subscribers.containsKey(phone)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscriber not found");
        }

        subscribers.put(phone, newTariff);
        return ResponseEntity.ok(Map.of("message", "Tariff updated"));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getSubscriberInfo(@RequestParam String phone) {
        if (!subscribers.containsKey(phone)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscriber not found");
        }

        return ResponseEntity.ok(Map.of(
            "phone", phone,
            "tariff", subscribers.get(phone)
        ));
    }
}
