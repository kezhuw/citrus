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

package com.consol.citrus.admin.websocket;

import com.consol.citrus.admin.launcher.ProcessListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Process listener implementation tries to extract test related notifications from process
 * log output such as test start/finish and test action start/finish events.
 *
 * @author Christoph Deppisch
 */
public class TestEventExtractingProcessListener implements ProcessListener {

    @Autowired
    private LoggingWebSocket loggingWebSocket;

    /**
     * {@inheritDoc}
     */
    public void onProcessActivity(String processId, String output) {
        if (output.contains("STARTING TEST")) {
            loggingWebSocket.push(SocketEvent.createEvent(processId, SocketEvent.TEST_START, output));
        } else if (output.contains("FINISHED TEST")) {
            loggingWebSocket.push(SocketEvent.createEvent(processId, SocketEvent.TEST_SUCCESS, output));
        } else if (output.contains("TEST STEP") && output.contains("done")) {
            loggingWebSocket.push(SocketEvent.createEvent(processId, SocketEvent.TEST_ACTION_FINISH, output));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onProcessOutput(String processId, String output) {
        // do nothing instead process activity notifications.
    }

    /**
     * {@inheritDoc}
     */
    public void onProcessStart(String processId) {
    }

    /**
     * {@inheritDoc}
     */
    public void onProcessSuccess(String processId) {
    }

    /**
     * {@inheritDoc}
     */
    public void onProcessFail(String processId, int exitCode) {
    }

    /**
     * {@inheritDoc}
     */
    public void onProcessFail(String processId, Throwable e) {
    }
}