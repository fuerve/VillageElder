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

/**
 * This class contains the result of an indexing operation.
 * @author lparker
 *
 */
public class IndexResultItem {
   private int indexDocuments;
   private int taxonomyCategories;
   
   /**
    * Initializes a new instance of IndexResultItem.
    */
   public IndexResultItem(
         final int iindexDocuments,
         final int ttaxonomyCategories) {
      indexDocuments = iindexDocuments;
      taxonomyCategories = ttaxonomyCategories;
   }
   
   /**
    * Gets the number of documents in the index, not counting
    * deletions.
    * @return The number of documents in the index.
    */
   public int getIndexMaxDoc() {
      return indexDocuments;
   }
   
   /**
    * Gets the number of categories recorded in the taxonomy
    * index.
    * @return The number of categories in the taxonomy index.
    */
   public int getTaxonomySize() {
      return taxonomyCategories;
   }
}
