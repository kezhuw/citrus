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

import com.consol.citrus.endpoint.AbstractEndpoint;
import com.consol.citrus.messaging.Consumer;
import com.consol.citrus.messaging.Producer;

/**
 * @author Christoph Deppisch
 * @since 1.4.1
 */
public class CamelEndpoint extends AbstractEndpoint {

    /**
     * Default constructor initializes endpoint configuration;
     */
    public CamelEndpoint() {
        this(new CamelEndpointConfiguration());
    }

    /**
     * Default constructor using endpoint configuration.
     *
     * @param endpointConfiguration
     */
    public CamelEndpoint(CamelEndpointConfiguration endpointConfiguration) {
        super(endpointConfiguration);
    }

    @Override
    public Producer createProducer() {
        return new CamelProducer(getProducerName(), getEndpointConfiguration());
    }

    @Override
    public Consumer createConsumer() {
        return new CamelConsumer(getConsumerName(), getEndpointConfiguration());
    }

    @Override
    public CamelEndpointConfiguration getEndpointConfiguration() {
        return (CamelEndpointConfiguration) super.getEndpointConfiguration();
    }
}
