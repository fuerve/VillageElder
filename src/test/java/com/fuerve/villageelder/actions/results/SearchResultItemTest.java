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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.facet.index.FacetFields;
import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.FacetRequest;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.fuerve.villageelder.common.Lucene;
import com.fuerve.villageelder.search.SearchQueryParser;

/**
 * Unit tests for the {@link SearchResultItem} class.
 * @author lparker
 *
 */
public class SearchResultItemTest {

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.SearchResultItem#SearchResultItem(org.apache.lucene.search.TopDocs, java.util.List)}.
    */
   @Test
   public final void testSearchResultItem() throws Exception {
      Directory indexDirectoryExpected = new RAMDirectory();
      Directory taxonomyDirectoryExpected = new RAMDirectory();
      
      buildDummyIndex(indexDirectoryExpected, taxonomyDirectoryExpected);
      
      IndexReader reader = DirectoryReader.open(indexDirectoryExpected);
      IndexSearcher searcher = new IndexSearcher(reader);
      TaxonomyReader taxo = new DirectoryTaxonomyReader(taxonomyDirectoryExpected);
      
      QueryParser parser =
            new SearchQueryParser(
                  Lucene.LUCENE_VERSION,
                  Lucene.DEFAULT_QUERY_FIELD,
                  Lucene.getPerFieldAnalyzer()
            );
      
      TopFieldCollector indexCollector = getDummyCollector();
      FacetsCollector facetsCollector = getDummyFacetsCollector((DirectoryReader) reader, taxo);
      Collector collector = MultiCollector.wrap(indexCollector, facetsCollector);
      searcher.search(parser.parse("Revision:5*"), collector);
      facetsCollector.getFacetResults();
      SearchResultItem target = new SearchResultItem(indexCollector.topDocs(), facetsCollector.getFacetResults());
      
      assertEquals(2, target.getTopDocs().totalHits);
      assertEquals(1, target.getFacetResults().size());
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
   
   private FacetsCollector getDummyFacetsCollector(
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
