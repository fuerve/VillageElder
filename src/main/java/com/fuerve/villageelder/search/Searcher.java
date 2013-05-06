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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * This class provides an interface for regular and faceted search
 * of the revision index.  The bulk of this class is a thin wrapper
 * around the {@link Search} class, slightly decoupling the management
 * of the indexes and the logic of creating and managing a search.
 * @author lparker
 *
 */
public class Searcher {
   private Directory indexDirectory;
   private Directory taxonomyDirectory;
   private String indexDirectoryName;
   private String taxonomyDirectoryName;
   private boolean stringDirectories;

   private DirectoryReader indexReader;
   private IndexSearcher indexSearcher;
   private TaxonomyReader taxonomyReader;
   
   private Search search;
   
   private boolean initialized;
   
   /**
    * Initializes a new instance of Searcher with a preconstructed regular index
    * Directory and taxonomy Directory.
    * @param iindexDirectory The Directory in which the primary Lucene index exists.
    * @param ttaxonomyDirectory The Directory in which the taxonomy index exists.
    */
   public Searcher(final Directory iindexDirectory, final Directory ttaxonomyDirectory) {
      indexDirectory = iindexDirectory;
      taxonomyDirectory = ttaxonomyDirectory;
   }
   
   /**
    * Initializes a new instance of Searcher with String pathnames to directories
    * in which regular and taxonomy indices exist.
    * @param iindexDirectory The pathname of the directory in which the primary Lucene
    * index exists.
    * @param ttaxonomyDirectory The pathname of the directory in which the
    * taxonomy index exists.
    */
   public Searcher(final String iindexDirectory, final String ttaxonomyDirectory) {
      if (iindexDirectory == null || ttaxonomyDirectory == null) {
         throw new IllegalArgumentException("The index and/or taxonomy directories were unspecified");
      }
      
      stringDirectories = true;
      indexDirectoryName = iindexDirectory;
      taxonomyDirectoryName = ttaxonomyDirectory;
   }
   
   /**
    * Performs initialization of the Lucene regular and taxonomy
    * indices by opening the directories and initializing
    * a searcher and taxonomy reader.
    * @throws IOException A fatal exception occurred while interacting
    * with the index directories.
    */
   public void initializeSearch() throws IOException {
      // Don't try to re-initialize an already initialized index.
      if (initialized == true) {
         return;
      }
      
      initializeDirectories();
      initializeSearcher();
      
      initialized = true;
   }
   
   /**
    * Readies the index directories for managing of the regular and
    * taxonomy indices by Lucene.
    * @throws IOException A fatal exception occurred when trying to
    * construct the Lucene Directory objects.  The path may not exist,
    * the Directory may be invalid, or some other error has occurred.
    */
   private void initializeDirectories() throws IOException {
      if (stringDirectories == true) {
         indexDirectory = initializeDirectoryFromString(indexDirectoryName);
         taxonomyDirectory =
               initializeDirectoryFromString(taxonomyDirectoryName);
      }
   }
   
   /**
    * Given a string pathname, returns a valid Lucene Directory object.
    * @param path The pathname in which to create and/or manage a
    * Lucene index.
    * @return The Directory object that contains the specified path.
    * @throws IOException A fatal exception occurred when trying to open
    * the pathname.  The path may not exist in the file system.
    */
   private Directory initializeDirectoryFromString(final String path) throws IOException {
      if (path.isEmpty()) {
         throw new IllegalArgumentException(
               "An empty pathname was specified for the index directory"
         );
      } else {
         return FSDirectory.open(new File(path));
      }
   }
   
   /**
    * Initializes an index searcher and taxonomy reader for
    * search operations.
    * @throws IOException A fatal exception occurred when trying
    * to interact with the indices or their directories.
    */
   private void initializeSearcher() throws IOException {
      if (indexDirectory == null || taxonomyDirectory == null) {
         throw new IllegalArgumentException(
               "Tried to open a searcher on null directories."
         );
      }
      indexReader = DirectoryReader.open(indexDirectory);
      indexSearcher = new IndexSearcher(indexReader);
      taxonomyReader = new DirectoryTaxonomyReader(taxonomyDirectory);
   }
   
   /**
    * Initializes a new instance of Search with a query.
    * @param qquery The query to be executed during the search.
    */
   public Search createSearch(final Query query) {
      search = new Search(query);
      return search;
   }
   
   /**
    * Initializes a new instance of Search with a query and
    * a custom sort order.
    * @param qquery The query to be executed during the search.
    * @param ssort The custom sort order.
    */
   public Search createSearch(final Query query, final Sort sort) {
      search = new Search(query, sort);
      return search;
   }
   
   /**
    * Initializes a new instance of Search with a query and a
    * set of facets (in {@link FacetRequest} form).
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    */
   public Search createSearch(final Query query, final List<FacetRequest> facets) {
      search = new Search(query, facets);
      return search;
   }
   
   /**
    * Initializes a new instance of Search with a query, a set
    * of facets (in {@link FacetRequest} form) and a custom
    * sort.
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    * @param ssort The custom sort order.
    */
   public Search createSearch(
         final Query query,
         final List<FacetRequest> facets,
         final Sort sort) {
      search = new Search(query, facets, sort);
      return search;
   }
   
   /**
    * Initializes a new instance of Search with a query.
    * @param qquery The query to be executed during the search.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search createSearch(final String query)
         throws ParseException {
      search = new Search(query);
      return search;
   }
   
   /**
    * Initializes a new instance of Search with a query and
    * a custom sort order.
    * @param qquery The query to be executed during the search.
    * @param ssort The custom sort order.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search createSearch(final String query, final Sort sort)
      throws ParseException {
      search = new Search(query, sort);
      return search;
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
   public Search createSearch(final String query, final Analyzer analyzer)
      throws ParseException {
      search = new Search(query, analyzer);
      return search;
   }
   
   /**
    * Initializes a new instance of Search with a query and a
    * set of facets (in {@link String} form).
    * @param qquery The query to be executed during the search.
    * @param ffacets The facets for which to search.
    * @throws ParseException A fatal exception occurred while
    * parsing the String query.
    */
   public Search createSearch(
         final String query,
         final Map<String, Integer> facets)
      throws ParseException {
      search = new Search(query, facets);
      return search;
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
   public Search createSearch(
         final String query,
         final Map<String, Integer> facets,
         final Sort sort)
      throws ParseException {
      search = new Search(query, facets, sort);
      return search;
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
   public Search createSearch(
         final String query,
         final Map<String, Integer> facets,
         final Analyzer analyzer)
      throws ParseException {
      search = new Search(query, facets, analyzer);
      return search;
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
   public Search createSearch(
         final String query,
         final Map<String, Integer> facets,
         final Analyzer analyzer,
         final Sort sort)
      throws ParseException {
      search = new Search(query, facets, analyzer, sort);
      return search;
   }
   
   /**
    * Gets this Searcher's {@link Search} object.  This will be null
    * if the search hasn't been initialized using one of the
    * {@link Searcher#createSearch} methods.
    * @return This Searcher's {@link Search} instance.
    */
   public Search getSearch() {
      return search;
   }
   
   /**
    * Given a maximum hit count, returns a Collector instance for the current
    * search that can be used with an IndexSearcher to return results from
    * the index.
    * @param count The maximum hit count, or number of documents, to return
    * from the search.
    * @return A Collector suitable for use with an IndexSearcher.
    * @throws IOException A fatal exception occurred while interacting with
    * the index.
    */
   public TopFieldCollector getCollector(final int count)
         throws IOException {
      if (search == null) {
         return null;
      } else {
         return search.getCollector(count);
      }
   }
   
   /**
    * Gets a FacetsCollector instance for the current search.
    * @return The {@link FacetsCollector} containing the requested
    * facets for this search.
    */
   public FacetsCollector getFacetsCollector() {
      if (search == null) {
         return null;
      } else {
         return search.getFacetsCollector(indexReader, taxonomyReader);
      }
   }
   
   /**
    * Provided a search has been created, this method will execute
    * that search with a given {@link Collector}.
    * @param results The {@link Collector} in which to aggregate results.
    * @throws IOException A fatal exception occurred while interacting
    * with the index.
    */
   public void search(final Collector results) throws IOException {
      if (search != null) {
         indexSearcher.search(search.getQuery(), results);
      }
   }
   
   /**
    * Provided a search has been created, this method will execute
    * that search and aggregate its results into the Search object's
    * collectors.  These collectors may be accessed using the
    * accessors of the Search object.
    * @throws IOException A fatal exception occurred while interacting
    * with the index.
    */
   public void search() throws IOException {
      if (search != null) {
         final Collector collector = search.getCollector();
         final FacetsCollector facetsCollector = search.getFacetsCollector(indexReader, taxonomyReader);
         indexSearcher.search(
               search.getQuery(),
               MultiCollector.wrap(collector, facetsCollector)
         );
      }
   }
   
   /**
    * Provided a search has been created, this method will execute
    * that search with a non-default max hit count
    * and aggregate its results into the Search object's
    * collectors.  These collectors may be accessed using the
    * accessors of the Search object.
    * @param count The maximum hit count to allow the search to return.
    * @throws IOException A fatal exception occurred while interacting
    * with the index.
    */
   public void search(final int count) throws IOException {
      if (search != null) {
         Collector collector = search.getCollector(count);
         FacetsCollector facetsCollector = search.getFacetsCollector(indexReader, taxonomyReader);
         indexSearcher.search(
               search.getQuery(),
               MultiCollector.wrap(collector, facetsCollector)
         );
      }
   }
   
   /**
    * Obtains a document from the index by numeric ID.  Generally
    * only useful after a search has been executed and a collection
    * of document IDs has been obtained.
    * @param docID The numeric ID of the Document to retrieve from the index.
    * @return The Lucene Document in the index corresponding to the ID.
    * @throws IOException A fatal exception has occurred while interacting
    * with the index.
    */
   public Document doc(final int docID) throws IOException {
      if (indexSearcher != null) {
         return indexSearcher.doc(docID);
      } else {
         return null;
      }
   }
   
   /**
    * Closes and disposes of open resources.
    * @throws IOException A fatal exception occurred while
    * attempting to close index readers.
    */
   public void dispose() throws IOException {
      if (initialized) {
         indexReader.close();
         taxonomyReader.close();
         initialized = false;
      }
   }
}
