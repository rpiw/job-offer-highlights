package io.rp.job.offer.viewer;

import jakarta.validation.constraints.NotBlank;

public record UrlDTO(@NotBlank String url) {

}
