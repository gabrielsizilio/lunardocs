package io.gitgub.gabrielsizilio.lunardocs.domain.document;

import lombok.Getter;

@Getter
public enum StatusDocument {
    SIGNED("SIGNED"),
    UNSIGNED("UNSIGNED"),;

    private final String statusDocument;

    private StatusDocument(String statusDocument) {
        this.statusDocument = statusDocument;
    }

}
