package ru.asmisloff.codewars.messageenrichment;

import lombok.Getter;

import java.util.concurrent.LinkedTransferQueue;

@Getter
public class MessageAccumulator {
    private final LinkedTransferQueue<String> enriched = new LinkedTransferQueue<>();
    private final LinkedTransferQueue<String> notEnriched = new LinkedTransferQueue<>();
}
