package ru.asmisloff.codewars.messageenrichment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

abstract class EnrichmentProcessor {

    @NotNull
    public EnrichmentResult enrich(
        @Nullable String content_,
        @NotNull BiFunction<String, String, IUserDetails> userDetailsProvider
    ) {
        requireNonNull(userDetailsProvider, "Параметр userDetailsProvider не может быть равен null");
        content_ = requireNonNullElse(content_, "");
        ObjectMapper m = new ObjectMapper();
        try {
            ObjectNode root = (ObjectNode) m.readTree(content_);
            String keyName = key();
            JsonNode keyNode = root.get(keyName);
            if (keyNode != null && keyNode.isTextual()) {
                String key = keyNode.asText();
                IUserDetails userDetails = userDetailsProvider.apply(keyName, key);
                if (userDetails != null) {
                    doEnrichment(root, userDetails);
                    return new EnrichmentResult(true, root.toString());
                }
            }
        } catch (JsonProcessingException e) {
            logger.info(e.getStackTrace());
        }
        return new EnrichmentResult(false, content_);
    }

    record EnrichmentResult(boolean enriched, String content) {
    }

    protected abstract String key();

    protected abstract void doEnrichment(
        @NotNull ObjectNode root,
        @NotNull IUserDetails userDetails
    );

    private final Logger logger = LogManager.getLogger(EnrichmentProcessor.class);
}
