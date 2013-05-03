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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.fuerve.villageelder.common.Lucene;

/**
 * Unit tests for {@link SearchQueryParser}.
 * @author lparker
 *
 */
public class SearchQueryParserTest {

   /**
    * Test method for {@link com.fuerve.villageelder.search.SearchQueryParser#getRangeQuery(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}.
    * @throws Exception 
    */
   @Test
   public final void testGetRangeQueryRevisionRange() throws Exception {
      QueryParser target = new SearchQueryParser(Lucene.LUCENE_VERSION, "Message", Lucene.getPerFieldAnalyzer());
      Query testQuery = target.parse("RevisionNumber:[50 TO 100]");
      
      assertEquals(NumericRangeQuery.class, testQuery.getClass());
      
      IndexSearcher searcher = new IndexSearcher(buildDummyIndex());
      ScoreDoc[] docs = searcher.search(testQuery, 10).scoreDocs;
      assertEquals(1, docs.length);
   }
   
   /**
    * Test method for {@link com.fuerve.villageelder.search.SearchQueryParser#getRangeQuery(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}.
    * @throws Exception 
    */
   @Test
   public final void testGetRangeQueryRevisionSingleton() throws Exception {
      QueryParser target = new SearchQueryParser(Lucene.LUCENE_VERSION, "Message", Lucene.getPerFieldAnalyzer());
      Query testQuery = target.parse("Revision:50");
      
      assertEquals(TermQuery.class, testQuery.getClass());
      
      IndexSearcher searcher = new IndexSearcher(buildDummyIndex());
      ScoreDoc[] docs = searcher.search(testQuery, 10).scoreDocs;
      assertEquals(1, docs.length);
   }
   
   /**
    * Test method for {@link com.fuerve.villageelder.search.SearchQueryParser#getRangeQuery(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}.
    * @throws Exception 
    */
   @Test
   public final void testGetRangeQueryDateRange() throws Exception {
      QueryParser target = new SearchQueryParser(Lucene.LUCENE_VERSION, "Message", Lucene.getPerFieldAnalyzer());
      Query testQuery = target.parse("Date:[2007010113 TO 2007010114]");
      
      assertEquals(NumericRangeQuery.class, testQuery.getClass());
   }
   
   /**
    * Test method for {@link com.fuerve.villageelder.search.SearchQueryParser#getRangeQuery(java.lang.String, java.lang.String, java.lang.String, boolean, boolean)}.
    * @throws Exception 
    */
   @Test
   public final void testGetRangeQueryDateSingleton() throws Exception {
      QueryParser target = new SearchQueryParser(Lucene.LUCENE_VERSION, "Message", Lucene.getPerFieldAnalyzer());
      Query testQuery = target.parse("Date:2007010113");
      
      assertEquals(TermQuery.class, testQuery.getClass());
   }

   private IndexReader buildDummyIndex() throws IOException {
      RAMDirectory indexDirectory = new RAMDirectory();
      
      IndexWriterConfig iwc = new IndexWriterConfig(Lucene.LUCENE_VERSION, Lucene.getPerFieldAnalyzer());
      iwc.setOpenMode(OpenMode.CREATE);
      IndexWriter iw = new IndexWriter(indexDirectory, iwc);
      
      Document doc = new Document();
      doc.add(new StringField("Author", "foo", Field.Store.YES));
      doc.add(new LongField("RevisionNumber", 50L, Field.Store.YES));
      doc.add(new StringField("Revision", "50", Field.Store.YES));
      doc.add(new TextField("Message", "stuff", Field.Store.YES));
      iw.addDocument(doc);
      
      doc = new Document();
      doc.add(new StringField("Author", "bar", Field.Store.YES));
      doc.add(new LongField("RevisionNumber", 5000L, Field.Store.YES));
      doc.add(new StringField("Revision", "5000", Field.Store.YES));
      doc.add(new TextField("Message", "stuff", Field.Store.YES));
      iw.addDocument(doc);
      iw.commit();
      iw.close();
      
      DirectoryReader result = DirectoryReader.open(indexDirectory);
      return result;
   }
}
