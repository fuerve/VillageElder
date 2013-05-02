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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.facet.index.FacetFields;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import com.fuerve.villageelder.sourcecontrol.ChangePath;
import com.fuerve.villageelder.sourcecontrol.RevisionInfo;

/**
 * Manages Lucene indexes and performs indexing operations.
 * @author lparker
 *
 */
public class Indexer {
   private IndexManager indexManager;
   
   /**
    * Initializes a new instance of Indexer with a preconstructed Directory
    * for both the regular index and the taxonomy index.  If an index exists
    * in these locations, it will be opened in append mode.  Otherwise, it
    * will be created.
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    */
   public Indexer(final Directory iindexDirectory, final Directory ttaxonomyDirectory) {
      this(iindexDirectory, ttaxonomyDirectory, OpenMode.CREATE_OR_APPEND);
   }
   
   /**
    * Initializes a new instance of Indexer with a preconstructed Directory
    * for both the regular index and the taxonomy index.  The open mode of
    * the Lucene index must be specified in this constructor.  Use this
    * with caution!
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    * @param oopenMode The open mode to use when opening the index.  Use with caution.
    */
   public Indexer(
         final Directory iindexDirectory,
         final Directory ttaxonomyDirectory,
         final OpenMode oopenMode) {
      indexManager = new IndexManager(iindexDirectory, ttaxonomyDirectory, oopenMode);
   }
   
   /**
    * Initializes a new instance of Indexer with a pair of string pathnames
    * for both the regular index and the taxonomy index.  If an index exists
    * in these locations, it will be opened in append mode.  Otherwise, it
    * will be created.
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    */
   public Indexer(
         final String iindexDirectory,
         final String ttaxonomyDirectory) {
      this(iindexDirectory, ttaxonomyDirectory, OpenMode.CREATE_OR_APPEND);
   }
   
   /**
    * Initializes a new instance of Indexer with a pair of string pathnames
    * for both the regular index and the taxonomy index.  The open mode of
    * the Lucene index must be specified in this constructor.  Use this
    * with caution!
    * @param iindexDirectory The Directory in which Lucene builds its index.
    * @param ttaxonomyDirectory The Directory in which Lucene builds its
    * taxonomy index.
    * @param oopenMode The open mode to use when opening the index.  Use with caution.
    */
   public Indexer(
         final String iindexDirectory,
         final String ttaxonomyDirectory,
         final OpenMode oopenMode) {
      indexManager = new IndexManager(iindexDirectory, ttaxonomyDirectory, oopenMode);
   }
   
   /**
    * Performs the initialization of the index by opening the Directories
    * for the regular and taxonomy indices and instantiating the index
    * writers.
    * @throws IOException A fatal exception occurred when trying to open
    * the index directories or construct the index writers.
    */
   public void initializeIndex() throws IOException {
      if (indexManager == null) {
         throw new IllegalArgumentException(
               "No index manager has been constructed for this index"
         );
      } else {
         indexManager.initializeIndex();
      }
   }
   
   /**
    * De-initializes the index writer resources.  After this method is
    * called, this instance may be initialized again if necessary, but
    * it cannot be reconfigured to use a different index.
    * @throws IOException A fatal exception occurred while trying to
    * close the index writers.
    */
   public void dispose() throws IOException {
      indexManager.dispose();
   }
   
   /**
    * Indexes a single revision entry, including facet information.
    * @param revision The revision entry to index.
    * @throws IOException A fatal exception occurred while trying to
    * index the revision.
    */
   public void indexRevision(final RevisionInfo revision) throws IOException {
      if (indexManager.isInitialized()) {
         Document doc = buildLuceneDocument(revision);
         buildLuceneFacets(doc, revision);
         indexManager.getIndexWriter().addDocument(doc);
      } else {
         throw new IllegalArgumentException(
               "Tried to index a document with an uninitialized Indexer"
         );
      }
   }
   
   /**
    * Builds an indexable Lucene document from a revision entry.
    * @param revision The revision entry from which to build the Lucene
    * document.
    * @return The Lucene document populated by the values in the revision
    * entry.
    */
   private Document buildLuceneDocument(final RevisionInfo revision) {
      Document doc = new Document();

      doc.add(
            new LongField(
                  "RevisionNumber",
                  revision.getRevision(),
                  Field.Store.YES
            )
      );
      
      doc.add(
            new StringField(
                  "Revision",
                  Long.toString(revision.getRevision()),
                  Field.Store.YES
            )
      );
      
      doc.add(
            new StringField(
                  "Author",
                  revision.getAuthor() == null ? "" : revision.getAuthor(),
                  Field.Store.YES
            )
      );
      
      doc.add(
            new LongField(
                  "Date",
                  revision.getDate().getTime(),
                  Field.Store.YES
            )
      );
      
      doc.add(
            new TextField(
                  "Message",
                  revision.getMessage() == null ? "" : revision.getMessage(),
                  Field.Store.YES
            )
      );
      
      if (revision.getChangePaths().size() > 0) {
         List<ChangePath> changedPaths = revision.getChangePaths();
         
         for (ChangePath entryPath : changedPaths) {
            doc.add(
                  new StringField(
                        "Path",
                        entryPath.getPath(),
                        Field.Store.YES
                  )
            );
            
            doc.add(
                  new StringField(
                        "Change",
                        entryPath.getChangeType(),
                        Field.Store.YES
                  )
            );
            
            if (entryPath.getCopyPath() != null) {
               doc.add(
                     new StringField(
                           "CopyPath",
                           entryPath.getCopyPath(),
                           Field.Store.YES
                     )
               );
               
               doc.add(
                     new LongField(
                           "CopyRevisionNumber",
                           entryPath.getCopyRevision(),
                           Field.Store.YES
                     )
               );
               
               doc.add(
                     new StringField(
                           "CopyRevision",
                           Long.toString(entryPath.getCopyRevision()),
                           Field.Store.YES
                     )
               );
            }
         }
      }
      
      return doc;
   }
   
   /**
    * Builds up and indexes a set of facet information for a revision
    * entry.
    * @param doc The Lucene doc for which facet information is being
    * derived.
    * @param revision The revision entry being indexed.
    * @throws IOException A fatal exception occurred while trying to
    * interact with the taxonomy index.
    */
   private void buildLuceneFacets(
         final Document doc,
         final RevisionInfo revision) throws IOException {
      List<CategoryPath> categories = new ArrayList<CategoryPath>();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd/HH");
      
      // Author facet.
      if (revision.getAuthor() != null &&
            revision.getAuthor().isEmpty() == false) {
         categories.add(new CategoryPath("Author", revision.getAuthor()));
      }
      
      // Date facet.
      if (revision.getDate() != null) {
         Date entryDate = revision.getDate();
         categories.add(
               new CategoryPath("Date/" + dateFormat.format(entryDate), '/')
         );
      }

      if (categories.isEmpty() == false) {
         FacetFields facetFields =
               new FacetFields(indexManager.getTaxonomyWriter());
         facetFields.addFields(doc, categories);
      }
   }
   
   /**
    * Indexes a collection of revisions, including facet information.
    * @param revisions The collection of revisions to index.
    * @throws IOException A fatal exception occurred while interacting
    * with the regular or taxonomy index.
    */
   public void indexRevisions(final Iterable<RevisionInfo> revisions)
         throws IOException {
      if (revisions == null) {
         throw new IllegalArgumentException(
               "Cannot index a null revision"
         );
      } else {
         for (RevisionInfo revision : revisions) {
            indexRevision(revision);
         }
      }
   }
}
