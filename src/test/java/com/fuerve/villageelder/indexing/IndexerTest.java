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
package com.fuerve.villageelder.indexing;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import com.fuerve.villageelder.sourcecontrol.RevisionInfo;

/**
 * Unit tests for the Indexer class.
 * @author lparker
 *
 */
public class IndexerTest {
   private static final long DUMB_REVISION_NUMBER = 0L;
   private static final String DUMB_REVISION_AUTHOR = "testauthor";
   private static final Date DUMB_REVISION_DATE = new Date();
   private static final String DUMB_REVISION_MESSAGE = "testmessage";

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.Indexer#Indexer(org.apache.lucene.store.Directory, org.apache.lucene.store.Directory)}.
    */
   @Test
   public final void testIndexerDirectoryDirectory() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      Field idField = IndexManager.class.getDeclaredField("indexDirectory");
      Field tdField = IndexManager.class.getDeclaredField("taxonomyDirectory");
      Field iwField = IndexManager.class.getDeclaredField("indexWriter");
      Field twField = IndexManager.class.getDeclaredField("taxonomyWriter");
      Field stField = IndexManager.class.getDeclaredField("stringDirectories");
      Field initField = IndexManager.class.getDeclaredField("initialized");
      Field imField = Indexer.class.getDeclaredField("indexManager");
      
      idField.setAccessible(true);
      tdField.setAccessible(true);
      iwField.setAccessible(true);
      twField.setAccessible(true);
      stField.setAccessible(true);
      initField.setAccessible(true);
      imField.setAccessible(true);
      
      Indexer target = new Indexer(indexDirectory, taxonomyDirectory);
      
      IndexManager testManager = (IndexManager) imField.get(target);
      
      // TEST 1: A newly constructed Indexer believes itself
      // to be uninitialized, as indicated by the 'initialized'
      // field.
      boolean initActual = initField.getBoolean(testManager);
      assertFalse(initActual);
      
      target.initializeIndex();
      
      Directory idActual = (Directory) idField.get(testManager);
      Directory tdActual = (Directory) tdField.get(testManager);
      IndexWriter iwActual = (IndexWriter) iwField.get(testManager);
      TaxonomyWriter twActual = (TaxonomyWriter) twField.get(testManager);
      boolean stActual = (Boolean) stField.get(testManager);
      initActual = initField.getBoolean(testManager);
      
      // TEST 2: The Indexer's index directory is what was passed in.
      assertEquals(indexDirectory, idActual);
      // TEST 3: The Indexer's taxonomy directory is what was passed in.
      assertEquals(taxonomyDirectory, tdActual);
      // TEST 4: The IndexWriter's directory is what was passed in.
      assertEquals(indexDirectory, iwActual.getDirectory());
      // TEST 5: The taxonomy index is initialized afresh with no categories
      // in it.
      assertEquals(1, twActual.getSize());
      // TEST 6: An Indexer constructed with Directories does not
      // believe that it needs to construct new Directories from string
      // pathnames.
      assertEquals(false, stActual);
      // TEST 7: The Indexer's initialized field is true after it
      // has been initialized.
      assertEquals(true, initActual);
      
      target.dispose();
      
      // TEST 8: The Indexer's index writer is null after it has
      // been disposed.
      iwActual = (IndexWriter) iwField.get(testManager);
      assertEquals(null, iwActual);
      
      // TEST 9: The Indexer's taxonomy writer is null after it
      // has been disposed.
      twActual = (TaxonomyWriter) twField.get(testManager);
      assertEquals(null, twActual);
      
      // TEST 10: The Indexer's initialized flag is false after
      // it has been disposed.
      initActual = initField.getBoolean(testManager);
      assertEquals(false, initActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.Indexer#Indexer(org.apache.lucene.store.Directory, org.apache.lucene.store.Directory, org.apache.lucene.index.IndexWriterConfig.OpenMode)}.
    */
   @Test
   public final void testIndexerDirectoryDirectoryOpenMode() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      Field idField = IndexManager.class.getDeclaredField("indexDirectory");
      Field tdField = IndexManager.class.getDeclaredField("taxonomyDirectory");
      Field iwField = IndexManager.class.getDeclaredField("indexWriter");
      Field twField = IndexManager.class.getDeclaredField("taxonomyWriter");
      Field stField = IndexManager.class.getDeclaredField("stringDirectories");
      Field initField = IndexManager.class.getDeclaredField("initialized");
      Field imField = Indexer.class.getDeclaredField("indexManager");
      
      idField.setAccessible(true);
      tdField.setAccessible(true);
      iwField.setAccessible(true);
      twField.setAccessible(true);
      stField.setAccessible(true);
      initField.setAccessible(true);
      imField.setAccessible(true);
      
      Indexer target = new Indexer(indexDirectory, taxonomyDirectory, OpenMode.CREATE);
      target.initializeIndex();
      IndexManager testManager = (IndexManager) imField.get(target);
      
      TaxonomyWriter tw = (TaxonomyWriter) twField.get(testManager);
      IndexWriter iw = (IndexWriter) iwField.get(testManager);
      tw.addCategory(new CategoryPath("test/stuff", '/'));
      Document doc = new Document();
      doc.add(new LongField("testfield", 1000L, Store.YES));
      iw.addDocument(doc);
      target.dispose();
      
      // TEST: Initializing an index, disposing it and initializing another
      // index instance on the same Directories results in loading the same
      // index.
      Indexer target2 = new Indexer(indexDirectory, taxonomyDirectory, OpenMode.APPEND);
      target2.initializeIndex();
      testManager = (IndexManager) imField.get(target2);
      iw = (IndexWriter) iwField.get(testManager);
      tw = (TaxonomyWriter) twField.get(testManager);
      assertEquals(1, iw.numDocs());
      assertEquals(3, tw.getSize());
      target2.dispose();
   }

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.Indexer#indexRevision(com.fuerve.villageelder.sourcecontrol.RevisionInfo)}.
    * @throws Exception 
    */
   @Test
   public final void testIndexRevision() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      Field iwField = IndexManager.class.getDeclaredField("indexWriter");
      Field twField = IndexManager.class.getDeclaredField("taxonomyWriter");
      Field imField = Indexer.class.getDeclaredField("indexManager");
      
      iwField.setAccessible(true);
      twField.setAccessible(true);
      imField.setAccessible(true);
      
      Indexer target = new Indexer(indexDirectory, taxonomyDirectory, OpenMode.CREATE);
      target.initializeIndex();
      IndexManager testManager = (IndexManager) imField.get(target);
      
      target.indexRevision(buildDumbRevisionInfo());
      
      TaxonomyWriter tw = (TaxonomyWriter) twField.get(testManager);
      IndexWriter iw = (IndexWriter) iwField.get(testManager);
      
      assertEquals(1, iw.numDocs());
      assertEquals(8, tw.getSize());
      
      target.dispose();
   }

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.Indexer#indexRevisions(java.lang.Iterable)}.
    */
   @Test
   public final void testIndexRevisions() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      Field iwField = IndexManager.class.getDeclaredField("indexWriter");
      Field twField = IndexManager.class.getDeclaredField("taxonomyWriter");
      Field imField = Indexer.class.getDeclaredField("indexManager");
      
      iwField.setAccessible(true);
      twField.setAccessible(true);
      imField.setAccessible(true);
      
      Indexer target = new Indexer(indexDirectory, taxonomyDirectory, OpenMode.CREATE);
      target.initializeIndex();
      IndexManager testManager = (IndexManager) imField.get(target);
      
      List<RevisionInfo> revisions = new ArrayList<RevisionInfo>();
      revisions.add(buildDumbRevisionInfo());
      revisions.add(buildDumbRevisionInfo());
      target.indexRevisions(revisions);
      
      TaxonomyWriter tw = (TaxonomyWriter) twField.get(testManager);
      IndexWriter iw = (IndexWriter) iwField.get(testManager);
      
      assertEquals(2, iw.numDocs());
      assertEquals(8, tw.getSize());
      
      target.dispose();
   }

   private RevisionInfo buildDumbRevisionInfo() {
      return new RevisionInfo(DUMB_REVISION_NUMBER, DUMB_REVISION_AUTHOR, DUMB_REVISION_DATE, DUMB_REVISION_MESSAGE);
   }
}

