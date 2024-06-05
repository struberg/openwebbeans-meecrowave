/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.meecrowave.junit5;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import jakarta.inject.Inject;

import org.apache.meecrowave.junit5.bean.Appender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(PER_CLASS)
@MeecrowaveConfig(scanningPackageIncludes = "org.apache.meecrowave.junit5.bean")
class PerClassMeecrowaveTestLifecycleTest {
    private static Appender global;

    @Inject
    private Appender appender;

    private PerClassMeecrowaveTestLifecycleTest firstInjectionInstance;

    @AfterFirstInjection
    void afterInjection() {
        appender.reset();
        global = appender;
        appender.append("afterInjection");
        firstInjectionInstance = this;
    }

    @BeforeEach
    void beforeEach() {
        appender.append("beforeEach");
    }

    @Test
    void test() {
        assertEquals("afterInjection/beforeEach", appender.get());
        appender.append("test");
    }

    @AfterEach
    void afterEach() {
        appender.append("afterEach");
    }

    @AfterLastTest
    void afterLast() {
        appender.append("afterLast");
        assertEquals(firstInjectionInstance, this);
    }

    @AfterAll
    static void afterAll() {
        assertEquals("afterInjection/beforeEach/test/afterEach", global.get());
    }
}
