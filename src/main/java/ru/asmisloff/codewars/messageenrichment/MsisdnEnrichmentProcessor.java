package ru.asmisloff.codewars.messageenrichment;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

class MsisdnEnrichmentProcessor extends EnrichmentProcessor {

    @Override
    protected String key() {
        return "msisdn";
    }

    @Override
    protected void doEnrichment(@NotNull ObjectNode root, @NotNull IUserDetails userDetails) {
        ObjectNode enrichment = JsonNodeFactory.instance.objectNode();
        enrichment.set("firstName", JsonNodeFactory.instance.textNode(userDetails.getFirstName()));
        enrichment.set("lastName", JsonNodeFactory.instance.textNode(userDetails.getLastName()));
        root.set("enrichment", enrichment);
    }
}
