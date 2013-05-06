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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.index.FacetFields;
import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.fuerve.villageelder.common.Lucene;

/**
 * Unit tests for Searcher.
 * @author lparker
 *
 */
public class SearcherTest {

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#Searcher(org.apache.lucene.store.Directory, org.apache.lucene.store.Directory)}.
    */
   @SuppressWarnings("unused")
   @Test
   public final void testSearcherDirectoryDirectory() throws Exception {
      // Gather declared fields.
      Field indexDirectoryField = Searcher.class.getDeclaredField("indexDirectory");
      Field taxonomyDirectoryField = Searcher.class.getDeclaredField("taxonomyDirectory");
      Field indexDirectoryNameField = Searcher.class.getDeclaredField("indexDirectoryName");
      Field taxonomyDirectoryNameField = Searcher.class.getDeclaredField("taxonomyDirectoryName");
      Field stringDirectoriesField = Searcher.class.getDeclaredField("stringDirectories");
      Field initializedField = Searcher.class.getDeclaredField("initialized");
      Field searchField = Searcher.class.getDeclaredField("search");
      
      indexDirectoryField.setAccessible(true);
      taxonomyDirectoryField.setAccessible(true);
      indexDirectoryNameField.setAccessible(true);
      taxonomyDirectoryNameField.setAccessible(true);
      stringDirectoriesField.setAccessible(true);
      initializedField.setAccessible(true);
      searchField.setAccessible(true);
      
      // Setup
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      Searcher target = new Searcher(indexDirectoryExpected, taxonomyDirectoryExpected);
      
      // Gather field values.
      Directory indexDirectoryActual = (Directory) indexDirectoryField.get(target);
      Directory taxonomyDirectoryActual = (Directory) taxonomyDirectoryField.get(target);
      String indexDirectoryNameActual = (String) indexDirectoryNameField.get(target);
      String taxonomyDirectoryNameActual = (String) taxonomyDirectoryNameField.get(target);
      boolean stringDirectoriesActual = stringDirectoriesField.getBoolean(target);
      boolean initializedActual = initializedField.getBoolean(target);
      Search searchFieldActual = (Search) searchField.get(target);
      
      // Test
      assertEquals(indexDirectoryExpected, indexDirectoryActual);
      assertEquals(false, stringDirectoriesActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#Searcher(java.lang.String, java.lang.String)}.
    * This test is disabled and unimplemented because this constructor
    * takes strings which are then opened as local paths.  I don't
    * like to write tests that have any external I/O dependencies unless
    * they are proper integration tests.  Note to self: try to exercise
    * this code in an automated integration test.
    */
   //@Test
   public final void testSearcherStringString() throws Exception {
      fail("Not yet implemented");
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#initializeSearch()}.
    */
   @SuppressWarnings("unused")
   @Test
   public final void testInitializeSearch() throws Exception {
      // Gather declared fields.
      Field indexDirectoryField = Searcher.class.getDeclaredField("indexDirectory");
      Field taxonomyDirectoryField = Searcher.class.getDeclaredField("taxonomyDirectory");
      Field indexDirectoryNameField = Searcher.class.getDeclaredField("indexDirectoryName");
      Field taxonomyDirectoryNameField = Searcher.class.getDeclaredField("taxonomyDirectoryName");
      Field stringDirectoriesField = Searcher.class.getDeclaredField("stringDirectories");
      Field initializedField = Searcher.class.getDeclaredField("initialized");
      Field searchField = Searcher.class.getDeclaredField("search");
      Field indexReaderField = Searcher.class.getDeclaredField("indexReader");
      Field indexSearcherField = Searcher.class.getDeclaredField("indexSearcher");
      Field taxonomyReaderField = Searcher.class.getDeclaredField("taxonomyReader");
      
      indexDirectoryField.setAccessible(true);
      taxonomyDirectoryField.setAccessible(true);
      indexDirectoryNameField.setAccessible(true);
      taxonomyDirectoryNameField.setAccessible(true);
      stringDirectoriesField.setAccessible(true);
      initializedField.setAccessible(true);
      searchField.setAccessible(true);
      indexReaderField.setAccessible(true);
      indexSearcherField.setAccessible(true);
      taxonomyReaderField.setAccessible(true);
      
      // Setup
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      IndexWriterConfig iwc = new IndexWriterConfig(Lucene.LUCENE_VERSION, Lucene.getPerFieldAnalyzer());
      IndexWriter iw = new IndexWriter(indexDirectoryExpected, iwc);
      TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxonomyDirectoryExpected, OpenMode.CREATE);
      
      iw.commit();
      tw.commit();
      
      Searcher target = new Searcher(indexDirectoryExpected, taxonomyDirectoryExpected);
      target.initializeSearch();
      
      // Gather field values.
      Directory indexDirectoryActual = (Directory) indexDirectoryField.get(target);
      Directory taxonomyDirectoryActual = (Directory) taxonomyDirectoryField.get(target);
      String indexDirectoryNameActual = (String) indexDirectoryNameField.get(target);
      String taxonomyDirectoryNameActual = (String) taxonomyDirectoryNameField.get(target);
      boolean stringDirectoriesActual = stringDirectoriesField.getBoolean(target);
      boolean initializedActual = initializedField.getBoolean(target);
      Search searchFieldActual = (Search) searchField.get(target);
      IndexReader indexReaderActual = (IndexReader) indexReaderField.get(target);
      IndexSearcher indexSearcherActual = (IndexSearcher) indexSearcherField.get(target);
      TaxonomyReader taxonomyReaderActual = (TaxonomyReader) taxonomyReaderField.get(target);
      
      // Test
      assertEquals(true, initializedActual);
      assertNotNull(indexReaderActual);
      assertNotNull(indexSearcherActual);
      assertNotNull(taxonomyReaderActual);
      
      // Finish
      tw.close();
      iw.close();
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#search(org.apache.lucene.search.Collector)}.
    */
   @SuppressWarnings("unused")
   @Test
   public final void testSearchCollector() throws Exception {
      // Gather declared fields.
      Field indexDirectoryField = Searcher.class.getDeclaredField("indexDirectory");
      Field taxonomyDirectoryField = Searcher.class.getDeclaredField("taxonomyDirectory");
      Field indexDirectoryNameField = Searcher.class.getDeclaredField("indexDirectoryName");
      Field taxonomyDirectoryNameField = Searcher.class.getDeclaredField("taxonomyDirectoryName");
      Field stringDirectoriesField = Searcher.class.getDeclaredField("stringDirectories");
      Field initializedField = Searcher.class.getDeclaredField("initialized");
      Field searchField = Searcher.class.getDeclaredField("search");
      Field indexReaderField = Searcher.class.getDeclaredField("indexReader");
      Field indexSearcherField = Searcher.class.getDeclaredField("indexSearcher");
      Field taxonomyReaderField = Searcher.class.getDeclaredField("taxonomyReader");
      
      indexDirectoryField.setAccessible(true);
      taxonomyDirectoryField.setAccessible(true);
      indexDirectoryNameField.setAccessible(true);
      taxonomyDirectoryNameField.setAccessible(true);
      stringDirectoriesField.setAccessible(true);
      initializedField.setAccessible(true);
      searchField.setAccessible(true);
      indexReaderField.setAccessible(true);
      indexSearcherField.setAccessible(true);
      taxonomyReaderField.setAccessible(true);
      
      // Setup
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      buildDummyIndex(indexDirectoryExpected, taxonomyDirectoryExpected);
      
      Searcher target = new Searcher(indexDirectoryExpected, taxonomyDirectoryExpected);
      target.initializeSearch();
      
      // Gather field values.
      Directory indexDirectoryActual = (Directory) indexDirectoryField.get(target);
      Directory taxonomyDirectoryActual = (Directory) taxonomyDirectoryField.get(target);
      String indexDirectoryNameActual = (String) indexDirectoryNameField.get(target);
      String taxonomyDirectoryNameActual = (String) taxonomyDirectoryNameField.get(target);
      boolean stringDirectoriesActual = stringDirectoriesField.getBoolean(target);
      boolean initializedActual = initializedField.getBoolean(target);
      Search searchFieldActual = (Search) searchField.get(target);
      IndexReader indexReaderActual = (IndexReader) indexReaderField.get(target);
      IndexSearcher indexSearcherActual = (IndexSearcher) indexSearcherField.get(target);
      TaxonomyReader taxonomyReaderActual = (TaxonomyReader) taxonomyReaderField.get(target);
      
      // Create the Collector to be passed in and execute a search to populate it.
      final TopFieldCollector collector = getDummyCollector();
      final FacetsCollector facetsCollector =
            getDummyFacetsCollector((DirectoryReader) indexReaderActual, taxonomyReaderActual);
      final Collector testCollector = MultiCollector.wrap(collector, facetsCollector);
      
      target.createSearch("Revision:5*");
      target.search(testCollector);
      
      // Test
      assertEquals(true, initializedActual);
      assertEquals(2, collector.topDocs().totalHits);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#search()}.
    */
   @SuppressWarnings("unused")
   @Test
   public final void testSearch() throws Exception {
      // Gather declared fields.
      Field indexDirectoryField = Searcher.class.getDeclaredField("indexDirectory");
      Field taxonomyDirectoryField = Searcher.class.getDeclaredField("taxonomyDirectory");
      Field indexDirectoryNameField = Searcher.class.getDeclaredField("indexDirectoryName");
      Field taxonomyDirectoryNameField = Searcher.class.getDeclaredField("taxonomyDirectoryName");
      Field stringDirectoriesField = Searcher.class.getDeclaredField("stringDirectories");
      Field initializedField = Searcher.class.getDeclaredField("initialized");
      Field searchField = Searcher.class.getDeclaredField("search");
      Field indexReaderField = Searcher.class.getDeclaredField("indexReader");
      Field indexSearcherField = Searcher.class.getDeclaredField("indexSearcher");
      Field taxonomyReaderField = Searcher.class.getDeclaredField("taxonomyReader");
      
      indexDirectoryField.setAccessible(true);
      taxonomyDirectoryField.setAccessible(true);
      indexDirectoryNameField.setAccessible(true);
      taxonomyDirectoryNameField.setAccessible(true);
      stringDirectoriesField.setAccessible(true);
      initializedField.setAccessible(true);
      searchField.setAccessible(true);
      indexReaderField.setAccessible(true);
      indexSearcherField.setAccessible(true);
      taxonomyReaderField.setAccessible(true);
      
      // Setup
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      buildDummyIndex(indexDirectoryExpected, taxonomyDirectoryExpected);
      
      Searcher target = new Searcher(indexDirectoryExpected, taxonomyDirectoryExpected);
      target.initializeSearch();
      
      // Gather field values.
      Directory indexDirectoryActual = (Directory) indexDirectoryField.get(target);
      Directory taxonomyDirectoryActual = (Directory) taxonomyDirectoryField.get(target);
      String indexDirectoryNameActual = (String) indexDirectoryNameField.get(target);
      String taxonomyDirectoryNameActual = (String) taxonomyDirectoryNameField.get(target);
      boolean stringDirectoriesActual = stringDirectoriesField.getBoolean(target);
      boolean initializedActual = initializedField.getBoolean(target);
      Search searchFieldActual = (Search) searchField.get(target);
      IndexReader indexReaderActual = (IndexReader) indexReaderField.get(target);
      IndexSearcher indexSearcherActual = (IndexSearcher) indexSearcherField.get(target);
      TaxonomyReader taxonomyReaderActual = (TaxonomyReader) taxonomyReaderField.get(target);
      
      // Execute the search.
      target.createSearch("Revision:5*");
      Search searchActual = target.getSearch();
      searchActual.addFacet("Author", 1);
      target.search();
      
      // Test
      assertNotNull(searchActual);
      
      TopFieldCollector collector = searchActual.getCollector();
      FacetsCollector facetsCollector = searchActual.getFacetsCollector((DirectoryReader) indexReaderActual, taxonomyReaderActual);
      assertEquals(true, initializedActual);
      assertEquals(2, collector.topDocs().totalHits);
      assertEquals(1, facetsCollector.getFacetResults().size());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#search(int)}.
    */
   @SuppressWarnings("unused")
   @Test
   public final void testSearchInt() throws Exception {
      // Gather declared fields.
      Field indexDirectoryField = Searcher.class.getDeclaredField("indexDirectory");
      Field taxonomyDirectoryField = Searcher.class.getDeclaredField("taxonomyDirectory");
      Field indexDirectoryNameField = Searcher.class.getDeclaredField("indexDirectoryName");
      Field taxonomyDirectoryNameField = Searcher.class.getDeclaredField("taxonomyDirectoryName");
      Field stringDirectoriesField = Searcher.class.getDeclaredField("stringDirectories");
      Field initializedField = Searcher.class.getDeclaredField("initialized");
      Field searchField = Searcher.class.getDeclaredField("search");
      Field indexReaderField = Searcher.class.getDeclaredField("indexReader");
      Field indexSearcherField = Searcher.class.getDeclaredField("indexSearcher");
      Field taxonomyReaderField = Searcher.class.getDeclaredField("taxonomyReader");
      
      indexDirectoryField.setAccessible(true);
      taxonomyDirectoryField.setAccessible(true);
      indexDirectoryNameField.setAccessible(true);
      taxonomyDirectoryNameField.setAccessible(true);
      stringDirectoriesField.setAccessible(true);
      initializedField.setAccessible(true);
      searchField.setAccessible(true);
      indexReaderField.setAccessible(true);
      indexSearcherField.setAccessible(true);
      taxonomyReaderField.setAccessible(true);
      
      // Setup
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      buildDummyIndex(indexDirectoryExpected, taxonomyDirectoryExpected);
      
      Searcher target = new Searcher(indexDirectoryExpected, taxonomyDirectoryExpected);
      target.initializeSearch();
      
      // Gather field values.
      Directory indexDirectoryActual = (Directory) indexDirectoryField.get(target);
      Directory taxonomyDirectoryActual = (Directory) taxonomyDirectoryField.get(target);
      String indexDirectoryNameActual = (String) indexDirectoryNameField.get(target);
      String taxonomyDirectoryNameActual = (String) taxonomyDirectoryNameField.get(target);
      boolean stringDirectoriesActual = stringDirectoriesField.getBoolean(target);
      boolean initializedActual = initializedField.getBoolean(target);
      Search searchFieldActual = (Search) searchField.get(target);
      IndexReader indexReaderActual = (IndexReader) indexReaderField.get(target);
      IndexSearcher indexSearcherActual = (IndexSearcher) indexSearcherField.get(target);
      TaxonomyReader taxonomyReaderActual = (TaxonomyReader) taxonomyReaderField.get(target);
      
      // Execute the search.
      target.createSearch("Revision:5*");
      Search searchActual = target.getSearch();
      searchActual.addFacet("Author", 1);
      target.search(1);
      
      // Test
      assertNotNull(searchActual);
      
      TopFieldCollector collector = searchActual.getCollector();
      FacetsCollector facetsCollector = searchActual.getFacetsCollector((DirectoryReader) indexReaderActual, taxonomyReaderActual);
      assertEquals(true, initializedActual);
      assertEquals(1, collector.topDocs().scoreDocs.length);
      assertEquals(1, facetsCollector.getFacetResults().size());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#doc(int)}.
    */
   @SuppressWarnings("unused")
   @Test
   public final void testDoc() throws Exception {
      // Gather declared fields.
      Field indexDirectoryField = Searcher.class.getDeclaredField("indexDirectory");
      Field taxonomyDirectoryField = Searcher.class.getDeclaredField("taxonomyDirectory");
      Field indexDirectoryNameField = Searcher.class.getDeclaredField("indexDirectoryName");
      Field taxonomyDirectoryNameField = Searcher.class.getDeclaredField("taxonomyDirectoryName");
      Field stringDirectoriesField = Searcher.class.getDeclaredField("stringDirectories");
      Field initializedField = Searcher.class.getDeclaredField("initialized");
      Field searchField = Searcher.class.getDeclaredField("search");
      Field indexReaderField = Searcher.class.getDeclaredField("indexReader");
      Field indexSearcherField = Searcher.class.getDeclaredField("indexSearcher");
      Field taxonomyReaderField = Searcher.class.getDeclaredField("taxonomyReader");
      
      indexDirectoryField.setAccessible(true);
      taxonomyDirectoryField.setAccessible(true);
      indexDirectoryNameField.setAccessible(true);
      taxonomyDirectoryNameField.setAccessible(true);
      stringDirectoriesField.setAccessible(true);
      initializedField.setAccessible(true);
      searchField.setAccessible(true);
      indexReaderField.setAccessible(true);
      indexSearcherField.setAccessible(true);
      taxonomyReaderField.setAccessible(true);
      
      // Setup
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      buildDummyIndex(indexDirectoryExpected, taxonomyDirectoryExpected);
      
      Searcher target = new Searcher(indexDirectoryExpected, taxonomyDirectoryExpected);
      target.initializeSearch();
      
      // Gather field values.
      Directory indexDirectoryActual = (Directory) indexDirectoryField.get(target);
      Directory taxonomyDirectoryActual = (Directory) taxonomyDirectoryField.get(target);
      String indexDirectoryNameActual = (String) indexDirectoryNameField.get(target);
      String taxonomyDirectoryNameActual = (String) taxonomyDirectoryNameField.get(target);
      boolean stringDirectoriesActual = stringDirectoriesField.getBoolean(target);
      boolean initializedActual = initializedField.getBoolean(target);
      Search searchFieldActual = (Search) searchField.get(target);
      IndexReader indexReaderActual = (IndexReader) indexReaderField.get(target);
      IndexSearcher indexSearcherActual = (IndexSearcher) indexSearcherField.get(target);
      TaxonomyReader taxonomyReaderActual = (TaxonomyReader) taxonomyReaderField.get(target);
      
      // Execute the search.
      target.createSearch("Revision:5*");
      Search searchActual = target.getSearch();
      searchActual.addFacet("Author", 1);
      target.search(1);
      
      // Test
      assertNotNull(searchActual);
      
      TopFieldCollector collector = searchActual.getCollector();
      FacetsCollector facetsCollector = searchActual.getFacetsCollector((DirectoryReader) indexReaderActual, taxonomyReaderActual);
      
      ScoreDoc[] docs = collector.topDocs().scoreDocs;
      for (int i = 0; i < docs.length; i++) {
         Document doc = target.doc(i);
         assertNotNull(doc.getField("Author"));
      }
   }

   /**
    * Test method for {@link com.fuerve.villageelder.search.Searcher#dispose()}.
    */
   @SuppressWarnings("unused")
   @Test(expected = AlreadyClosedException.class)
   public final void testDispose() throws Exception {
      // Gather declared fields.
      Field indexDirectoryField = Searcher.class.getDeclaredField("indexDirectory");
      Field taxonomyDirectoryField = Searcher.class.getDeclaredField("taxonomyDirectory");
      Field indexDirectoryNameField = Searcher.class.getDeclaredField("indexDirectoryName");
      Field taxonomyDirectoryNameField = Searcher.class.getDeclaredField("taxonomyDirectoryName");
      Field stringDirectoriesField = Searcher.class.getDeclaredField("stringDirectories");
      Field initializedField = Searcher.class.getDeclaredField("initialized");
      Field searchField = Searcher.class.getDeclaredField("search");
      Field indexReaderField = Searcher.class.getDeclaredField("indexReader");
      Field indexSearcherField = Searcher.class.getDeclaredField("indexSearcher");
      Field taxonomyReaderField = Searcher.class.getDeclaredField("taxonomyReader");
      
      indexDirectoryField.setAccessible(true);
      taxonomyDirectoryField.setAccessible(true);
      indexDirectoryNameField.setAccessible(true);
      taxonomyDirectoryNameField.setAccessible(true);
      stringDirectoriesField.setAccessible(true);
      initializedField.setAccessible(true);
      searchField.setAccessible(true);
      indexReaderField.setAccessible(true);
      indexSearcherField.setAccessible(true);
      taxonomyReaderField.setAccessible(true);
      
      // Setup
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      buildDummyIndex(indexDirectoryExpected, taxonomyDirectoryExpected);
      
      Searcher target = new Searcher(indexDirectoryExpected, taxonomyDirectoryExpected);
      target.initializeSearch();

      target.createSearch("Revision:5*");
      Search searchActual = target.getSearch();
      searchActual.addFacet("Author", 1);
      target.search(1);
      target.dispose();

      // Gather field values.
      Directory indexDirectoryActual = (Directory) indexDirectoryField.get(target);
      Directory taxonomyDirectoryActual = (Directory) taxonomyDirectoryField.get(target);
      String indexDirectoryNameActual = (String) indexDirectoryNameField.get(target);
      String taxonomyDirectoryNameActual = (String) taxonomyDirectoryNameField.get(target);
      boolean stringDirectoriesActual = stringDirectoriesField.getBoolean(target);
      boolean initializedActual = initializedField.getBoolean(target);
      Search searchFieldActual = (Search) searchField.get(target);
      IndexReader indexReaderActual = (IndexReader) indexReaderField.get(target);
      IndexSearcher indexSearcherActual = (IndexSearcher) indexSearcherField.get(target);
      TaxonomyReader taxonomyReaderActual = (TaxonomyReader) taxonomyReaderField.get(target);

      
      // Test
      assertEquals(false, initializedActual);
      
      target.search();
   }

   private void buildDummyIndex(
         final Directory indexDirectory,
         final Directory taxonomyDirectory) throws IOException {
      IndexWriterConfig iwc = new IndexWriterConfig(Lucene.LUCENE_VERSION, Lucene.getPerFieldAnalyzer());
      iwc.setOpenMode(OpenMode.CREATE);
      IndexWriter iw = new IndexWriter(indexDirectory, iwc);
      TaxonomyWriter tw = new DirectoryTaxonomyWriter(taxonomyDirectory, OpenMode.CREATE);
      List<CategoryPath> categories = new ArrayList<CategoryPath>();
      FacetFields facetFields = new FacetFields(tw);
      
      Document doc = new Document();
      categories.clear();
      doc.add(new StringField("Author", "foo", Store.YES));
      categories.add(new CategoryPath("Author", "foo"));
      doc.add(new LongField("RevisionNumber", 50L, Store.YES));
      doc.add(new StringField("Revision", "50", Store.YES));
      doc.add(new TextField("Message", "stuff", Store.YES));
      iw.addDocument(doc);
      facetFields.addFields(doc, categories);
      
      doc = new Document();
      facetFields = new FacetFields(tw);
      categories.clear();
      doc.add(new StringField("Author", "bar", Store.YES));
      categories.add(new CategoryPath("Author", "bar"));
      doc.add(new LongField("RevisionNumber", 5000L, Store.YES));
      doc.add(new StringField("Revision", "5000", Store.YES));
      doc.add(new TextField("Message", "stuff", Store.YES));
      iw.addDocument(doc);
      facetFields.addFields(doc, categories);
      
      tw.commit();
      tw.close();
      iw.commit();
      iw.close();
   }
   
   private TopFieldCollector getDummyCollector() throws Exception {
      TopFieldCollector collector =
            TopFieldCollector.create(
                  Sort.RELEVANCE,
                  100,
                  true,
                  false,
                  false,
                  false);

      return collector;
   }
   
   public FacetsCollector getDummyFacetsCollector(
         final DirectoryReader indexReader,
         final TaxonomyReader taxonomyReader) {
      
      final List<FacetRequest> facets = new ArrayList<FacetRequest>();
      final CategoryPath facetPath = new CategoryPath("Author", '/');
      
      final CountFacetRequest facetRequest =
            new CountFacetRequest(facetPath, 10);
      facets.add(facetRequest);
      
      final FacetSearchParams facetSearchParams =
            new FacetSearchParams(facets);
      final FacetsCollector facetsCollector =
            FacetsCollector.create(
                  facetSearchParams,
                  indexReader,
                  taxonomyReader);

      return facetsCollector;
   }
}
