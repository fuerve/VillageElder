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

/**
 * This class supports a more-or-less strongly typed property
 * set.  Use this class by inheriting from a parameterized
 * version of it (ie. extend TypedProperty<String>).
 * @author lparker
 *
 */
public abstract class TypedProperty<T> {
   private T value;
   private T defaultValue;
   
   /**
    * Initializes a new instance of TypedProperty with
    * no default value.
    */
   public TypedProperty() {
      defaultValue = null;
   }
   
   /**
    * Initializes a new instance of TypedProperty with
    * a default value.
    * @param ddefaultValue The default value of the property.
    */
   public TypedProperty(final T ddefaultValue) {
      defaultValue = ddefaultValue;
   }
   
   /**
    * This method should be implemented in each child class
    * to furnish custom parsing for each supported type.
    * @param vvalue The string value to parse into a type.
    * @return The strongly-typed value.
    */
   public abstract T parse(final String vvalue);
   
   /**
    * This method is called internally by {@link PropertyHandler}.
    * @param vvalue The value to parse.
    */
   public void doParse(final String vvalue) {
      if (vvalue == null || vvalue.isEmpty()) {
         value = defaultValue;
      } else {
         value = parse(vvalue);
      }
   }
   
   /**
    * Gets the typed value of this property.
    * @return The value of the property.
    */
   public T getValue() {
      if (value == null) {
         return defaultValue;
      } else {
         return value;
      }
   }
}
