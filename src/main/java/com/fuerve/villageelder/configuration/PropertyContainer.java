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

/**
 * This class is just a container for properties classes.
 * It makes it convenient to pass properties around
 * throughout the system.
 * @author lparker
 *
 */
public class PropertyContainer {
   private static class InstanceHolder {
      public static PropertyContainer instance;
      
      static {
         try {
            instance = new PropertyContainer();
         } catch (IOException e) {
            instance = null;
         }
      }
   }
   
   private CommonProperties commonProperties;
   private IndexProperties indexProperties;
   private SearchProperties searchProperties;
   private SourceControlProperties sourceControlProperties;
   
   private PropertyContainer() throws IOException {
      final String source = System.getProperty("properties.source");
      
      if (source == null || "default".equals(source.toLowerCase())) {
         commonProperties = new CommonProperties();
         indexProperties = new IndexProperties();
         searchProperties = new SearchProperties();
         sourceControlProperties = new SourceControlProperties();
      } else {
         commonProperties = new CommonProperties(source);
         indexProperties = new IndexProperties(source);
         searchProperties = new SearchProperties(source);
         sourceControlProperties = new SourceControlProperties(source);
      }
      
      commonProperties.load();
      indexProperties.load();
      searchProperties.load();
      sourceControlProperties.load();
   }
   
   /**
    * Gets the single instance of PropertyContainer.
    * @return The reference to the instance of PropertyContainer.
    */
   public static PropertyContainer getInstance() {
      return InstanceHolder.instance;
   }
   
   /**
    * Gets the common properties container.
    * @return The common properties container.
    */
   public CommonProperties getCommonProperties() {
      return commonProperties;
   }
   
   /**
    * Gets the index properties container.
    * @return The index properties container.
    */
   public IndexProperties getIndexProperties() {
      return indexProperties;
   }
   
   /**
    * Gets the search properties container.
    * @return The search properties container.
    */
   public SearchProperties getSearchProperties() {
      return searchProperties;
   }
   
   /**
    * Gets the source control properties container.
    * @return The source control properties container.
    */
   public SourceControlProperties getSourceControlProperties() {
      return sourceControlProperties;
   }
}
