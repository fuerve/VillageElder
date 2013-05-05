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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.SortField.Type;

import com.fuerve.villageelder.common.Lucene;

/**
 * This class represents a single search of the revision log
 * index.
 * @author lparker
 *
 */
public class Search {
   // By default, searches are sorted by descending revision number.
   private static final Sort DEFAULT_SORT =
         new Sort(new SortField("RevisionNumber", Type.LONG, true));
   private static final List<FacetRequest> DEFAULT_FACETS = null;
   private static final Map<String, Integer> DEFAULT_FACET_STRINGS = null;
   private static final Analyzer DEFAULT_ANALYZER = Lucene.getPerFieldAnalyzer();
   private static final int DEFAULT_HITS = 100;
   
   private final Query query;
   private final Sort sort;

   private List<FacetRequest> facets;
   
   private TopFieldCollector collector;
   private FacetsCollector facetsCollector;

   /**
    * Initializes a new instance of Search with a query.
    * @param qquery The query to be executed during the search.
    */
   public Search(final Query qquery) {
      this(qquery, DEFAULT_FACETS, DEFAULT_SORT);
   }
   
   /**
    * Initializes a new instance of Search with a query and
    * a custom sort order.
    * @param qquery The query to be executed during the search.
    * @param ssort The custom sort order.
    */
   public Search(final Query qquery, final Sort ssort) {
      this(qquery, DEFAULT_FACETS, ssort);
   }
   
   /**
    * Initializes a new instance of Search with a query and a
    * set of facets (in {@link FacetRequest} form).
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    */
   public Search(final Query qquery, final List<FacetRequest> ffacets) {
      this(qquery, ffacets, DEFAULT_SORT);
   }
   
   /**
    * Initializes a new instance of Search with a query, a set
    * of facets (in {@link FacetRequest} form) and a custom
    * sort.
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    * @param ssort The custom sort order.
    */
   public Search(
         final Query qquery,
         final List<FacetRequest> ffacets,
         final Sort ssort) {
      query = qquery;
      facets = ffacets;
      sort = ssort;
   }
   
   
   /**
    * Initializes a new instance of Search with a query.
    * @param qquery The query to be executed during the search.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search(final String qquery) throws ParseException {
      this(qquery, DEFAULT_FACET_STRINGS, DEFAULT_ANALYZER, DEFAULT_SORT);
   }
   
   /**
    * Initializes a new instance of Search with a query and
    * a custom sort order.
    * @param qquery The query to be executed during the search.
    * @param ssort The custom sort order.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search(final String qquery, final Sort ssort)
      throws ParseException {
      this(qquery, DEFAULT_FACET_STRINGS, DEFAULT_ANALYZER, ssort);
   }
   
   /**
    * Initializes a new instance of Search with a query and
    * an analyzer.
    * @param qquery The query to be executed during the search.
    * @param aanalyzer The analyzer with which to parse the
    * query.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search(final String qquery, final Analyzer aanalyzer)
      throws ParseException {
      this(qquery, DEFAULT_FACET_STRINGS, aanalyzer, DEFAULT_SORT);
   }
   
   /**
    * Initializes a new instance of Search with a query and a
    * set of facets (in {@link String} form).
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search(
         final String qquery,
         final Map<String, Integer> ffacets)
      throws ParseException {
      this(qquery, ffacets, DEFAULT_ANALYZER, DEFAULT_SORT);
   }
   
   /**
    * Initializes a new instance of Search with a query, a set
    * of facets (in {@link String} form with integer counts) and a custom
    * sort.
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    * @param ssort The custom sort order.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search(
         final String qquery,
         final Map<String, Integer> ffacets,
         final Sort ssort)
      throws ParseException {
      this(qquery, ffacets, DEFAULT_ANALYZER, ssort);
   }
   
   /**
    * Initializes a new instance of Search with a query,
    * a set of facets (in {@link String} form with integer counts)
    * and an analyzer.
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    * @param aanalyzer The analyzer with which to parse the
    * query.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search(
         final String qquery,
         final Map<String, Integer> ffacets,
         final Analyzer aanalyzer)
      throws ParseException {
      this(qquery, ffacets, aanalyzer, DEFAULT_SORT);
   }
   
   /**
    * Initializes a new instance of Search with a query,
    * a set of facets (in {@link String} form with integer counts), an
    * analyzer and a custom sort.
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    * @param aanalyzer The analyzer with which to parse the
    * query.
    * @param ssort The custom sort order.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search(
         final String qquery,
         final Map<String, Integer> ffacets,
         final Analyzer aanalyzer,
         final Sort ssort)
               throws ParseException {
      QueryParser parser =
            new SearchQueryParser(
                  Lucene.LUCENE_VERSION,
                  Lucene.DEFAULT_QUERY_FIELD,
                  aanalyzer
             );
      parser.setDateResolution(Resolution.HOUR);
      query = parser.parse(qquery);
      
      if (ffacets == null) {
         facets = null;
      } else {
         facets = new ArrayList<FacetRequest>(ffacets.size());
         
         for (Entry<String, Integer> facet : ffacets.entrySet()) {
            final CategoryPath facetPath =
                  new CategoryPath(facet.getKey(), '/');
            final CountFacetRequest facetRequest =
                  new CountFacetRequest(facetPath, facet.getValue());
            facets.add(facetRequest);
         }
      }
      
      sort = ssort;
   }
   
   /**
    * Adds a facet request to the existing list of facet requests,
    * or creates a new list of facet requests if one does not
    * already exist.
    * @param facetRequest The facet request to add to the list.
    */
   public void addFacet(final FacetRequest facetRequest) {
      if (facetRequest == null) {
         return;
      }
      
      if (facets == null) {
         facets = new ArrayList<FacetRequest>();
      }
      facets.add(facetRequest);
   }
   
   /**
    * Adds a facet request to the existing list of facet requests,
    * or creates a new list of facet requests if one does not
    * already exist.  Calling this method implies that the facet
    * category path is delimited by a forward-slash ('/')
    * character and that the facet request is a CountFacetRequest.
    * @param name The string name of the category path (facet) being
    * requested.
    * @param count The maximum number of facet results to return for
    * this facet.
    */
   public void addFacet(final String name, final int count) {
      if (name == null) {
         return;
      }
      
      if (facets == null) {
         facets = new ArrayList<FacetRequest>();
      }
      
      CategoryPath facetPath = new CategoryPath(name, '/');
      CountFacetRequest facetRequest =
            new CountFacetRequest(facetPath, count);
      facets.add(facetRequest);
   }
   
   /**
    * Returns a Collector instance for this search that can be used with an
    * IndexSearcher to return results from the index.  If one has not already
    * been created (by calling {@link Search#getCollector(int)}), one will
    * be created with a default max hit count of 100 documents.
    * @return A Collector suitable for use with an IndexSearcher.
    * @throws IOException A fatal exception occurred while interacting
    * with the index.
    */
   public TopFieldCollector getCollector() throws IOException {
      if (collector == null) {
         collector =
               TopFieldCollector.create(sort, DEFAULT_HITS, true, false, false, false);
      }
      return collector;
   }
   
   /**
    * Given a maximum hit count, returns a Collector instance for this search
    * that can be used with an IndexSearcher to return results from the index.
    * @param count The maximum hit count, or number of documents, to return
    * from the search.  If this method has already been called, calling it
    * again will cause the argument to be ignored and the preexisting Collector
    * will be returned.  In that case, you might want to use the overload
    * of this method that has no argument.
    * @return A Collector suitable for use with an IndexSearcher.
    * @throws IOException A fatal exception occurred while interacting with
    * the index.
    */
   public TopFieldCollector getCollector(final int count) throws IOException {
      if (collector == null) {
         collector =
               TopFieldCollector.create(sort, count, true, false, false, false);
      }
      return collector;
   }
   
   /**
    * Given an index reader and a taxonomy reader, returns a FacetsCollector
    * appropriate for this search.
    * @param indexReader The {@link DirectoryReader} being used to read from
    * the index.
    * @param taxonomyReader The {@link TaxonomyReader} being used to read from
    * the taxonomy index.
    * @return
    */
   public FacetsCollector getFacetsCollector(
         final DirectoryReader indexReader,
         final TaxonomyReader taxonomyReader) {
      if (facets != null && facetsCollector == null) {
         final FacetSearchParams facetSearchParams =
               new FacetSearchParams(facets);
         facetsCollector =
               FacetsCollector.create(facetSearchParams, indexReader, taxonomyReader);
      }
      return facetsCollector;
   }
   
   /**
    * Gets the primary query for this search.
    * @return The {@link Query} for this search.
    */
   public Query getQuery() {
      return query;
   }
}
