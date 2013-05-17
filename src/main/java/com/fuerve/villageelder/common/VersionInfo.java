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
package com.fuerve.villageelder.common;

/**
 * This class contains information about this version
 * of Village Elder.
 * @author lparker
 *
 */
public final class VersionInfo {
   private static final int REVISION_MAJOR = 0;
   private static final int REVISION_MINOR = 0;
   private static final int REVISION_UPDATE = 0;
   private static final int REVISION_HOTFIX = 0;
   private static final int REVISION_BUILD = 0;
   
   private static final String VERSION =
         String.format("%d.%d.%d.%d.%d",
               REVISION_MAJOR,
               REVISION_MINOR,
               REVISION_UPDATE,
               REVISION_HOTFIX,
               REVISION_BUILD);
   /**
    * Hidden constructor.
    */
   private VersionInfo() {
   }
   
   /**
    * This method gets the version string associated with this build
    * of Village Elder.
    * @return The fully formed version string.
    */
   public static String getVersion() {
      return VERSION;
   }
   
}
