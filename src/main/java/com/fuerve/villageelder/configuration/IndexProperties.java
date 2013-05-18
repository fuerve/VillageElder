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

import java.io.Reader;

/**
 * This class contains properties specific to the management
 * and operation of Lucene indices.  Things that might go in
 * here include index tuning parameters and other things that
 * are specific to indexing.
 * 
 * @author lparker
 *
 */
public class IndexProperties extends PropertyHandler {
   /**
    * Initializes a new instance of IndexProperties by reading
    * from the default properties file.
    */
   public IndexProperties() {
      super();
   }
   
   /**
    * Initializes a new instance of IndexProperties by reading
    * from a specified file on disk.
    * @param propertyFilename The pathname of the properties file.
    */
   public IndexProperties(final String propertyFilename) {
      super(propertyFilename);
   }
   
   /**
    * Initializes a new instance of IndexProperties.
    * @param ppropertySource The {@link Reader} from which
    * the index properties are to be read.
    */
   public IndexProperties(Reader ppropertySource) {
      super(ppropertySource);
   }
}
