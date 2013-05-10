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
package com.fuerve.villageelder.configuration;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import com.fuerve.villageelder.configuration.types.TypedProperty;

/**
 * Unit tests for the PropertyHandler class.
 * 
 * @author lparker
 *
 */
public class PropertyHandlerTest {
   /**
    * Test method for {@link com.fuerve.villageelder.configuration.PropertyHandler#load()}.
    */
   @Test
   public final void testLoad() throws Exception {
      final String testProperties =
            "DumbString = this is a string\nDumbInteger = 57\n";
      StringReader reader = new StringReader(testProperties);
      
      MockPropertyHandler target = new MockPropertyHandler(reader);
      target.load();
      
      assertEquals("MOCK:this is a string", target.getDumbString());
      assertEquals(new Integer(57), target.getDumbInteger());
   }
   
   @Test
   public final void testLoadDefaults() throws Exception {
      final String testProperties = "";
      StringReader reader = new StringReader(testProperties);
      
      MockPropertyHandler target = new MockPropertyHandler(reader);
      target.load();
      
      assertEquals("default!", target.getDumbString());
      assertEquals(new Integer(40), target.getDumbInteger());
   }

   private class MockStringProperty extends TypedProperty<String> {
      @SuppressWarnings("unused")
      public MockStringProperty() {
         super();
      }
      
      public MockStringProperty(final String defaultValue) {
         super(defaultValue);
      }
      
      @Override
      public String parse(String vvalue) {
         return "MOCK:" + vvalue;
      }
   }
   
   private class MockIntProperty extends TypedProperty<Integer> {
      @SuppressWarnings("unused")
      public MockIntProperty() {
         super();
      }
      
      public MockIntProperty(final Integer defaultValue) {
         super(defaultValue);
      }
      
      @Override
      public Integer parse(String vvalue) {
         return Integer.parseInt(vvalue);
      }
   }
   
   private class MockPropertyHandler extends PropertyHandler {
      private static final String DUMB_STRING_PROPERTY = "DumbString";
      private static final String DUMB_INT_PROPERTY = "DumbInteger";
      
      public MockPropertyHandler(Reader ppropertySource) {
         super(ppropertySource);
         
         requestProperty(DUMB_STRING_PROPERTY, new MockStringProperty("default!"));
         requestProperty(DUMB_INT_PROPERTY, new MockIntProperty(40));
      }
      
      public String getDumbString() {
         TypedProperty<String> value = get(DUMB_STRING_PROPERTY);
         return value.getValue();
      }
      
      public Integer getDumbInteger() {
         TypedProperty<Integer> value = get(DUMB_INT_PROPERTY);
         return value.getValue();
      }
   }
}
