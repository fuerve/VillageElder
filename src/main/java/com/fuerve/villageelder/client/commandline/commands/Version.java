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

import com.fuerve.villageelder.common.VersionInfo;

/**
 * The 'version' command.
 * @author lparker
 *
 */
public final class Version extends Command {

   /**
    * Initializes a new instance of Version with command line arguments.
    * @param aargs The command line arguments.
    */
   public Version() {
      super();
      addOption("s", "short", false, "Prints just the requested version info with no labeling");
      addOption("M", "major", false, "Include the major revision number");
      addOption("m", "minor", false, "Include the minor revision number");
      addOption("u", "update", false, "Include the update revision number");
      addOption("h", "hotfix", false, "Include the hotfix revision number");
      addOption("b", "build", false, "Include the build revision number");
      addOption("a", "all", false, "(Default) Include the entire revision number");
      addOption("?", "help", false, "Show help for the version command");
   }

   /* (non-Javadoc)
    * @see com.fuerve.villageelder.client.commandline.commands.Command#getCommandName()
    */
   @Override
   protected String getCommandName() {
      return "version";
   }

   /* (non-Javadoc)
    * @see com.fuerve.villageelder.client.commandline.commands.Command#execute()
    */
   @Override
   public int execute(final String[] args) {
      StringBuilder result = new StringBuilder();
      boolean afterNumber = false;
      
      if (args.length == 0) {
         addLabel(result);
         addAll(result);
      } else {
         CommandLine commandLine = parseCommandLine(args);
         if (commandLine.hasOption("?")) {
            printHelp(true);
            return 0;
         }
         if (commandLine.hasOption("a")) {
            addLabel(result);
            addAll(result);
         } else if (commandLine.hasOption("s") && commandLine.getOptions().length == 1) {
            addAll(result);
         } else {
            if (!commandLine.hasOption("s")) {
               addLabel(result);
            }
            
            if (commandLine.hasOption("M")) {
               addMajor(result);
               afterNumber = true;
            }
            
            if (commandLine.hasOption("m")) {
               result = afterNumber ? addDelimiter(result) : addNothing(result);
               addMinor(result);
               afterNumber = true;
            }
            
            if (commandLine.hasOption("u")) {
               result = afterNumber ? addDelimiter(result) : addNothing(result);
               addUpdate(result);
               afterNumber = true;
            }
            
            if (commandLine.hasOption("h")) {
               result = afterNumber ? addDelimiter(result) : addNothing(result);
               addHotfix(result);
               afterNumber = true;
            }
            
            if (commandLine.hasOption("b")) {
               result = afterNumber ? addDelimiter(result) : addNothing(result);
               addBuild(result);
               afterNumber = true;
            }
         }
         
      }
      
      System.out.println(result.toString());
      return 0;
   }
   
   private StringBuilder addLabel(final StringBuilder sb) {
      return sb.append("Village Elder version ");
   }
   
   private StringBuilder addDelimiter(final StringBuilder sb) {
      return sb.append('.');
   }
   
   private StringBuilder addMajor(final StringBuilder sb) {
      return sb.append(VersionInfo.getMajorRevision());
   }
   
   private StringBuilder addMinor(final StringBuilder sb) {
      return sb.append(VersionInfo.getMinorRevision());
   }
   
   private StringBuilder addUpdate(final StringBuilder sb) {
      return sb.append(VersionInfo.getUpdateRevision());
   }
   
   private StringBuilder addHotfix(final StringBuilder sb) {
      return sb.append(VersionInfo.getHotfixRevision());
   }
   
   private StringBuilder addBuild(final StringBuilder sb) {
      return sb.append(VersionInfo.getBuildRevision());
   }
   
   private StringBuilder addAll(final StringBuilder sb) {
      return sb.append(VersionInfo.getVersion());
   }
   
   private StringBuilder addNothing(final StringBuilder sb) {
      return sb;
   }
}
