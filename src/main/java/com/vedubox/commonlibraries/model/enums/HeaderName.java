package com.vedubox.commonlibraries.model.enums;

public enum HeaderName {
    X_CORRELATION_ID("x-correlation-id");

    private final String text;

    /**
     * @param text
     */
    HeaderName(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
