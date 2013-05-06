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
package com.fuerve.villageelder.search;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.fuerve.villageelder.common.Lucene;

/**
 * Unit tests for the Search class.
 * These tests are more drudgery than brilliance by far, so please feel
 * free to improve upon them.  I'm really just checking whether things
 * are bolted together the way I expect them to be.  Much of what these
 * classes do is integration-specific, anyway.
 * @author lparker
 *
 */
public class SearchTest {

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(org.apache.lucene.search.Query)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchQuery() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      Query queryExpected = parser.parse("test:foo");
      
      Search target = new Search(queryExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual);
      assertEquals(defaultSort, sortActual);
      assertEquals(defaultFacets, facetsActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(org.apache.lucene.search.Query, org.apache.lucene.search.Sort)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchQuerySort() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      Query queryExpected = parser.parse("test:foo");
      Sort sortExpected = Sort.RELEVANCE;
      
      Search target = new Search(queryExpected, sortExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual);
      assertEquals(sortExpected, sortActual);
      assertEquals(defaultFacets, facetsActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(org.apache.lucene.search.Query, java.util.List)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchQueryListOfFacetRequest() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      Query queryExpected = parser.parse("test:foo");
      List<FacetRequest> facetsExpected = new ArrayList<FacetRequest>();
      
      Search target = new Search(queryExpected, facetsExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual);
      assertEquals(defaultSort, sortActual);
      assertEquals(facetsExpected, facetsActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(org.apache.lucene.search.Query, java.util.List, org.apache.lucene.search.Sort)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchQueryListOfFacetRequestSort() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      Query queryExpected = parser.parse("test:foo");
      List<FacetRequest> facetsExpected = new ArrayList<FacetRequest>();
      Sort sortExpected = Sort.RELEVANCE;
      
      Search target = new Search(queryExpected, facetsExpected, sortExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual);
      assertEquals(sortExpected, sortActual);
      assertEquals(facetsExpected, facetsActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(java.lang.String)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchString() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      String queryExpected = "test:foo";
      
      Search target = new Search(queryExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual.toString());
      assertEquals(defaultSort, sortActual);
      assertEquals(defaultFacets, facetsActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(java.lang.String, org.apache.lucene.search.Sort)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchStringSort() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      String queryExpected = "test:foo";
      Sort sortExpected = Sort.RELEVANCE;
      
      Search target = new Search(queryExpected, sortExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual.toString());
      assertEquals(sortExpected, sortActual);
      assertEquals(defaultFacets, facetsActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(java.lang.String, org.apache.lucene.analysis.Analyzer)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchStringAnalyzer() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      String queryExpected = "test:foo";
      
      Search target = new Search(queryExpected, Lucene.getPerFieldAnalyzer());
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual.toString());
      assertEquals(defaultSort, sortActual);
      assertEquals(defaultFacets, facetsActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(java.lang.String, java.util.Map)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchStringMapOfStringInteger() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      String queryExpected = "test:foo";
      Map<String, Integer> facetsExpected = new HashMap<String, Integer>();
      facetsExpected.put("testFacet", 99);
      
      Search target = new Search(queryExpected, facetsExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual.toString());
      assertEquals(defaultSort, sortActual);
      assertEquals("testFacet", facetsActual.get(0).categoryPath.toString());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(java.lang.String, java.util.Map, org.apache.lucene.search.Sort)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchStringMapOfStringIntegerSort() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      String queryExpected = "test:foo";
      Map<String, Integer> facetsExpected = new HashMap<String, Integer>();
      Sort sortExpected = Sort.RELEVANCE;
      
      facetsExpected.put("testFacet", 99);
      
      Search target = new Search(queryExpected, facetsExpected, sortExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual.toString());
      assertEquals(sortExpected, sortActual);
      assertEquals("testFacet", facetsActual.get(0).categoryPath.toString());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(java.lang.String, java.util.Map, org.apache.lucene.analysis.Analyzer)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchStringMapOfStringIntegerAnalyzer() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      String queryExpected = "test:foo";
      Map<String, Integer> facetsExpected = new HashMap<String, Integer>();
      facetsExpected.put("testFacet", 99);
      
      Search target = new Search(queryExpected, facetsExpected, Lucene.getPerFieldAnalyzer());
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual.toString());
      assertEquals(defaultSort, sortActual);
      assertEquals("testFacet", facetsActual.get(0).categoryPath.toString());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#Search(java.lang.String, java.util.Map, org.apache.lucene.analysis.Analyzer, org.apache.lucene.search.Sort)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testSearchStringMapOfStringIntegerAnalyzerSort() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      String queryExpected = "test:foo";
      Map<String, Integer> facetsExpected = new HashMap<String, Integer>();
      Sort sortExpected = Sort.RELEVANCE;
      facetsExpected.put("testFacet", 99);
      
      Search target = new Search(queryExpected, facetsExpected, Lucene.getPerFieldAnalyzer(), sortExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      assertEquals(queryExpected, queryActual.toString());
      assertEquals(sortExpected, sortActual);
      assertEquals("testFacet", facetsActual.get(0).categoryPath.toString());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#addFacet(org.apache.lucene.facet.search.FacetRequest)}.
    */
   @Test
   @SuppressWarnings({ "unchecked" })
   public final void testAddFacetFacetRequest() throws Exception {
      Field facetsField = Search.class.getDeclaredField("facets");
      facetsField.setAccessible(true);
      
      FacetRequest expected = getDumbFacetRequest();
      Search target = new Search("test:foo");
      target.addFacet(expected);
      
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      assertEquals(expected, facetsActual.get(0));
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#addFacet(java.lang.String, int)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testAddFacetStringInt() throws Exception {
      Field facetsField = Search.class.getDeclaredField("facets");
      facetsField.setAccessible(true);
      
      FacetRequest expected = getDumbFacetRequest();
      Search target = new Search("test:foo");
      target.addFacet("test", 100);
      
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      assertEquals("test", facetsActual.get(0).categoryPath.toString());
      assertEquals(100, facetsActual.get(0).numResults);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#getCollector()}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
   public final void testGetCollector() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      Query queryExpected = parser.parse("test:foo");
      List<FacetRequest> facetsExpected = new ArrayList<FacetRequest>();
      Sort sortExpected = Sort.RELEVANCE;
      
      Search target = new Search(queryExpected, facetsExpected, sortExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      TopFieldCollector actual = target.getCollector();
      Class clazz = null;
      Class[] clazzes = TopFieldCollector.class.getDeclaredClasses();
      for (int i = 0; i < clazzes.length; i++) {
         if (clazzes[i].getSimpleName().equals("OutOfOrderOneComparatorNonScoringCollector")) {
            clazz = clazzes[i];
            break;
         }
      }
      assertEquals(clazz, actual.getClass());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#getCollector(int)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testGetCollectorInt() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      Query queryExpected = parser.parse("test:foo");
      List<FacetRequest> facetsExpected = new ArrayList<FacetRequest>();
      Sort sortExpected = Sort.RELEVANCE;
      
      Search target = new Search(queryExpected, facetsExpected, sortExpected);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Test
      TopFieldCollector actual = target.getCollector(99);
      assertEquals("OutOfOrderOneComparatorNonScoringCollector", actual.getClass().getSimpleName());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#getFacetsCollector(org.apache.lucene.index.DirectoryReader, org.apache.lucene.facet.taxonomy.TaxonomyReader)}.
    */
   @Test
   @SuppressWarnings({ "unchecked", "unused" })
   public final void testGetFacetsCollector() throws Exception {
      // Constants
      Field defaultSortField = Search.class.getDeclaredField("DEFAULT_SORT");
      Field defaultFacetsField = Search.class.getDeclaredField("DEFAULT_FACETS");
      Field defaultFacetStringsField = Search.class.getDeclaredField("DEFAULT_FACET_STRINGS");
      Field defaultAnalyzerField = Search.class.getDeclaredField("DEFAULT_ANALYZER");
      Field defaultHitsField = Search.class.getDeclaredField("DEFAULT_HITS");
      
      defaultSortField.setAccessible(true);
      defaultFacetsField.setAccessible(true);
      defaultFacetStringsField.setAccessible(true);
      defaultAnalyzerField.setAccessible(true);
      defaultHitsField.setAccessible(true);
      
      final Sort defaultSort = (Sort) defaultSortField.get(null);
      final List<FacetRequest> defaultFacets = (List<FacetRequest>) defaultFacetsField.get(null);
      final Map<String, Integer> defaultFacetStrings = (Map<String, Integer>) defaultFacetStringsField.get(null);
      final Analyzer defaultAnalyzer = (Analyzer) defaultAnalyzerField.get(null);
      final int defaultHits = defaultHitsField.getInt(null);
      
      // Private members
      Field queryField = Search.class.getDeclaredField("query");
      Field sortField = Search.class.getDeclaredField("sort");
      Field facetsField = Search.class.getDeclaredField("facets");
      
      queryField.setAccessible(true);
      sortField.setAccessible(true);
      facetsField.setAccessible(true);
      
      // Test setup
      QueryParser parser = getQueryParser();
      Query queryExpected = parser.parse("test:foo");
      List<FacetRequest> facetsExpected = new ArrayList<FacetRequest>();
      Sort sortExpected = Sort.RELEVANCE;
      
      Search target = new Search(queryExpected, facetsExpected, sortExpected);
      target.addFacet("test", 100);
      
      // Gather fields
      Query queryActual = (Query) queryField.get(target);
      Sort sortActual = (Sort) sortField.get(target);
      List<FacetRequest> facetsActual = (List<FacetRequest>) facetsField.get(target);
      
      // Set up some dummy indices.
      Directory indexDirectory = new RAMDirectory();
      IndexWriterConfig iwc = new IndexWriterConfig(Lucene.LUCENE_VERSION, Lucene.getPerFieldAnalyzer());
      IndexWriter iw = new IndexWriter(indexDirectory, iwc);
      Directory taxonomyDirectory = new RAMDirectory();
      TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxonomyDirectory, OpenMode.CREATE);
      
      iw.commit();
      tw.commit();
      
      // Test
      FacetsCollector actual =
            target.getFacetsCollector(
                  DirectoryReader.open(indexDirectory),
                  new DirectoryTaxonomyReader(taxonomyDirectory)
            );

      assertEquals("DocsOnlyCollector", actual.getClass().getSimpleName());
      iw.close();
      tw.close();
      taxonomyDirectory.close();
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Search#getQuery()}.
    */
   @Test
   public final void testGetQuery() throws Exception {
      Search target = new Search("test:foo");
      Query actual = target.getQuery();
      
      assertEquals("test:foo", actual.toString());
   }

   private QueryParser getQueryParser() {
      QueryParser parser =
            new QueryParser(
                  Lucene.LUCENE_VERSION,
                  "test",
                  Lucene.getPerFieldAnalyzer()
            );
      return parser;
   }
   
   private FacetRequest getDumbFacetRequest() {
      CategoryPath facetPath = new CategoryPath("testFacet", '/');
      CountFacetRequest facetRequest =
            new CountFacetRequest(facetPath, 100);
      return facetRequest;
   }
}
