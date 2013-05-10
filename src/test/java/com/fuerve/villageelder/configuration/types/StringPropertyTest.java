/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.fuerve.villageelder.configuration.types;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the StringProperty class.
 * 
 * @author lparker
 *
 */
public class StringPropertyTest {

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.types.StringProperty#StringProperty()}.
    */
   @Test
   public final void testStringProperty() {
      TypedProperty<?> target = new StringProperty();
      assertEquals(null, target.getValue());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.types.StringProperty#StringProperty(java.lang.String)}.
    */
   @Test
   public final void testStringPropertyString() {
      TypedProperty<?> target = new StringProperty("default!");
      assertEquals("default!", target.getValue());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.types.StringProperty#parse(java.lang.String)}.
    */
   @Test
   public final void testParseString() {
      TypedProperty<?> target = new StringProperty();
      target.doParse("test");
      assertEquals("test", target.getValue());
   }

}
