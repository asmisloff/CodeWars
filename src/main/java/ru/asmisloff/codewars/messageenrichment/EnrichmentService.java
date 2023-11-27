package ru.asmisloff.codewars.messageenrichment;

import lombok.Getter;

import java.util.function.BiFunction;

class EnrichmentService {

    public EnrichmentService(BiFunction<String, String, IUserDetails> userDetailsProvider, MessageAccumulator acc) {
        this.userDetailsProvider = userDetailsProvider;
        this.acc = acc;
    }

    public String enrich(Message message) {
        EnrichmentProcessor processor = message.enrichmentType().processor;
        var result = processor.enrich(message.content(), userDetailsProvider);
        var queue = result.enriched() ? acc.getEnriched() : acc.getNotEnriched();
        queue.add(result.content());
        return result.content();
    }

    private final BiFunction<String, String, IUserDetails> userDetailsProvider;
    @Getter
    private final MessageAccumulator acc;
}
