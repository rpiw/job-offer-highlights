package io.rp.job.offer.viewer.scrapper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.rp.job.offer.viewer.scrapper.meta.AgreementType;
import io.rp.job.offer.viewer.scrapper.meta.JobsOfferMetadata;
import io.rp.job.offer.viewer.scrapper.meta.SalariesInOffer;
import io.rp.job.offer.viewer.scrapper.meta.SalaryDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class AccessNoFluffJobs implements PageScrapper, NoFluffJobsOfferMetadata {

    private final String url;

    AccessNoFluffJobs(String url) {
        this.url = url;
    }


    public static ScrappingResult scrap(String url) {
        return new AccessNoFluffJobs(url).scrapAllTags();
    }

    /*
     todo: remove that method. Reason: opening and closing a browser per request is too slow and too expensive
     todo: for any sane user. Instead browser opening should be connected to user session (session are not supported yet).
     */
    private ScrappingResult scrapAllTags() {
        try (Browser browser = Playwright.create().firefox().launch(new BrowserType.LaunchOptions().setHeadless(true))) {
            Page page = browser.newPage();
            page.navigate(url);
            page.waitForSelector(this.getListingTagToClick());
            page.click(this.getListingTagToClick());
            ScrappingResult scrappingResult = scrap(page);

            browser.close();
            return scrappingResult;
        }
    }

    /**
     * Assuming a salary is represented as a possibly two ranges of numbers
     * following a scheme: firstNumber - secondNumber (first string describing an agreement type)
     * some text anotherFirstNumber - anotherSecondNumber (second string describing an agreement type)
     *
     * @param page               containing salary list selectors
     * @param salaryListSelector to search for
     * @return DTO containing two salaries ranges for two agreement types
     */
    SalariesInOffer locateAllSalaries(Page page, JobsOfferMetadata salaryListSelector) {
        String salaryText = page.textContent(salaryListSelector.getSelector());
        Pattern pattern = Pattern.compile("\\d+(\\s|\\p{Z})+\\d+");
        Matcher matcher = pattern.matcher(salaryText);

        Function<Matcher, Integer> extractNumber = (matcherWithNumbers) -> {
            if (matcherWithNumbers.find()) {
                String number = matcherWithNumbers.group();
                return Integer.valueOf(number.replaceAll("(\\s|\\p{Z})+", "").trim());
            }
            return 0;
        };

        String collectedMarkingStrings = Arrays.stream(AgreementType.values())
                .map(AgreementType::getMarkingString)
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));

        String markingStringRegexPattern = String.format("(%s)", collectedMarkingStrings);
        Matcher agreementTypeMatcher = Pattern.compile(markingStringRegexPattern).matcher(salaryText);

        Function<Matcher, AgreementType> extractAgreementType = matcherWithAgreementType -> {
            if (matcherWithAgreementType.find()) {
                return AgreementType.match(matcherWithAgreementType.group());
            }
            return null;
        };

        int firstLower = extractNumber.apply(matcher);
        int firstHigher = extractNumber.apply(matcher);
        AgreementType firstAgreement = extractAgreementType.apply(agreementTypeMatcher);

        int secondLower = extractNumber.apply(matcher);
        int secondHigher = extractNumber.apply(matcher);
        AgreementType secondAgreement = extractAgreementType.apply(agreementTypeMatcher);

        return new SalariesInOffer(
                SalaryDTO.salary(firstLower, firstHigher, firstAgreement),
                SalaryDTO.salary(secondLower, secondHigher, secondAgreement)
        );
    }

    SalariesInOffer locateAllSalaries(Page page) {
        return locateAllSalaries(page, JobsOfferMetadata.SALARY_LIST);
    }

    @Override
    public ScrappingResult scrap(Page page) {
        return new ScrappingResult(
                this.getMetadata()
                        .stream()
                        .map(meta -> meta.extractor.apply(
                                page.locator(meta.getSelector()).innerText())
                        )
                        .toList()
        );
    }
}

