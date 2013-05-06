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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

/**
 * Unit tests for {@link IndexManager}.
 * @author lparker
 *
 */
public class IndexManagerTest {

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.IndexManager#IndexManager(org.apache.lucene.store.Directory, org.apache.lucene.store.Directory)}.
    * @throws Exception 
    */
   @Test
   public final void testIndexManagerDirectoryDirectory() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      Field idField = IndexManager.class.getDeclaredField("indexDirectory");
      Field tdField = IndexManager.class.getDeclaredField("taxonomyDirectory");
      Field iwField = IndexManager.class.getDeclaredField("indexWriter");
      Field twField = IndexManager.class.getDeclaredField("taxonomyWriter");
      Field stField = IndexManager.class.getDeclaredField("stringDirectories");
      Field initField = IndexManager.class.getDeclaredField("initialized");
      
      idField.setAccessible(true);
      tdField.setAccessible(true);
      iwField.setAccessible(true);
      twField.setAccessible(true);
      stField.setAccessible(true);
      initField.setAccessible(true);
      
      IndexManager target = new IndexManager(indexDirectory, taxonomyDirectory);
      
      // TEST 1: A newly constructed IndexManager believes itself
      // to be uninitialized, as indicated by the 'initialized'
      // field.
      boolean initActual = initField.getBoolean(target);
      assertFalse(initActual);
      
      target.initializeIndex();
      
      Directory idActual = (Directory) idField.get(target);
      Directory tdActual = (Directory) tdField.get(target);
      IndexWriter iwActual = (IndexWriter) iwField.get(target);
      TaxonomyWriter twActual = (TaxonomyWriter) twField.get(target);
      boolean stActual = (Boolean) stField.get(target);
      initActual = initField.getBoolean(target);
      
      // TEST 2: The IndexManager's index directory is what was passed in.
      assertEquals(indexDirectory, idActual);
      // TEST 3: The IndexManager's taxonomy directory is what was passed in.
      assertEquals(taxonomyDirectory, tdActual);
      // TEST 4: The IndexWriter's directory is what was passed in.
      assertEquals(indexDirectory, iwActual.getDirectory());
      // TEST 5: The taxonomy index is initialized afresh with no categories
      // in it.
      assertEquals(1, twActual.getSize());
      // TEST 6: An IndexManager constructed with Directories does not
      // believe that it needs to construct new Directories from string
      // pathnames.
      assertEquals(false, stActual);
      // TEST 7: The IndexManager's initialized field is true after it
      // has been initialized.
      assertEquals(true, initActual);
      
      target.dispose();
      
      // TEST 8: The IndexManager's index writer is null after it has
      // been disposed.
      iwActual = (IndexWriter) iwField.get(target);
      assertEquals(null, iwActual);
      
      // TEST 9: The IndexManager's taxonomy writer is null after it
      // has been disposed.
      twActual = (TaxonomyWriter) twField.get(target);
      assertEquals(null, twActual);
      
      // TEST 10: The IndexManager's initialized flag is false after
      // it has been disposed.
      initActual = initField.getBoolean(target);
      assertEquals(false, initActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.IndexManager#IndexManager(org.apache.lucene.store.Directory, org.apache.lucene.store.Directory, org.apache.lucene.index.IndexWriterConfig.OpenMode)}.
    */
   @Test
   public final void testIndexManagerDirectoryDirectoryOpenMode() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      Field idField = IndexManager.class.getDeclaredField("indexDirectory");
      Field tdField = IndexManager.class.getDeclaredField("taxonomyDirectory");
      Field iwField = IndexManager.class.getDeclaredField("indexWriter");
      Field twField = IndexManager.class.getDeclaredField("taxonomyWriter");
      Field stField = IndexManager.class.getDeclaredField("stringDirectories");
      Field initField = IndexManager.class.getDeclaredField("initialized");
      
      idField.setAccessible(true);
      tdField.setAccessible(true);
      iwField.setAccessible(true);
      twField.setAccessible(true);
      stField.setAccessible(true);
      initField.setAccessible(true);
      
      IndexManager target = new IndexManager(indexDirectory, taxonomyDirectory, OpenMode.CREATE);
      target.initializeIndex();
      TaxonomyWriter tw = (TaxonomyWriter) twField.get(target);
      IndexWriter iw = (IndexWriter) iwField.get(target);
      tw.addCategory(new CategoryPath("test/stuff", '/'));
      Document doc = new Document();
      doc.add(new LongField("testfield", 1000L, Store.YES));
      iw.addDocument(doc);
      target.dispose();
      
      // TEST: Initializing an index, disposing it and initializing another
      // index instance on the same Directories results in loading the same
      // index.
      IndexManager target2 = new IndexManager(indexDirectory, taxonomyDirectory, OpenMode.APPEND);
      target2.initializeIndex();
      iw = (IndexWriter) iwField.get(target2);
      tw = (TaxonomyWriter) twField.get(target2);
      assertEquals(1, iw.numDocs());
      assertEquals(3, tw.getSize());
      target2.dispose();
   }

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.IndexManager#getIndexWriter()}.
    */
   @Test
   public final void testGetIndexWriter() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      IndexManager target = new IndexManager(indexDirectory, taxonomyDirectory);
      target.initializeIndex();
      
      Document doc = new Document();
      doc.add(new LongField("testfield", 1000L, Store.YES));
      target.getIndexWriter().addDocument(doc);
      assertEquals(1, target.getIndexWriter().numDocs());
      
      target.dispose();
   }

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.IndexManager#getTaxonomyWriter()}.
    */
   @Test
   public final void testGetTaxonomyWriter() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      IndexManager target = new IndexManager(indexDirectory, taxonomyDirectory);
      target.initializeIndex();
      
      target.getTaxonomyWriter().addCategory(new CategoryPath("test/stuff", '/'));
      assertEquals(3, target.getTaxonomyWriter().getSize());
      
      target.dispose();
   }

   /**
    * Test method for {@link com.fuerve.villageelder.indexing.IndexManager#getAnalyzer()}.
    */
   @Test
   public final void testGetAnalyzer() throws Exception {
      RAMDirectory indexDirectory = new RAMDirectory();
      RAMDirectory taxonomyDirectory = new RAMDirectory();
      
      IndexManager target = new IndexManager(indexDirectory, taxonomyDirectory);
      target.initializeIndex();
      
      assertTrue(target.getAnalyzer() instanceof PerFieldAnalyzerWrapper);
      
      target.dispose();
   }
}
