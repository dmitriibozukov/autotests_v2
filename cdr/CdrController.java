package cdr;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cdr")
public class CdrController {

    @PostMapping("/generate")
    public ResponseEntity<Void> generateCdrFile() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/last")
    public ResponseEntity<Map<String, Object>> getLastCdrFile() {
        Map<String, Object> response = new HashMap<>();
        response.put("records", List.of()); // пустой список записей
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-date")
    public ResponseEntity<?> getCdrByDate(@RequestParam String date) {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
        Map<String, Object> response = new HashMap<>();
        response.put("records", List.of());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteCdrFiles() {
        return ResponseEntity.ok(Map.of("message", "CDR files deleted"));
    }
}
