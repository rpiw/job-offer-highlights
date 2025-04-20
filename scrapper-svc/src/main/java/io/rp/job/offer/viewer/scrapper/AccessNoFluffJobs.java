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

// todo: move the class to a website specific package
@Slf4j
public class AccessNoFluffJobs implements PageScrapper, NoFluffJobsOfferMetadata {

    private final String url;

    AccessNoFluffJobs(String url) {
        this.url = url;
    }


    public static ScrappingResult scrap(String url) {
        return new AccessNoFluffJobs(url).scrap();
    }

    /*
     todo: remove that method. Reason: opening and closing a browser per request is too slow and too expensive
     todo: for any sane user. Instead browser opening should be connected to user session (session are not supported yet).
     */
    ScrappingResult scrap() {
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
     * Locate salary on a Page. Defaults to NoFluffJobs selector
     *
     * @param page HTML page with a salary selector
     * @return SalaryDTO containing salary range and salary type
     */
    SalaryDTO locateSalary(Page page) {
        return locateB2BSalary(page, "common-posting-salaries-list");
    }

    /**
     * Locate and extract B2B salary on a Page
     *
     * @param page     HTML page with a salary selector
     * @param selector to search for and extract a salary numbers from
     * @return SalaryDTO containing salary range and salary type
     */
    SalaryDTO locateB2BSalary(Page page, String selector) {
        String salariesContent = page.textContent(selector);
        Pattern pattern = Pattern.compile("\\d+(\\s|\\p{Z})+\\d+");
        Matcher matcher = pattern.matcher(salariesContent);

        Function<Matcher, Integer> extractNumber = (matcherWithNumbers) -> {
            if (matcherWithNumbers.find()) {
                String number = matcherWithNumbers.group();
                return Integer.valueOf(number.replaceAll("(\\s|\\p{Z})+", "").trim());
            }
            return 0;
        };

        int lowerBound = extractNumber.apply(matcher);
        int higherBound = extractNumber.apply(matcher);

        AgreementType agreementType = salariesContent.contains("VAT (B2B)") ? AgreementType.B2B : AgreementType.CoE;

        return new SalaryDTO(lowerBound, higherBound, agreementType);
    }

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

