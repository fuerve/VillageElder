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
package com.fuerve.villageelder.sourcecontrol;

/**
 * ChangePath - Represents a changed path entry in a revision.
 * A revision will often affect multiple files, implying a
 * one-to-many relationship between a revision and the paths
 * that took the change.  An object of this type is a single
 * such path within a revision, of which there may be many.
 * @author lparker
 */
public class ChangePath {
   private final String path;
   private final String changeType;
   private final String copyPath;
   private final long copyRevision;
   
   /**
    * Initializes a new instance of ChangePath with
    * a path and a change type.
    * @param ppath The path that was changed.
    * @param cchangeType A string representing the change that was made.  The
    * value of this string may differ between source control implementations.
    */
   public ChangePath(final String ppath, final String cchangeType) {
      this(ppath, cchangeType, null, -1);
   }
   
   /**
    * Initializes a new instance of ChangePath with
    * a path, a change type, a copy path and a copy revision.
    * @param ppath The path that was changed.
    * @param cchangeType A string representing the change that was made.  The
    * value of this string may differ between source control implementations.
    * @param ccopyPath The path to which the changed path was copied.
    * @param ccopyRevision The revision of the copy.
    */
   public ChangePath(
         final String ppath,
         final String cchangeType,
         final String ccopyPath,
         final long ccopyRevision) {
      path = ppath;
      changeType = cchangeType;
      copyPath = ccopyPath;
      copyRevision = ccopyRevision;
   }
   
   /**
    * Determines whether the change operation for this path included a copy
    * operation.
    * @return True if this changed path was a copy.  False otherwise.
    */
   public boolean isCopy() {
      return copyPath != null;
   }
   
   /**
    * Gets the path that was changed.
    * @return The path that was changed.
    */
   public String getPath() {
      return path;
   }
   
   /**
    * Gets the string that represents the type of change made to this path.
    * @return The string that represents the type of change made to this path.
    */
   public String getChangeType() {
      return changeType;
   }
   
   /**
    * Gets the target of the copy operation performed on this path.  If this
    * changed path was not copied, returns null.
    * @return The target of the copy operation performed on this path.
    */
   public String getCopyPath() {
      return copyPath;
   }
   
   /**
    * Gets the copy revision.
    * @return The copy revision number.
    */
   public long getCopyRevision() {
      return copyRevision;
   }
}
