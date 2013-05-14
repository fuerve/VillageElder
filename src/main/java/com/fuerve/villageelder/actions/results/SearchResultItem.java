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
package com.fuerve.villageelder.actions.results;

import java.util.List;

import org.apache.lucene.facet.search.FacetResult;
import org.apache.lucene.search.TopDocs;

/**
 * This class contains the results of a search that has been
 * executed against the index.
 * 
 * @author lparker
 *
 */
public class SearchResultItem {
   private final TopDocs topDocs;
   private final List<FacetResult> facetResults;
   
   /**
    * Initializes a new instance of SearchResultItem with
    * a Lucene {@link TopDocs} and a list of {@link FacetResult}
    * objects.
    * @param ttopDocs The container for the top documents returned
    * from the search query.
    * @param ffacetResults The list of facet results for the query.
    */
   public SearchResultItem(
         final TopDocs ttopDocs,
         final List<FacetResult> ffacetResults) {
      topDocs = ttopDocs;
      facetResults = ffacetResults;
   }
   
   /**
    * Gets the {@link TopDocs} associated with this search result.
    * @return The container of the top documents returned by a
    * search query.
    */
   public TopDocs getTopDocs() {
      return topDocs;
   }
   
   /**
    * Gets the list of {@link FacetResult} objects returned by
    * a search query.
    * @return The list of facet results for the query.
    */
   public List<FacetResult> getFacetResults() {
      return facetResults;
   }
}
