package io.rp.job.offer.viewer.scrapper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.rp.job.offer.viewer.scrapper.meta.AgreementType;
import io.rp.job.offer.viewer.scrapper.meta.SalariesInOffer;
import io.rp.job.offer.viewer.scrapper.meta.SalaryDTO;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AccessNoFluffJobsIntegrationTest {

    private static Playwright playwright;
    private static Browser browser;

    @BeforeAll
    static void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    private static final String SALARY_HTML_B2B_ONLY = """
            <common-posting-salaries-list _ngcontent-serverapp-c2302630848="" _nghost-serverapp-c529458852=""><div _ngcontent-serverapp-c529458852=""
            class="salary"><h4 _ngcontent-serverapp-c529458852="" class="tw-mb-0"> 13&nbsp;500  – 21&nbsp;900 <!----><!----> 
            PLN </h4><div _ngcontent-serverapp-c529458852="" class="paragraph tw-text-xs lg:tw-text-sm tw-flex tw-items-center 
            tw-flex-wrap type tw-relative"><span _ngcontent-serverapp-c529458852=""> + VAT (B2B) miesięcznie&nbsp; 
            </span><a _ngcontent-serverapp-c529458852="" class="calculate"> oblicz "na rękę" 
            <inline-icon _ngcontent-serverapp-c529458852="" maticon="keyboard_arrow_right" class="lg:!tw-hidden md-icon" 
            style="--width: 20px; --height: 20px;" _nghost-serverapp-c2305233609=""><svg _ngcontent-serverApp-c2305233609="" 
            class="inline-svg"><use _ngcontent-serverApp-c2305233609="" 
            href="#md-keyboard_arrow_right"></use></svg></inline-icon></a><!----><!----><!----></div>
            </div><!----><!----><!----><!----><!----><!----><!----><!----></common-posting-salaries-list>
            """;

    private static final String SALARY_HTML_B2B_AND_COE = """
            <common-posting-salaries-list _ngcontent-serverapp-c2302630848="" _nghost-serverapp-c529458852=""><div _ngcontent-serverapp-c529458852=""
            class="salary ng-star-inserted"><h4 _ngcontent-serverapp-c529458852="" class="tw-mb-0"> 26&nbsp;880  – 29&nbsp;400 <!----><!----> 
            PLN </h4><div _ngcontent-serverapp-c529458852="" class="paragraph tw-text-xs lg:tw-text-sm tw-flex tw-items-center tw-flex-wrap type tw-relative">
            <span _ngcontent-serverapp-c529458852=""> + VAT (B2B) miesięcznie&nbsp; </span><a _ngcontent-serverapp-c529458852="" 
            class="calculate ng-star-inserted"> oblicz "na rękę" <inline-icon _ngcontent-serverapp-c529458852="" maticon="keyboard_arrow_right" 
            class="lg:!tw-hidden md-icon" style="--width: 20px; --height: 20px;" _nghost-serverapp-c2305233609=""><svg _ngcontent-serverApp-c2305233609="" 
            class="inline-svg"><use _ngcontent-serverApp-c2305233609="" href="#md-keyboard_arrow_right"></use></svg></inline-icon></a><!----><!----><!----></div>
            </div><!----><!----><div _ngcontent-serverapp-c529458852="" class="salary ng-star-inserted"><h4 _ngcontent-serverapp-c529458852="" 
            class="tw-mb-0"> 21&nbsp;000  – 22&nbsp;500 <!----><!----> PLN </h4><div _ngcontent-serverapp-c529458852="" class="paragraph tw-text-xs 
            lg:tw-text-sm tw-flex tw-items-center tw-flex-wrap type tw-relative"><span _ngcontent-serverapp-c529458852=""> brutto miesięcznie (UoP)&nbsp; 
            </span><a _ngcontent-serverapp-c529458852="" class="calculate ng-star-inserted"> oblicz netto <inline-icon _ngcontent-serverapp-c529458852=""
             maticon="keyboard_arrow_right" class="lg:!tw-hidden md-icon" style="--width: 20px; --height: 20px;"
              _nghost-serverapp-c2305233609=""><svg _ngcontent-serverApp-c2305233609="" class="inline-svg"><use _ngcontent-serverApp-c2305233609="" 
              href="#md-keyboard_arrow_right"></use></svg></inline-icon></a><!----><!----><!----></div>
              </div><!----><!----><!----><!----><!----><!----><!----><!----></common-posting-salaries-list>
            """;

    @Test
    void givenStaticHtmlWithSalary_whenSalaryTagIsPresent_thenReturnSalaryDTO() {
        Page page = browser.newPage();
        page.setContent(SALARY_HTML_B2B_ONLY);

        AccessNoFluffJobs accessNoFluffJobs = new AccessNoFluffJobs("empty");
        SalaryDTO salaryDTO = accessNoFluffJobs.locateSalary(page);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(salaryDTO.lowerBoundary()).isEqualTo(13500);
        softly.assertThat(salaryDTO.higherBoundary()).isEqualTo(21900);
        softly.assertThat(salaryDTO.agreementType()).isEqualTo(AgreementType.B2B);
        softly.assertAll();
    }

    @Test
    void givenHtml_whenContainsSalaryList_thenReturnTwoSalaries() {
        Page page = browser.newPage();
        page.setContent(SALARY_HTML_B2B_AND_COE);

        AccessNoFluffJobs accessNoFluffJobs = new AccessNoFluffJobs("empty");

        SalariesInOffer salaries = accessNoFluffJobs.locateAllSalaries(page);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(salaries.b2b()).isPresent();
        SalaryDTO firstSalary = salaries.b2b().get();

        softly.assertThat(firstSalary.lowerBoundary()).isEqualTo(26880);
        softly.assertThat(firstSalary.higherBoundary()).isEqualTo(29400);
        softly.assertThat(firstSalary.agreementType()).isEqualTo(AgreementType.B2B);

        softly.assertThat(salaries.contractOfEmployment()).isPresent();
        SalaryDTO secondSalary = salaries.contractOfEmployment().get();

        softly.assertThat(secondSalary.lowerBoundary()).isEqualTo(21000);
        softly.assertThat(secondSalary.higherBoundary()).isEqualTo(22500);
        softly.assertThat(secondSalary.agreementType()).isEqualTo(AgreementType.CoE);

        softly.assertAll();
    }

    @Test
    void givenHtmlWithSalaries_whenB2BTagOnly_thenReturnOneFilledDTOAndOneEmpty() {
        Page page = browser.newPage();
        page.setContent(SALARY_HTML_B2B_ONLY);

        AccessNoFluffJobs accessNoFluffJobs = new AccessNoFluffJobs("empty");

        SalariesInOffer salaries = accessNoFluffJobs.locateAllSalaries(page);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(salaries.b2b()).isPresent();
        SalaryDTO firstSalary = salaries.b2b().get();

        softly.assertThat(firstSalary.lowerBoundary()).isEqualTo(13500);
        softly.assertThat(firstSalary.higherBoundary()).isEqualTo(21900);
        softly.assertThat(firstSalary.agreementType()).isEqualTo(AgreementType.B2B);

        softly.assertThat(salaries.contractOfEmployment()).isEmpty();

        softly.assertAll();
    }

}