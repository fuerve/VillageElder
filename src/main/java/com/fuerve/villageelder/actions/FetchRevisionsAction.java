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

import java.io.IOException;
import com.fuerve.villageelder.actions.results.FetchRevisionsResult;
import com.fuerve.villageelder.sourcecontrol.Repository;

/**
 * This class performs the action of fetching a revision history
 * from a source control repository.
 * @author lparker
 *
 */
public class FetchRevisionsAction extends Action<FetchRevisionsResult> {
   private final Repository repository;
   private final long begin;
   private final long end;
   
   /**
    * Initializes a new instance of FetchRevisionsAction with
    * a source control repository attached.
    * @param rrepository The source control repository reference.
    */
   public FetchRevisionsAction(final Repository rrepository) {
      this(rrepository, 0L, -1L);
   }
   
   /**
    * Initializes a new instance of FetchRevisionsAction with
    * a source control repository and a range of revisions to
    * fetch.
    * @param rrepository They source control repository reference.
    * @param bbegin The inclusive beginning of the range of revisions.
    * @param eend The inclusive end of the range of revisions.
    */
   public FetchRevisionsAction(
         final Repository rrepository,
         final long bbegin,
         final long eend) {
      repository = rrepository;
      begin = bbegin;
      end = eend;
   }
   
   @Override
   public FetchRevisionsResult doWork() throws ActionException {
      try {
         FetchRevisionsResult result =
               new FetchRevisionsResult();
         result.aggregate(repository.getRevisionRange(begin, end));
         return result;
      } catch (IOException e) {
         throw new ActionException(e);
      }
   }
}
