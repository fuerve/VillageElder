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

import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fuerve.villageelder.configuration.PropertyContainer;

/**
 * This abstract class is the basis for command line commands.
 * @author lparker
 *
 */
public abstract class Command {
   private Options options;
   private PropertyContainer propertyContainer;
   
   /**
    * Initializes a new instance of Command with
    * a set of arguments.
    */
   public Command() {
      options = new Options();
   }
   
   /**
    * This method may be called by subclasses to aggregate
    * a set of options for a command.
    * @param name The name of the option as it should be
    * specified on the command line.
    * @param hasArg Whether the option has an argument.
    * @param description The long form description of the
    * option.
    */
   protected void addOption(
         final String name,
         final boolean hasArg,
         final String description) {
      if (options == null) {
         options = new Options();
      }
      options.addOption(name, hasArg, description);
   }
   
   /**
    * This method may be called by subclasses to aggregate
    * a set of options for a command.
    * @param opt The short name of the option.
    * @param longOpt The long name of the option.
    * @param hasArg Whether the option has an argument.
    * @param description The long form description of the
    * option.
    */
   protected void addOption(
         final String opt,
         final String longOpt,
         final boolean hasArg,
         final String description) {
      options.addOption(opt, longOpt, hasArg, description);
   }
   
   /**
    * This method parses the command line and returns the results
    * in a {@link CommandLine} object.
    * @param args The arguments to parse.
    * @return The {@link CommandLine} object containing the set
    * command line arguments.
    */
   protected CommandLine parseCommandLine(final String[] args) {
      final CommandLineParser parser = new GnuParser();
      CommandLine result;
      
      try {
         result = parser.parse(options, args);
      } catch (ParseException e) {
         printUsage(new PrintWriter(System.out));
         throw new IllegalArgumentException(e);
      }
      
      return result;
   }
   
   /**
    * Triggers the loading of the properties file, which
    * becomes accessible to subclasses via the
    * {@link Command#getProperties()} method.
    */
   protected void loadProperties() {
      propertyContainer = PropertyContainer.getInstance();
   }
   
   /**
    * Gets the Village Elder property container.
    * @return The property container.
    */
   protected PropertyContainer getProperties() {
      if (propertyContainer == null) {
         loadProperties();
      }
      
      return propertyContainer;
   }
   
   /**
    * This method shall be implemented by subclasses to return a
    * simple string containing the name of the command, which is
    * used throughout this framework.
    * @return The string name of this command.
    */
   protected abstract String getCommandName();
   
   /**
    * This method is the entry point to a command,
    * fully containing the execution path.
    * @return A return code.
    */
   public abstract int execute(final String[] args);
   
   /**
    * Prints a usage message to a given output {@link Writer}
    * Stolen shamelessly from http://marxsoftware.blogspot.com/2008/11/command-line-parsing-with-apache.html
    * 
    * @param writer The {@link Writer} to which the usage information
    * shall be printed.
    */
   public void printUsage(final Writer writer) {
      final PrintWriter printWriter = new PrintWriter(writer);
      final HelpFormatter formatter = new HelpFormatter();
      formatter.printUsage(printWriter, 80, getCommandName(), options);
      printWriter.close();
   }
   
   /**
    * Prints a help message to a given output {@link Writer}.
    * Stolen shamelessly from http://marxsoftware.blogspot.com/2008/11/command-line-parsing-with-apache.html
    * 
    * @param printedRowWidth The width of the printed row.
    * @param header The header to print before the help message.
    * @param footer The footer to print after the help message.
    * @param spacesBeforeOption The number of spaces to insert before
    * printing each option.
    * @param spacesBeforeOptionDescription The number of spaces to
    * insert before printing each option's description.
    * @param displayUsage Whether to display basic usage information.
    * @param writer The writer to which the help message shall be
    * written.
    */
   public void printHelp(final boolean displayUsage) {
      final String commandLineSyntax = "VillageElder.sh " + getCommandName();
      final HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(commandLineSyntax, options);
   }
}
