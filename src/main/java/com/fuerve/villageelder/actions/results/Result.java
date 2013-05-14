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

/**
 * This class represents a single result item in an abstract way.
 * @author lparker
 *
 */
public abstract class Result<T> {
   private T value;
   
   /**
    * Initializes a new Result.
    */
   public Result() {
   }
   
   /**
    * Gets this result item value.
    * @return The result item's value.
    */
   public T getValue() {
      return value;
   }
   
   /**
    * Sets this result item value.
    * @param vvalue The result item's value.
    */
   public void setValue(final T vvalue) {
      value = vvalue;
   }
   
   /**
    * Called within a subclass to set the result value.
    * @param vvalue The value to aggregate into the result.
    */
   public abstract void aggregate(final T vvalue);
}
