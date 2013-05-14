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
 * This class aggregates the result of a search against the index.
 * 
 * @author lparker
 *
 */
public class SearchResult extends Result<SearchResultItem> {
   /* (non-Javadoc)
    * @see com.fuerve.villageelder.actions.Result#aggregate(java.lang.Object)
    */
   @Override
   public void aggregate(SearchResultItem vvalue) {
      setValue(vvalue);
   }

   /**
    * Gets the top documents returned by a search query.
    * @return The top documents returned by a search.
    */
   public TopDocs getTopDocs() {
      SearchResultItem value = getValue();
      if (value == null) {
         return null;
      } else {
         return value.getTopDocs();
      }
   }
   
   /**
    * Gets the facet results from a search query.
    * @return The facet results returned by a search.
    */
   public List<FacetResult> getFacetResults() {
      SearchResultItem value = getValue();
      if (value == null) {
         return null;
      } else {
         return value.getFacetResults();
      }
   }
}
