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
import java.util.ArrayList;
import java.util.List;

/**
 * A simulated repository for unit test purposes.
 * @author lparker
 *
 */
public final class MockRepository extends Repository {
   private List<RevisionInfo> history;
   
   /**
    * Initializes a new instance of MockRepository with a repository
    * path.
    * @param path The repository path.
    */
   public MockRepository(String path) {
      super(path);
   }

   /**
    * Initializes a new instance of MockRepository with a repository
    * path, a username and a password.
    * @param path The repository path.
    * @param username The repository username.
    * @param password The repository password.
    */
   public MockRepository(String path, String username, String password) {
      super(path, username, password);
   }

   /* (non-Javadoc)
    * @see com.fuerve.villageelder.sourcecontrol.Repository#getRevision(long)
    */
   @Override
   public RevisionInfo getRevision(long revision) throws IOException {
      if (history != null) {
         return history.get((int) revision);
      } else {
         return null;
      }
   }

   /* (non-Javadoc)
    * @see com.fuerve.villageelder.sourcecontrol.Repository#getRevisionRange(long, long)
    */
   @Override
   public List<RevisionInfo> getRevisionRange(long begin, long end) throws IOException {
      // Not much we can do about List not having a 64-bit range.
      int actualBegin = Math.max((int) begin, 0);
      int actualEnd = (int) end;

      final List<RevisionInfo> result = new ArrayList<RevisionInfo>();
      
      if (begin > end) {
         throw new IllegalArgumentException("Negative range was specified");
      }
      
      if (history != null && !history.isEmpty()) {
         // Enforce the end boundary argument.
         if (actualEnd == -1 || actualEnd >= history.size()) {
            actualEnd = history.size() - 1;
         }
         
         for (int i = actualBegin; i <= actualEnd; i++) {
            result.add(history.get(i));
         }
      }
      
      return result;
   }

   /**
    * Sets the entire revision history for this mock repository.
    * @param revisions A list of RevisionInfo objects containing
    * a fabricated revision history.
    */
   public void setRevisionHistory(final List<RevisionInfo> revisions) {
      history = revisions;
   }
   
   public void addRevision(final RevisionInfo revision) {
      if (revision == null) {
         throw new IllegalArgumentException("Tried to add a null revision to the MockRepository " +
               "revision history.  Don't do that.");
      }
      
      if (history == null) {
         history = new ArrayList<RevisionInfo>();
      }
      
      history.add(revision);
   }
}
