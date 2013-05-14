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
package com.fuerve.villageelder.actions.results;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

/**
 * Unit tests for the Result class.
 * @author lparker
 *
 */
public class ResultTest {

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.Result#Result()}.
    */
   @Test
   public final void testResult() throws Exception {
      Result<String> target = new MockResult();
      target.aggregate("test!");
      
      Field valueField = Result.class.getDeclaredField("value");
      valueField.setAccessible(true);
      
      assertEquals("test!", (String) valueField.get(target));
   }

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.Result#getValue()}.
    */
   @Test
   public final void testGetValue() {
      Result<String> target = new MockResult();
      target.aggregate("test!");
      
      assertEquals("test!", target.getValue());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.Result#setValue(java.lang.Object)}.
    */
   @Test
   public final void testSetValue() throws Exception {
      Result<String> target = new MockResult();
      target.setValue("test!");
      
      Field valueField = Result.class.getDeclaredField("value");
      valueField.setAccessible(true);
      
      assertEquals("test!", (String) valueField.get(target));
   }

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.Result#aggregate(java.lang.Object)}.
    */
   @Test
   public final void testAggregate() throws Exception {
      Result<String> target = new MockResult();
      target.aggregate("test!");
      
      Field valueField = Result.class.getDeclaredField("value");
      valueField.setAccessible(true);
      
      assertEquals("test!", (String) valueField.get(target));
   }

   /**
    * Mock {@link Result} class for testing the interface to Result.
    * @author lparker
    *
    */
   private class MockResult extends Result<String> {
      @Override
      public void aggregate(String vvalue) {
         setValue(vvalue);
      }
   }
}
