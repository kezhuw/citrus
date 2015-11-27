/*
 * Copyright 2006-2014 the original author or authors.
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

package com.consol.citrus.camel.endpoint;

import com.consol.citrus.context.TestContext;
import com.consol.citrus.exceptions.ActionTimeoutException;
import com.consol.citrus.message.Message;
import com.consol.citrus.messaging.Consumer;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christoph Deppisch
 * @since 1.4.1
 */
public class CamelConsumer implements Consumer {
    /** Endpoint configuration */
    private final CamelEndpointConfiguration endpointConfiguration;

    /** The consumer name */
    private final String name;

    /** Cached consumer template - only created once for this consumer */
    private ConsumerTemplate consumerTemplate;

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(CamelConsumer.class);

    /**
     * Constructor using endpoint configuration and fields.
     * @param name
     * @param endpointConfiguration
     */
    public CamelConsumer(String name, CamelEndpointConfiguration endpointConfiguration) {
        this.name = name;
        this.endpointConfiguration = endpointConfiguration;
    }

    @Override
    public Message receive(TestContext context) {
        return receive(context, endpointConfiguration.getTimeout());
    }

    @Override
    public Message receive(TestContext context, long timeout) {
        if (log.isDebugEnabled()) {
            log.debug("Receiving message from camel endpoint: '" + endpointConfiguration.getEndpointUri() + "'");
        }

        Exchange exchange = getConsumerTemplate().receive(endpointConfiguration.getEndpointUri(), timeout);

        if (exchange == null) {
            throw new ActionTimeoutException("Action timed out while receiving message from camel endpoint '" + endpointConfiguration.getEndpointUri() + "'");
        }

        log.info("Received message from camel endpoint: '" + endpointConfiguration.getEndpointUri() + "'");

        Message message = endpointConfiguration.getMessageConverter().convertInbound(exchange, endpointConfiguration);
        context.onInboundMessage(message);

        return message;
    }

    /**
     * Creates new consumer template if not present yet. Create consumer template only once which is
     * mandatory for direct endpoints that do only support one single consumer at a time.
     * @return
     */
    protected ConsumerTemplate getConsumerTemplate() {
        if (consumerTemplate == null) {
            consumerTemplate = endpointConfiguration.getCamelContext().createConsumerTemplate();
        }

        return consumerTemplate;
    }

    @Override
    public String getName() {
        return name;
    }

}
