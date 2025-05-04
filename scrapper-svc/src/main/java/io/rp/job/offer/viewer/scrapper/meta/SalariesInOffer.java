package io.rp.job.offer.viewer.scrapper.meta;

import java.util.Optional;

public record SalariesInOffer(Optional<SalaryDTO> b2b, Optional<SalaryDTO> contractOfEmployment) {

}
