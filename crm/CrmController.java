package crm;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/crm")
public class CrmController {

    private final Map<Integer, Map<String, String>> customers = new HashMap<>();
    private int customerIdCounter = 1;

    @PostMapping("/customers")
    public ResponseEntity<?> createCustomer(@RequestBody Map<String, String> customer) {
        if (customer.get("name") == null || customer.get("email") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name and email are required");
        }

        Map<String, String> newCustomer = new HashMap<>(customer);
        newCustomer.put("id", String.valueOf(customerIdCounter++));
        customers.put(Integer.parseInt(newCustomer.get("id")), newCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable int id) {
        Map<String, String> customer = customers.get(id);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Customer not found"));
        }
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable int id, @RequestBody Map<String, String> updatedCustomer) {
        Map<String, String> existingCustomer = customers.get(id);
        if (existingCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Customer not found"));
        }

        existingCustomer.putAll(updatedCustomer);
        return ResponseEntity.ok(existingCustomer);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int id) {
        if (!customers.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Customer not found"));
        }
        customers.remove(id);
        return ResponseEntity.noContent().build();
    }
}
