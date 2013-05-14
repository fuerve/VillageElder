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
import java.util.List;

import com.fuerve.villageelder.actions.results.IndexResult;
import com.fuerve.villageelder.actions.results.IndexResultItem;
import com.fuerve.villageelder.indexing.Indexer;
import com.fuerve.villageelder.sourcecontrol.RevisionInfo;

/**
 * This class represents the action of indexing a set of
 * revision history logs by creating an index (if necessary)
 * and adding to it.
 * @author lparker
 *
 */
public class IndexAction extends Action<IndexResult> {
   private final Indexer indexer;
   private final List<RevisionInfo> revisions;
   
   /**
    * Initializes a new instance of IndexAction with an {@link Indexer}.
    * @param iindexer The {@link Indexer} instance to use when indexing.
    */
   public IndexAction(final Indexer iindexer, final List<RevisionInfo> rrevisions) {
      indexer = iindexer;
      revisions = rrevisions;
   }
   
   @Override
   public IndexResult doWork() throws ActionException {
      try {
         indexer.indexRevisions(revisions);
         IndexResult result = new IndexResult();
         result.aggregate(new IndexResultItem(indexer.getMaxDoc(), indexer.getTaxonomySize()));
         return result;
      } catch (IOException e) {
         throw new ActionException(e);
      }
   }
}
