package io.rp.job.offer.viewer.crawler;

import java.util.Optional;

enum NoFluffJobsCrawlingLocators {
    LOAD_MORE("button[nfjloadmore]", "See more offers");

    private final String locator;
    private final Optional<String> text;

    NoFluffJobsCrawlingLocators(String locator, String text) {
        this.locator = locator;
        this.text = Optional.ofNullable(text);
    }
}
