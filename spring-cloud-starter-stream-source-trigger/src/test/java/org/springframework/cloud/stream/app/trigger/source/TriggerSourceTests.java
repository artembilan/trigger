/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.trigger.source;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * Trigger source tests.
 * @author Ilayaperumal Gopinathan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public abstract class TriggerSourceTests {

	@Autowired
	@Bindings(TriggerSourceConfiguration.class)
	protected Source triggerSource;

	@Autowired
	protected MessageCollector messageCollector;

	@SpringBootTest({"trigger.fixedDelay=2", "trigger.initialDelay=1", "trigger.source.payload='test'"})
	public static class FixedDelayTest extends TriggerSourceTests {

		@Test
		public void fixedDelayTest() throws InterruptedException {
			assertTrue(messageCollector.forChannel(triggerSource.output()).poll(2100, TimeUnit.MILLISECONDS).getPayload().equals("test"));
		}
	}

	@SpringBootTest({"trigger.fixedDelay=2", "trigger.initialDelay=1"})
	public static class FixedDelayEmptyPayloadTest extends TriggerSourceTests {

		@Test
		public void fixedDelayTest() throws InterruptedException {
			assertTrue(messageCollector.forChannel(triggerSource.output()).poll(2100, TimeUnit.MILLISECONDS).getPayload().equals(""));
		}
	}

	@SpringBootTest({"trigger.cron=0/2 * * * * *", "trigger.source.payload='cronTest'"})
	public static class CronTriggerTest extends TriggerSourceTests {

		@Test
		public void cronTriggerTest() throws InterruptedException {
			assertTrue(messageCollector.forChannel(triggerSource.output()).poll(2100, TimeUnit.MILLISECONDS).getPayload().equals("cronTest"));
			assertTrue(messageCollector.forChannel(triggerSource.output()).poll(2100, TimeUnit.MILLISECONDS).getPayload().equals("cronTest"));
		}
	}

	@SpringBootApplication
	public static class TriggerSourceApplication {

	}

}
