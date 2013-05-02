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

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.fuerve.villageelder.common.Lucene;

/**
 * This class contains the logic necessary to initialize and
 * de-initialize the indexing interaction components of a
 * Lucene index.
 * Note: This logic could easily have been in {@link Indexer},
 * but there was enough code that I decided to break it out
 * into its own class so as not to muddy that one.
 * @author lparker
 *
 */
class IndexManager {
   private Directory indexDirectory;
   private Directory taxonomyDirectory;
   private String indexDirectoryName;
   private String taxonomyDirectoryName;
   private boolean stringDirectories;
   private OpenMode openMode;
   private Analyzer analyzer;
   private Version luceneVersion;
   
   private IndexWriter indexWriter;
   private TaxonomyWriter taxonomyWriter;
   
   private boolean initialized;
   
   /**
    * Initializes a new instance of IndexManager with a preconstructed Directory
    * for both the regular index and the taxonomy index.  If an index exists
    * in these locations, it will be opened in append mode.  Otherwise, it
    * will be created.
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    */
   public IndexManager(final Directory iindexDirectory, final Directory ttaxonomyDirectory) {
      this(iindexDirectory, ttaxonomyDirectory, OpenMode.CREATE_OR_APPEND);
   }
   
   /**
    * Initializes a new instance of IndexManager with a preconstructed Directory
    * for both the regular index and the taxonomy index.  The open mode of
    * the Lucene index must be specified in this constructor.  Use this
    * with caution!
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    * @param oopenMode The open mode to use when opening the index.  Use with caution.
    */
   public IndexManager(final Directory iindexDirectory, final Directory ttaxonomyDirectory, final OpenMode oopenMode) {
      indexDirectory = iindexDirectory;
      taxonomyDirectory = ttaxonomyDirectory;
      openMode = oopenMode;
   }
   
   /**
    * Initializes a new instance of IndexManager with a pair of string pathnames
    * for both the regular index and the taxonomy index.  If an index exists
    * in these locations, it will be opened in append mode.  Otherwise, it
    * will be created.
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    */
   public IndexManager(
         final String iindexDirectory,
         final String ttaxonomyDirectory) {
      this(iindexDirectory, ttaxonomyDirectory, OpenMode.CREATE_OR_APPEND);
   }
   
   /**
    * Initializes a new instance of IndexManager with a pair of string pathnames
    * for both the regular index and the taxonomy index.  The open mode of
    * the Lucene index must be specified in this constructor.  Use this
    * with caution!
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    * @param oopenMode The open mode to use when opening the index.  Use with caution.
    */
   public IndexManager(
         final String iindexDirectory,
         final String ttaxonomyDirectory,
         final OpenMode oopenMode) {
      if (iindexDirectory == null || ttaxonomyDirectory == null) {
         throw new IllegalArgumentException("The index and/or taxonomy directories were unspecified");
      }
      
      stringDirectories = true;
      indexDirectoryName = iindexDirectory;
      taxonomyDirectoryName = ttaxonomyDirectory;
      openMode = oopenMode;
   }
   
   /**
    * Performs the initialization of the index by opening the Directories
    * for the regular and taxonomy indices and instantiating the index
    * writers.
    * @throws IOException A fatal exception occurred when trying to open
    * the index directories or construct the index writers.
    */
   public void initializeIndex() throws IOException {
      // Don't try to re-initialize an already initialized index.
      if (initialized == true) {
         return;
      }
      initializeDirectories();
      initializeAttributes();
      initializeWriters();
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
    * Obtains some contextual attributes from a common source of truth.
    * This is broken out into a separate method because I'm guessing it
    * will fill up someday, and I'd hate for it to just grow unchecked
    * inside the body of another method and burst out of its chest.
    */
   private void initializeAttributes() {
      luceneVersion = Lucene.LUCENE_VERSION;
      analyzer = Lucene.getPerFieldAnalyzer();
   }
   
   /**
    * Gets the writers for the regular and taxonomy indices ready to go.
    * @throws IOException A fatal exception occurred while trying to
    * construct the index writers.
    */
   private void initializeWriters() throws IOException {
      if (luceneVersion == null || analyzer == null) {
         throw new IllegalArgumentException(
               "The Lucene version and the index analyzer were unspecified " +
               "when attempting to create the index writers"
         );
      }
      IndexWriterConfig iwc = new IndexWriterConfig(luceneVersion, analyzer);
      iwc.setOpenMode(openMode);
      
      indexWriter = new IndexWriter(indexDirectory, iwc);
      taxonomyWriter = new DirectoryTaxonomyWriter(taxonomyDirectory, openMode);
   }
   
   /**
    * De-initializes the index writer resources.  After this method is
    * called, this instance may be initialized again if necessary, but
    * it cannot be reconfigured to use a different index.
    * @throws IOException A fatal exception occurred while trying to
    * close the index writers.
    */
   public void dispose() throws IOException {
      if (initialized == true) {
         closeWriters();
         initialized = false;
      } else {
         throw new IllegalArgumentException(
               "Tried to dispose uninitialized index resources"
         );
      }
   }
   
   /**
    * Closes the index writers, committing all pending changes.
    * @throws IOException A fatal exception occurred while trying to
    * close the index writers.
    */
   private void closeWriters() throws IOException {
      taxonomyWriter.close();
      indexWriter.close();
      
      taxonomyWriter = null;
      indexWriter = null;
   }

   
   /**
    * Gets the index writer.
    * @return The index writer, which may be null if the index has
    * not yet been initialized.
    */
   public IndexWriter getIndexWriter() {
      return indexWriter;
   }
   
   /**
    * Gets the taxonomy writer.
    * @return The taxonomy writer, which may be null if the
    * taxonomy index has not been initialized.
    */
   public TaxonomyWriter getTaxonomyWriter() {
      return taxonomyWriter;
   }
   
   /**
    * Gets the analyzer in use by this index.
    * @return The analyzer in use by this index.
    */
   public Analyzer getAnalyzer() {
      return analyzer;
   }
   
   /**
    * Gets whether this index manager has been initialized.
    */
   public boolean isInitialized() {
      return initialized;
   }
}
