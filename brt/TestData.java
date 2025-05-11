package brt;

public class TestData {
    public static CallRecord validCallRecord() {
        return new CallRecord(
            "01", 
            "89991112233", 
            "89992223344", 
            Instant.now().minus(5, ChronoUnit.MINUTES), 
            Instant.now()
        );
    }
    
    public static CallRecord invalidCallRecord() {
        return new CallRecord(
            null,
            "invalid",
            "",
            null,
            Instant.now()
        );
    }
}