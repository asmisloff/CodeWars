package ru.asmisloff.codewars.messageenrichment;

record Message(String content, Message.EnrichmentType enrichmentType) {

    enum EnrichmentType {

        MSISDN(new MsisdnEnrichmentProcessor());

        EnrichmentType(EnrichmentProcessor processor) {
            this.processor = processor;
        }

        public final EnrichmentProcessor processor;
    }
}
