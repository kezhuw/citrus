/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus.config.xml;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.consol.citrus.TestActor;
import com.consol.citrus.channel.ReplyMessageChannelReceiver;
import com.consol.citrus.testng.AbstractBeanDefinitionParserTest;

/**
 * @author Christoph Deppisch
 */
public class ReplyMessageChannelReceiverParserTest extends AbstractBeanDefinitionParserTest {

    @Test
    public void testFailActionParser() {
        Map<String, ReplyMessageChannelReceiver> messageReceivers = beanDefinitionContext.getBeansOfType(ReplyMessageChannelReceiver.class);
        
        Assert.assertEquals(messageReceivers.size(), 2);
        Assert.assertTrue(messageReceivers.containsKey("replyMessageChannelReceiver1"));
        Assert.assertTrue(messageReceivers.containsKey("replyMessageChannelReceiver2"));
        
        // 2nd message receiver
        ReplyMessageChannelReceiver messageReceiver = messageReceivers.get("replyMessageChannelReceiver2");
        Assert.assertNotNull(messageReceiver.getActor());
        Assert.assertEquals(messageReceiver.getActor(), beanDefinitionContext.getBean("testActor", TestActor.class));
    }
}
