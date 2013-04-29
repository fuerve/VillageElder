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

import java.io.IOException;
import java.util.List;

/**
 * Repository - Base class for source control repository implementations.
 * @author lparker
 *
 */
public abstract class Repository {
   private final String path;
   private final String username;
   private final String password;
   // I understand that storing a username and password is suboptimal,
   // but it gets us going for now.
   
   /**
    * Initializes an instance of Repository with a repository path.
    * @param ppath The repository path.  What is actually contained in this
    * string will change from one system to another.  This is often a URL.
    */
   public Repository(final String ppath) {
      this(ppath, (String) null, (String) null);
   }
   
   /**
    * Initializes an instance of Repository with a repository path, username and password.
    * @param ppath The repository path.  What is actually contained in this
    * string will change from one system to another.  This is often a URL.
    * @param uusername The username used to connect to the repository.
    * @param ppassword The password user to connect to the repository.
    */
   public Repository(final String ppath, final String uusername, final String ppassword) {
      path = ppath;
      username = uusername;
      password = ppassword;
   }
   
   /**
    * Gets the repository path.
    * @return The repository path.
    */
   public String getPath() {
      return path;
   }
   
   /**
    * Gets the repository username.
    * @return The repository username.
    */
   public String getUsername() {
      return username;
   }
   
   /**
    * Gets the repository password.
    * @return The repository password.
    */
   public String getPassword() {
      return password;
   }
   
   /**
    * Retrieves a RevisionInfo for a single source revision from the repository.
    * Using this often is highly inefficient - use
    * {@link Repository#getRevisionRange} when you can.
    * @param revision The revision number to get from the repository.
    * @return The RevisionInfo associated with the given revision
    * number, or null if no such revision exists.
    */
   public abstract RevisionInfo getRevision(final long revision)
         throws IOException;
   
   /**
    * Retrieves a list of RevisionInfo objects for a range of revisions from
    * the repository.
    * @param begin The lowest revision number to obtain.
    * @param end The highest revision number to obtain.  Often, this may be
    * set to -1 to obtain the head revision as the last in the list.
    * @return The list of RevisionInfo objects containing the requested
    * revisions.  Implementations should always return a non-null value,
    * even if just an empty list.
    */
   public abstract List<RevisionInfo> getRevisionRange(
         final long begin,
         final long end)
         throws IOException;
}
