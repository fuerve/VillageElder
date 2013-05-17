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
   public Version(String[] aargs) {
      super(aargs);
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
   public int execute() {
      System.out.println("Village Elder version " + VersionInfo.getVersion());
      return 0;
   }
}
