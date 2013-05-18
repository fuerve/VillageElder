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

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.fuerve.villageelder.configuration.types.TypedProperty;

/**
 * This class provides a strongly-typed Lucene {@link Directory}
 * property, parsed from a string pathname.
 * 
 * @author lparker
 *
 */
public class DirectoryProperty extends TypedProperty<Directory> {
   @Override
   public Directory parse(String vvalue) {
      try {
         final Directory directory = FSDirectory.open(new File(vvalue));
         return directory;
      } catch (IOException e) {
         return null;
      }
   }
}
