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
package com.fuerve.villageelder.client.commandline;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.fuerve.villageelder.client.commandline.commands.Command;
import com.fuerve.villageelder.client.commandline.commands.Version;

/**
 * This class is the entry point for the command line interface
 * to Village Elder.
 * @author lparker
 *
 */
public class Main {
   private static final Map<String, Command> commandMap =
         new HashMap<String, Command>();
   
   /**
    * The entry point into the Village Elder command line utility.
    * @param args Command line arguments.
    */
   public static void main(String[] args) {
      setupCommandMap();
      System.exit(run(args));
   }
   
   /**
    * The entry point into the Village Elder command line utility.
    * @param args Command line arguments.
    */
   public static int run(String[] args) {
      if (args.length < 1) {
         return 1;
      } else {
         final String commandName = args[0];
         final String[] remainingArgs = ArrayUtils.subarray(args, 1, args.length);
         
         if (commandMap.containsKey(commandName)) {
            final Command command = commandMap.get(commandName);
            command.execute(remainingArgs);
         }
         return 0;
      }
   }

   /**
    * Sets up the command mappings.  If you want to add
    * a command to the collection of supported commands,
    * put it in here.
    */
   private static void setupCommandMap() {
      commandMap.put("version", new Version());
   }
}
