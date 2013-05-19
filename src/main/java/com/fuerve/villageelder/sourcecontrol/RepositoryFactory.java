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
package com.fuerve.villageelder.sourcecontrol;

import com.fuerve.villageelder.sourcecontrol.MockRepository;

/**
 * RepositoryFactory - Factory method container for source control repository
 * connectivity providers.
 * @author lparker
 *
 */
public final class RepositoryFactory {
   /**
    * Hidden constructor.
    */
   private RepositoryFactory() { }
   
   /**
    * Gets a Repository instance by type, calling the barebones constructor
    * that simply asks for a repository path.
    * @param repositoryType The type of repository provider 
    * @param path The repository path.
    * @return The appropriate Repository instance (or null if none exists).
    */
   public static Repository getRepositoryInstance(
         final RepositoryProviderType repositoryType,
         final String path) {
      switch (repositoryType) {
      case MOCK:
         return new MockRepository(path);
      case SUBVERSION:
         return new SubversionRepository(path);
      default:
         return null;
      }
   }
   
   /**
    * Gets a Repository instance by type, calling the simple authentication
    * constructor.
    * @param repositoryType The type of repository provider.
    * @param path The repository path.
    * @param username The repository username.
    * @param password The repository password.
    * @return The appropriate Repository instance (or null if none exists).
    */
   public static Repository getRepositoryInstance(
         final RepositoryProviderType repositoryType,
         final String path,
         final String username,
         final String password) {
      switch (repositoryType) {
      case MOCK:
         return new MockRepository(path, username, password);
      case SUBVERSION:
         return new SubversionRepository(path, username, password);
      default:
         return null;
      }
   }
}
