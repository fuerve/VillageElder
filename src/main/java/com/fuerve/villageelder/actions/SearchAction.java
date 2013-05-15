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

import org.apache.lucene.search.Collector;

import com.fuerve.villageelder.actions.results.SearchResult;
import com.fuerve.villageelder.actions.results.SearchResultItem;
import com.fuerve.villageelder.search.Searcher;

/**
 * This class represents the action of searching against
 * an index of revision histories.
 * @author lparker
 *
 */
public class SearchAction extends Action<SearchResult> {
   private final Searcher searcher;
   private int count = 0;
   private Collector collector;
   
   /**
    * Initializes a new instance of SearchAction with a {@link Searcher}.
    * @param ssearcher The {@link Searcher} with which to execute the
    * search.
    */
   public SearchAction(final Searcher ssearcher) {
      searcher = ssearcher;
   }
   
   /**
    * Sets a custom {@link Collector} into which Lucene will
    * aggregate its search results.
    * @param ccollector The custom {@link Collector} object.
    */
   public void setCollector(final Collector ccollector) {
      collector = ccollector;
   }
   
   /**
    * Sets the maximum count of results to bring back from
    * a search.
    * @param ccount The maximum number of documents to return
    * from the index when performing the search.
    */
   public void setCount(final int ccount) {
      count = ccount;
   }
   
   @Override
   public SearchResult doWork() throws ActionException {
      try {
         if (collector != null) {
            searcher.search(collector);
         } else if (count > 0) {
            searcher.search(count);
         } else {
            searcher.search();
         }
      } catch (IOException e) {
         throw new ActionException(e);
      }
      
      final SearchResult result = new SearchResult();
      
      try {
         result.aggregate(
               new SearchResultItem(
                     searcher.getCollector().topDocs(),
                     searcher.getFacetsCollector().getFacetResults()
               )
         );
         
         return result;
      } catch (IOException e) {
         throw new ActionException(e);
      }
   }
}
