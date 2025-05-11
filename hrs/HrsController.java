package hrs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/hrs")
public class HrsController {

    private final Set<String> validNumbers = Set.of("1001", "1002", "1003");
    private final List<Map<String, String>> routes = new ArrayList<>();

    @PostMapping("/route")
    public ResponseEntity<?> routeCall(@RequestBody Map<String, String> request) {
        String caller = request.get("caller");
        String receiver = request.get("receiver");

        if (caller == null || receiver == null) {
            return ResponseEntity.badRequest().body("Missing caller or receiver");
        }

        if (!validNumbers.contains(receiver)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Receiver not found"));
        }

        routes.add(Map.of("caller", caller, "receiver", receiver));
        return ResponseEntity.ok(Map.of("status", "routed"));
    }

    @GetMapping("/routes")
    public ResponseEntity<?> getRoutingTable() {
        return ResponseEntity.ok(routes);
    }

    @DeleteMapping("/routes")
    public ResponseEntity<?> clearRoutingTable() {
        routes.clear();
        return ResponseEntity.noContent().build();
    }
}
