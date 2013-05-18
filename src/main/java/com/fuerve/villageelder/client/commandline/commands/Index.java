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
package com.fuerve.villageelder.client.commandline.commands;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;

import com.fuerve.villageelder.actions.ActionException;
import com.fuerve.villageelder.actions.FetchRevisionsAction;
import com.fuerve.villageelder.actions.IndexAction;
import com.fuerve.villageelder.actions.results.IndexResult;
import com.fuerve.villageelder.configuration.PropertyContainer;
import com.fuerve.villageelder.indexing.Indexer;
import com.fuerve.villageelder.sourcecontrol.Repository;
import com.fuerve.villageelder.sourcecontrol.RepositoryFactory;
import com.fuerve.villageelder.sourcecontrol.RepositoryProviderType;
import com.fuerve.villageelder.sourcecontrol.RevisionInfo;

/**
 * The 'index' command.
 * @author lparker
 *
 */
public class Index extends Command {
   private String indexDir = "";
   private Directory indexDirectory;
   private String taxonomyDir = "";
   private Directory taxonomyDirectory;
   private String repository = "";
   private RepositoryProviderType providerType;
   
   private long startRevision = 0;
   private long endRevision = -1;
   
   private boolean doOperation = true;
   
   /**
    * Initializes a new instance of Index.
    */
   public Index() {
      super();
      addOption("?", "help", false, "Show help for the index command");
      addOption(null, "indexdir", true, "The directory into which to create/update the index");
      addOption(null, "taxonomydir", true, "The directory into which to create/update the taxonomy (facet) index");
      addOption(null, "repository", true, "The repository path from which to gather revision history");
      addOption(null, "repositoryType", true, "The type of repository from which to gather revision history (ie. Subversion)");
      addOption("s", "start", true, "The revision number at which to start indexing (default 0)");
      addOption("e", "end", true, "The revision number at which to end indexing (default -1 for all)");
   }

   /* (non-Javadoc)
    * @see com.fuerve.villageelder.client.commandline.commands.Command#getCommandName()
    */
   @Override
   protected String getCommandName() {
      return "index";
   }

   /* (non-Javadoc)
    * @see com.fuerve.villageelder.client.commandline.commands.Command#execute(java.lang.String[])
    */
   @Override
   public int execute(String[] args) {
      setDefaults();
      
      // Parse the arguments and determine whether
      // we need to do anything, or whether we're giving
      // up early.
      if (parseArgs(args) == false) {
         return 1;
      }
      
      if (doOperation == false) {
         return 0;
      }
      
      // Print the execution options.
      printExecutionOptions();
      
      //TODO: Should we inject a sleep in here to give the user
      // a chance to abort?  Or even a yes/no prompt?
      
      // Grobble the logs and create the index.
      //TODO: That.
      List<RevisionInfo> revisions = fetchRevisionHistory();
      
      if (revisions == null) {
         return 1;
      } else {
         System.out.println(
               String.format(
                     "Revisions: %d", revisions.size()
               )
         );
      }
      
      IndexResult indexResult = indexRevisionHistory(revisions);
      if (indexResult == null) {
         return 1;
      } else {
         System.out.println(
               String.format(
                     "Index size: %d\nTaxonomy size: %d",
                     indexResult.getIndexMaxDoc(),
                     indexResult.getTaxonomySize()
               )
         );
      }

      return 0;
   }
   
   /**
    * Fetches a set of revision logs from the repository.
    * @return The list of revision entries.
    */
   private List<RevisionInfo> fetchRevisionHistory() {
      Repository repo =
            RepositoryFactory.getRepositoryInstance(providerType, repository);
      FetchRevisionsAction fetch =
            new FetchRevisionsAction(repo, startRevision, endRevision);
      List<RevisionInfo> result = null;
      
      try {
         result = fetch.doWork().getValue();
      } catch (ActionException e) {
         System.out.println("ERROR: " + e.getMessage());
         if (e.getCause().getCause() != null) {
            System.out.println("Cause: " + e.getCause().getCause().getMessage());
         }
      }
      
      return result;
   }
   
   /**
    * Creates an index out of a set of revision logs.
    * @param revisions The revision logs to index.
    * @return The indexing result.
    */
   private IndexResult indexRevisionHistory(List<RevisionInfo> revisions) {
      Indexer indexer = new Indexer(indexDirectory, taxonomyDirectory, OpenMode.CREATE);
      try {
         indexer.initializeIndex();
      } catch (IOException e) {
         System.out.println("ERROR: " + e.getMessage());
         return null;
      }
      
      IndexAction indexAction = new IndexAction(indexer, revisions);
      IndexResult result = null;
      
      try {
         result = indexAction.doWork();
         return result;
      } catch (ActionException e) {
         System.out.println("ERROR: " + e.getMessage());
         return null;
      } finally {
         try {
            //TODO: This is an awful lot of gymnastics.
            // Factor more of this down into the IndexAction
            // class.  While you're at it, do the same with
            // FetchRevisionsAction and SearchAction.
            indexer.dispose();
         } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return null;
         }
      }
   }
   
   /**
    * Gathers up properties and sets defaults.
    */
   private void setDefaults() {
      PropertyContainer properties = getProperties();
      if (properties != null) {
         indexDirectory = properties.getCommonProperties().getIndexDirectory();
         taxonomyDirectory = properties.getCommonProperties().getTaxonomyDirectory();
         repository = properties.getSourceControlProperties().getRepositoryPath();
         providerType = properties.getSourceControlProperties().getProviderType();
         
         if (indexDirectory != null) {
            indexDir = extractPathFromDirectory(indexDirectory);
         }
         
         if (taxonomyDirectory != null) {
            taxonomyDir = extractPathFromDirectory(taxonomyDirectory);
         }
      }
   }
   
   /**
    * Given a Lucene {@link Directory}, extracts the pathname
    * from it as a string.
    * @param directory The {@link Directory} from which to extract
    * the pathname.
    * @return The string pathname of the {@link Directory}
    */
   private String extractPathFromDirectory(final Directory directory) {
      String dirStr = directory.toString();
      int strBegin = dirStr.indexOf('@') + 1;
      int strEnd = dirStr.indexOf(" ", strBegin);
      return dirStr.substring(strBegin, strEnd);
   }
   
   /**
    * Parses the command line arguments passed in to this command.
    * @param args The arguments that were passed in.
    * @return True if the arguments were correctly parsed, false otherwise.
    */
   private boolean parseArgs(final String[] args) {
      CommandLine commandLine = parseCommandLine(args);
      if (commandLine.hasOption("?")) {
         printHelp(true);
         doOperation = false;
         return true;
      } else {
         if (commandLine.hasOption("indexdir")) {
            indexDir = commandLine.getOptionValue("indexdir");
         }
         
         if (commandLine.hasOption("taxonomydir")) {
            taxonomyDir = commandLine.getOptionValue("taxonomydir");
         }
         
         if (commandLine.hasOption("repository")) {
            repository = commandLine.getOptionValue("repository");
         }
         
         if (commandLine.hasOption("repositoryType")) {
            providerType =
                  RepositoryProviderType.valueOf(
                        commandLine.getOptionValue("repositoryType")
                  );
         }
         
         if (commandLine.hasOption("s")) {
            try {
               startRevision = Long.parseLong(commandLine.getOptionValue("s"));
            } catch (NumberFormatException e) {
               System.out.println("Invalid start revision");
               printHelp(true);
               doOperation = false;
               return false;
            }
         }
         
         if (commandLine.hasOption("e")) {
            try {
               endRevision = Long.parseLong(commandLine.getOptionValue("e"));
            } catch (NumberFormatException e) {
               System.out.println("Invalid end revision");
               printHelp(true);
               doOperation = false;
               return false;
            }
         }
      }
      
      return true;
   }
   
   /**
    * Prints the execution options for this index run.
    */
   private void printExecutionOptions() {
      StringBuilder sb = new StringBuilder();
      
      sb.append("Index directory: " + indexDir + "\n");
      sb.append("Taxonomy directory: " + taxonomyDir + "\n");
      sb.append("Repository path: " + repository + "\n");
      sb.append("Repository type: " + providerType.toString() + "\n");
      sb.append("Start revision: " + startRevision + "\n");
      sb.append("End revision: " + endRevision);
      
      System.out.println(sb.toString());
   }
}
