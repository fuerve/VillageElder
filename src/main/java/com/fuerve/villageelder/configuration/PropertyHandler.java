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

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.fuerve.villageelder.configuration.types.TypedProperty;

/**
 * This abstract base class provides the underlying mechanism for
 * loading properties in a way that makes it relatively easy to
 * extend and modify the set of properties to be loaded at
 * runtime.
 * 
 * @author lparker
 *
 */
public abstract class PropertyHandler {
   private Reader propertySource;
   private Map<String, TypedProperty<?>> propertyMap;
   
   /**
    * Initializes a new instance of PropertyHandler with a property
    * source.  This will usually be from a file on disk, but could
    * be from anywhere.
    * @param ppropertySource The property source, in Java properties
    * format.
    */
   public PropertyHandler(final Reader ppropertySource) {
      propertySource = ppropertySource;
   }
   
   /**
    * Requests that a property be initialized as part of the property
    * set.  These properties are strongly-typed properties that
    * support defaults and custom parsing interaction.
    * @param key The name of the property.
    * @param property The property itself, which should be a
    * descendent of TypedProperty with a specific type parameter.
    */
   protected void requestProperty(final String key, final TypedProperty<?> property) {
      if (propertyMap == null) {
         propertyMap = new HashMap<String, TypedProperty<?>>();
      }
      
      if (property == null) {
         throw new IllegalArgumentException("Tried to request a null property");
      }
      
      propertyMap.put(key, property);
   }
   
   /**
    * Gets the value of a property, if the property exists and
    * has a value.
    * @param key The name of the property to retrieve.
    * @return The value of the property, or null if no such
    * property exists.
    */
   @SuppressWarnings("unchecked")
   public <T> TypedProperty<T> get(final String key) {
      TypedProperty<?> property = propertyMap.get(key);
      return (TypedProperty<T>) property;
   }
   
   /**
    * Triggers the loading of properties from the properties
    * stream.
    * @throws IOException A fatal error occurred while interacting
    * with the properties stream.
    */
   public void load() throws IOException {
      PropertyLoader loader = new PropertyLoader(propertySource);
      loader.loadProperties();
   }
   
   /**
    * This class handles the low-level interaction of loading properties
    * from the stream and interacting with the strongly-typed property
    * system.
    * 
    * @author lparker
    *
    */
   private class PropertyLoader {
      private Reader source;
      private Properties properties;
      
      /**
       * Initializes a new instance of PropertyLoader with a source stream.
       * @param ssource The source, in Java properties format.
       */
      public PropertyLoader(final Reader ssource) {
         properties = new Properties();
         source = ssource;
      }
      
      /**
       * Loads the properties from a stream and sets the appropriate
       * requested values.
       * @throws IOException A fatal exception occurred while interacting
       * with the properties source stream.
       */
      public void loadProperties() throws IOException {
         properties.load(source);
         
         if (propertyMap == null) {
            throw new IOException("PropertyHandler tried to load unknown properties");
         }
         
         for(Entry<Object, Object> property : properties.entrySet()) {
            TypedProperty<?> typedProperty = propertyMap.get(property.getKey());
            
            if (typedProperty == null) {
               continue;
            } else {
               typedProperty.doParse((String) property.getValue());
            }
         }
      }
   }
}