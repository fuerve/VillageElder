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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for the {@link IndexResult} class.
 * @author lparker
 *
 */
public class IndexResultTest {

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.IndexResult#aggregate(com.fuerve.villageelder.actions.results.IndexResultItem)}.
    */
   @Test
   public final void testAggregateIndexResultItem() throws Exception {
      IndexResultItem testItem = new IndexResultItem(5, 6);
      Result<IndexResultItem> target = new IndexResult();
      target.aggregate(testItem);
      
      IndexResultItem actual = target.getValue();
      assertEquals(5, actual.getIndexMaxDoc());
      assertEquals(6, actual.getTaxonomySize());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.IndexResult#getIndexMaxDoc()}.
    */
   @Test
   public final void testGetIndexMaxDoc() {
      IndexResultItem testItem = new IndexResultItem(5, 6);
      IndexResult target = new IndexResult();
      target.aggregate(testItem);
      
      assertEquals(5, target.getIndexMaxDoc());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.IndexResult#getTaxonomySize()}.
    */
   @Test
   public final void testGetTaxonomySize() {
      IndexResultItem testItem = new IndexResultItem(5, 6);
      IndexResult target = new IndexResult();
      target.aggregate(testItem);
      
      assertEquals(6, target.getTaxonomySize());
   }
}
