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

import org.apache.commons.cli.CommandLine;

import com.fuerve.villageelder.configuration.IndexProperties;

/**
 * The 'index' command.
 * @author lparker
 *
 */
public class Index extends Command {
   private String indexDir = "";
   private String taxonomyDir = "";
   private String repository = "";
   
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
      // TODO: Wire up the properties file here.
      
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
      
      return 0;
   }
   
   private boolean parseArgs(final String[] args) {
      CommandLine commandLine = parseCommandLine(args);
      if (args.length == 0 || commandLine.hasOption("?")) {
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
   
   private void printExecutionOptions() {
      StringBuilder sb = new StringBuilder();
      
      sb.append("Index directory: " + indexDir + "\n");
      sb.append("Taxonomy directory: " + taxonomyDir + "\n");
      sb.append("Repository path: " + repository + "\n");
      sb.append("Start revision: " + startRevision + "\n");
      sb.append("End revision: " + endRevision);
      
      System.out.println(sb.toString());
   }
}
