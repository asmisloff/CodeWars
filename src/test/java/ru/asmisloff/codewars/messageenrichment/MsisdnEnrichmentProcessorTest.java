package ru.asmisloff.codewars.messageenrichment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MsisdnEnrichmentProcessorTest {

    @ParameterizedTest
    @CsvSource(value = "Vasya:Ivanov", delimiter = ':')
    void doEnrichmentWithAddition(String firstName, String lastName) throws JsonProcessingException {
        MsisdnEnrichmentProcessor p = new MsisdnEnrichmentProcessor();
        var root = m.readTree("{}");
        checkAssertions(firstName, lastName, p, root);
    }

    @ParameterizedTest
    @CsvSource(value = "Vasya:Ivanov", delimiter = ':')
    void doEnrichmentWithReplacement(String firstName, String lastName) throws JsonProcessingException {
        MsisdnEnrichmentProcessor p = new MsisdnEnrichmentProcessor();
        var root = m.readTree("""
            {
                "enrichment": {
                    "firstName": "Vasya_",
                    "lastName": "Ivanov_"
                }
            }
            """
        );
        checkAssertions(firstName, lastName, p, root);
    }

    @ParameterizedTest
    @MethodSource("generateTestData")
    void enrich(TestData td) {
        EnrichmentProcessor p = new MsisdnEnrichmentProcessor();
        var actual = p.enrich(td.srcContent, (key, value) -> userDetails(td.firstName, td.lastName));
        assertEquals(td.expected, actual.content(), td.description);
    }

    private void checkAssertions(String firstName, String lastName, MsisdnEnrichmentProcessor p, JsonNode root) {
        p.doEnrichment((ObjectNode) root, userDetails(firstName, lastName));
        var enrichment = root.get("enrichment");
        assertNotNull(enrichment);
        var firstNameNode = enrichment.get("firstName");
        var lastNameNode = enrichment.get("lastName");
        assertNotNull(firstName);
        assertEquals(firstName, firstNameNode.textValue());
        assertNotNull(lastName);
        assertEquals(lastName, lastNameNode.textValue());
    }

    @NotNull
    public static IUserDetails userDetails(@NotNull String firstName, @NotNull String lastName) {
        return new IUserDetails() {
            @Override
            public String getFirstName() {
                return firstName;
            }

            @Override
            public String getLastName() {
                return lastName;
            }
        };
    }

    private static Stream<Arguments> generateTestData() {
        return Stream.of(
            Arguments.of(
                new TestData(
                    "Базовый сценарий",
                    "Vasya",
                    "Ivanov",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":\"88005553535\"}",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":\"88005553535\",\"enrichment\":{\"firstName\":\"Vasya\",\"lastName\":\"Ivanov\"}}"
                )
            ),
            Arguments.of(
                new TestData(
                    "Нет msisdn",
                    "Vasya",
                    "Ivanov",
                    "{\"action\":\"button_click\",\"page\":\"book_card\"}",
                    "{\"action\":\"button_click\",\"page\":\"book_card\"}"
                )
            ),
            Arguments.of(
                new TestData(
                    "msisdn не строка",
                    "Vasya",
                    "Ivanov",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":88005553535}",
                    "{\"action\":\"button_click\",\"page\":\"book_card\",\"msisdn\":88005553535}"
                )
            )
        );
    }

    private record TestData(String description, String firstName, String lastName, String srcContent, String expected) {
    }

    private final ObjectMapper m = new ObjectMapper();
}