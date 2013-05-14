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
package com.fuerve.villageelder.actions;

/**
 * This class represents an exception while performing any action.
 * It is primarily used to contain other, more specific exceptions.
 * @author lparker
 *
 */
public final class ActionException extends Exception {
   /**
    * The serialization version ID for this exception class.
    */
   private static final long serialVersionUID = 1L;

   /**
    * Initializes a new instance of ActionException.
    */
   public ActionException() {
   }

   /**
    * Initializes a new instance of ActionException
    * with a message.
    * @param message The exception message.
    */
   public ActionException(String message) {
      super(message);
   }

   /**
    * Initializes a new instance of ActionException
    * with an inner or causative exception.
    * @param cause The inner or causative exception.
    */
   public ActionException(Throwable cause) {
      super(cause);
   }

   /**
    * Initializes a new instance of ActionException
    * with a message and an inner or causative exception.
    * @param message The exception message.
    * @param cause The inner or causative exception.
    */
   public ActionException(String message, Throwable cause) {
      super(message, cause);
   }
}
