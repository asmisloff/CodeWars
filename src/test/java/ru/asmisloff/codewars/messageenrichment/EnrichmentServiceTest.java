package ru.asmisloff.codewars.messageenrichment;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.asmisloff.codewars.messageenrichment.Message.EnrichmentType.MSISDN;

class EnrichmentServiceTest {

    @ParameterizedTest
    @MethodSource("generateTestData")
    public void enrich(@NotNull TestData td) {
        Message msg = new Message(td.content, td.type);
        EnrichmentService s = createService(td.firstName, td.lastName);
        assertEquals(td.expected, s.enrich(msg));
    }

    @Test
    @SneakyThrows
    public void enrichConcurrently() {
        int n = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        CountDownLatch latch = new CountDownLatch(n);
        for (int i = 0; i < n; i++) {
            executorService.submit(() -> {
                    generateTestData().forEach(args -> {
                        var td = (TestData) args.get()[0];
                        Message msg = new Message(td.content, td.type);
                        EnrichmentService s = createService(td.firstName, td.lastName);
                        assertEquals(td.expected, s.enrich(msg));
                    });
                    latch.countDown();
                }
            );
        }
        latch.await();
        assertEquals(n, acc.getEnriched().size());
        assertEquals(2 * n, acc.getNotEnriched().size());
    }

    private EnrichmentService createService(String firstName, String lastName) {
        return new EnrichmentService((key, value) -> MsisdnEnrichmentProcessorTest.userDetails(firstName, lastName), acc);
    }

    private static @NotNull Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of(
                new TestData(
                    MSISDN,
                    "Базовый сценарий",
                    "Vasya",
                    "Ivanov",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":\"88005553535\"}",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":\"88005553535\",\"enrichment\":{\"firstName\":\"Vasya\",\"lastName\":\"Ivanov\"}}"
                )
            ),
            Arguments.of(
                new TestData(
                    MSISDN,
                    "Нет msisdn",
                    "Vasya",
                    "Ivanov",
                    "{\"action\":\"button_click\",\"page\":\"book_card\"}",
                    "{\"action\":\"button_click\",\"page\":\"book_card\"}"
                )
            ),
            Arguments.of(
                new TestData(
                    MSISDN,
                    "msisdn не строка",
                    "Vasya",
                    "Ivanov",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":88005553535}",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":88005553535}"
                )
            )
        );
    }

    public record TestData(
        Message.EnrichmentType type,
        String description,
        String firstName,
        String lastName,
        String content,
        String expected
    ) {
    }

    private final MessageAccumulator acc = new MessageAccumulator();
}