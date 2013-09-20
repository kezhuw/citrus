/*
 * Copyright 2006-2013 the original author or authors.
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

package com.consol.citrus.admin.executor;

import com.consol.citrus.admin.model.TestCaseInfo;
import com.consol.citrus.admin.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author Christoph Deppisch
 */
@ContextConfiguration(locations = { "classpath:com/consol/citrus/admin/citrus-admin-test-context.xml" })
public class FileSystemTestExecutorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ConfigurationService configService;
    
    @Autowired
    private FileSystemTestExecutor testExecutor;
    
    @Test
    public void testGetTests() throws IOException {
        reset(configService);

        expect(configService.getProjectHome()).andReturn(new ClassPathResource("test-project").getFile().getAbsolutePath()).times(2);
        
        replay(configService);
        
        List<TestCaseInfo> tests = testExecutor.getTests();
        
        Assert.assertNotNull(tests);
        Assert.assertEquals(tests.size(), 2L);
        
        TestCaseInfo test = tests.get(0);
        Assert.assertEquals(test.getName(), "FooTest");
        Assert.assertEquals(test.getPackageName(), "");
        
        test = tests.get(1);
        Assert.assertEquals(test.getName(), "BarTest");
        Assert.assertEquals(test.getPackageName(), "com.consol.citrus");
        
        verify(configService);
    }
}
