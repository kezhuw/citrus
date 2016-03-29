/*
 * Copyright 2006-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.validation;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.ValidationException;
import com.consol.citrus.util.TypeConversionUtils;
import com.consol.citrus.validation.matcher.ValidationMatcherUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Utility class provides helper methods for validation work in Citrus.
 * 
 * @author Christoph Deppisch
 * @since 1.3
 */
public abstract class ValidationUtils {

    /**
     * Prevent instantiation.
     */
    private ValidationUtils() {
        super();
    }

    /**
     * Validates actual against expected value of element
     * @param actualValue
     * @param expectedValue
     * @param pathExpression
     * @param context
     * @throws com.consol.citrus.exceptions.ValidationException if validation fails
     */
    public static void validateValues(String actualValue, Object expectedValue, String pathExpression, TestContext context)
            throws ValidationException {
        try {
            if (actualValue != null) {
                Assert.isTrue(expectedValue != null,
                        ValidationUtils.buildValueMismatchErrorMessage(
                                "Values not equal for element '" + pathExpression + "'", null, actualValue));

                //check if validation matcher on element is specified
                if (expectedValue instanceof String && ValidationMatcherUtils.isValidationMatcherExpression(String.valueOf(expectedValue))) {
                    ValidationMatcherUtils.resolveValidationMatcher(pathExpression,
                            actualValue,
                            String.valueOf(expectedValue),
                            context);
                } else if (expectedValue instanceof String) {
                    Assert.isTrue(actualValue.equals(expectedValue),
                            ValidationUtils.buildValueMismatchErrorMessage(
                                    "Values not equal for element '" + pathExpression + "'", expectedValue, actualValue));
                } else {
                    Object converted = TypeConversionUtils.convertIfNecessary(actualValue, expectedValue.getClass());

                    if (converted instanceof List) {
                        Assert.isTrue(converted.toString().equals(expectedValue.toString()),
                                ValidationUtils.buildValueMismatchErrorMessage(
                                        "Values not equal for element '" + pathExpression + "'", expectedValue.toString(), converted.toString()));
                    } else if (converted instanceof String[]) {
                        String convertedDelimitedString = StringUtils.arrayToCommaDelimitedString((String[]) converted);
                        String expectedDelimitedString = StringUtils.arrayToCommaDelimitedString((String[]) expectedValue);

                        Assert.isTrue(convertedDelimitedString.equals(expectedDelimitedString),
                                ValidationUtils.buildValueMismatchErrorMessage(
                                        "Values not equal for element '" + pathExpression + "'", expectedDelimitedString, convertedDelimitedString));
                    } else if (converted instanceof byte[]) {
                        String convertedBase64 = Base64.encodeBase64String((byte[]) converted);
                        String expectedBase64 = Base64.encodeBase64String((byte[]) expectedValue);

                        Assert.isTrue(convertedBase64.equals(expectedBase64),
                                ValidationUtils.buildValueMismatchErrorMessage(
                                        "Values not equal for element '" + pathExpression + "'", expectedBase64, convertedBase64));
                    } else {
                        Assert.isTrue(converted.equals(expectedValue),
                                ValidationUtils.buildValueMismatchErrorMessage(
                                        "Values not equal for element '" + pathExpression + "'", expectedValue, converted));
                    }
                }
            } else {
                Assert.isTrue(expectedValue == null || String.valueOf(expectedValue).length() == 0,
                        ValidationUtils.buildValueMismatchErrorMessage(
                                "Values not equal for element '" + pathExpression + "'", expectedValue, null));
            }
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Validation failed:", e);
        }
    }
    
    /**
     * Constructs proper error message with expected value and actual value.
     * @param baseMessage the base error message.
     * @param controlValue the expected value.
     * @param actualValue the actual value.
     * @return
     */
    public static String buildValueMismatchErrorMessage(String baseMessage, Object controlValue, Object actualValue) {
        return baseMessage + ", expected '" + controlValue + "' but was '" + actualValue + "'";
    }
}
