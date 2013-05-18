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
/**
 * This package deals with the vagaries of configuration, from digging
 * through properties files to exposing configuration specific to
 * functional areas.  Probably overengineered, but frankly, after
 * years of dealing with properties systems, this is kind of the
 * one that I've always wanted.
 * 
 * @author lparker
 *
 */
package com.fuerve.villageelder.configuration;

import java.io.Reader;

import org.apache.lucene.store.Directory;

import com.fuerve.villageelder.configuration.types.DirectoryProperty;
import com.fuerve.villageelder.configuration.types.TypedProperty;

/**
 * This class holds trait properties that are common to more than
 * one functional area.
 * 
 * @author lparker
 *
 */
public class CommonProperties extends PropertyHandler {
   private static final String INDEX_DIRECTORY_PROPERTY = "Common.IndexDirectory";
   private static final String TAXONOMY_DIRECTORY_PROPERTY = "Common.TaxonomyDirectory";
   
   /**
    * Initializes a new instance of CommonProperties by reading
    * from the default properties file.
    */
   public CommonProperties() {
      super();
      
      requestProperties();
   }
   
   /**
    * Initializes a new instance of CommonProperties by reading
    * from a specified file on disk.
    * @param propertyFilename The pathname of the properties file.
    */
   public CommonProperties(final String propertyFilename) {
      super(propertyFilename);
      
      requestProperties();
   }
   
   /**
    * Initializes a new instance of CommonProperties with a
    * Reader from which the properties can be parsed in common
    * Java properties format.
    * @param ppropertySource The {@link Reader} from which properties
    * may be parsed in common Java properties format.
    */
   public CommonProperties(Reader ppropertySource) {
      super(ppropertySource);
      
      requestProperties();
   }
   
   private void requestProperties() {
      requestProperty(INDEX_DIRECTORY_PROPERTY, new DirectoryProperty());
      requestProperty(TAXONOMY_DIRECTORY_PROPERTY, new DirectoryProperty());
   }
   
   /**
    * Gets the index directory.
    * @return The Lucene {@link Directory} object in which the index
    * lives.
    */
   public Directory getIndexDirectory() {
      TypedProperty<Directory> value = get(INDEX_DIRECTORY_PROPERTY);
      return value == null ? null : value.getValue();
   }
   
   /**
    * Gets the taxonomy directory.
    * @return The Lucene {@link Directory} object in which the taxonomy
    * index lives.
    */
   public Directory getTaxonomyDirectory() {
      TypedProperty<Directory> value = get(TAXONOMY_DIRECTORY_PROPERTY);
      return value.getValue();
   }
}
