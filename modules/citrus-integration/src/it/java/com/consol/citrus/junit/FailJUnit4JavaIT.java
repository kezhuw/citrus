/*
 * Copyright 2006-2017 the original author or authors.
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

package com.consol.citrus.junit;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.exceptions.TestCaseFailedException;
import org.junit.Test;

/**
 * @author Christoph Deppisch
 */
public class FailJUnit4JavaIT extends JUnit4CitrusTestDesigner {

    @Override
    protected void configure() {
        echo("This test should fail because of unknown variable ${foo}");
    }

    @Test(expected = TestCaseFailedException.class)
    public void doExecute() {
        executeTest();
    }

    @Test(expected = TestCaseFailedException.class)
    @CitrusTest
    public void failTest() {
        echo("This test should fail because of unknown variable ${foo}");
    }
}
