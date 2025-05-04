package io.rp.job.offer.viewer.crawler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class URLEntityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static Stream<Arguments> provideCorrectURLs() {
        return Stream.of(
                Arguments.of("file://goodEnoug"),
                Arguments.of("http://1232131"),
                Arguments.of("http://" + "1".repeat(140))
        );
    }


    private static Stream<Arguments> provideIncorrectURLs() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of("http://"),
                Arguments.of("http://;drop database"),
                Arguments.of("http:// asdsad dasdasdsa"),
                Arguments.of("http://" + "2".repeat(151))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCorrectURLs")
    void validateStringsHappyPathTest(String url) {
        URLEntity urlEntity = new URLEntity(url);
        Set<ConstraintViolation<URLEntity>> validate = validator.validate(urlEntity);
        assertTrue(validate.isEmpty(), validate::toString);
    }

    @ParameterizedTest
    @MethodSource("provideIncorrectURLs")
    void validateStringsWithExpectedConstraintViolation(String url) {
        URLEntity urlEntity = new URLEntity(url);
        Set<ConstraintViolation<URLEntity>> validate = validator.validate(urlEntity);
        assertFalse(validate.isEmpty());
    }

}