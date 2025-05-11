package cdr;

import java.time.Instant;

/**
 * Модель данных CDR-записи.
 */
public class CdrRecord {

    private String id;
    private String caller;
    private String callee;
    private int duration; // длительность звонка в секундах
    private Instant timestamp;

    // Конструктор, геттеры, сеттеры и т.д.
}

/**
 * Класс с методами для генерации тестовых данных.
 */
public class TestData {
    public static CdrRecord validCdrRecord() {
        return new CdrRecord(
            "12345", // id
            "89991112233", // caller
            "89992223344", // callee
            120, // duration
            Instant.now() // timestamp
        );
    }
    
    public static CdrRecord invalidCdrRecord() {
        return new CdrRecord(
            null, // invalid ID
            "invalid", // invalid caller
            "", // invalid callee
            -1, // invalid duration
            null // invalid timestamp
        );
    }
}
