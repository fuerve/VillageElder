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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * SubversionRepository - Concrete implementation of Repository that supports
 * the Subversion source control system via the SVNKit library.
 * @author lparker
 *
 */
public class SubversionRepository extends Repository {
   private static SVNRepository repositoryReference;
   
   /**
    * Initializes a new instance of SubversionRepository with a repository path.
    * For Subversion, this will be a URL using one of the protocols
    * supported by SVNKit (svn://, svn+ssh://, http://).
    * @param path The repository path.
    */
   public SubversionRepository(String path) {
      super(path);
   }
   
   /**
    * Initializes a new instance of SubversionRepository with a repository path,
    * a username and a password.  The repository path for Subversion will
    * be a URL using one of the protocols supported by SVNKit
    * (svn://, svn+ssh://, http://).  The username and password are
    * naked text.
    * @param path The repository path.
    * @param username The repository username.
    * @param password The repository password.
    */
   public SubversionRepository(String path, String username, String password) {
      super(path, username, password);
   }
   
   /**
    * Called internally to initialize the connection to the Subversion
    * repository.
    * @throws IOException Unable to connect to Subversion.
    */
   private void initializeRepository() throws IOException {
      if (repositoryReference == null) {
         try {
            final SVNURL path = SVNURL.parseURIEncoded(getPath());
            final String protocol = path.getProtocol();
            
            if (protocol.startsWith("http")) {
               DAVRepositoryFactory.setup();
            } else {
               SVNRepositoryFactoryImpl.setup();
            }
            
            repositoryReference =
                  SVNRepositoryFactory.create(
                        SVNURL.parseURIEncoded(getPath())
                  );
            
         } catch (SVNException e) {
            throw new IOException("Could not connect to Subversion", e);
         }
         
         final String username = getUsername();
         final String password = getPassword();
         if (username != null && password != null) {
            repositoryReference.setAuthenticationManager(
                  SVNWCUtil.createDefaultAuthenticationManager(
                        username,
                        password)
                  );
         }
      }
   }
   
   /* (non-Javadoc)
    * @see com.fuerve.villageelder.sourcecontrol.Repository#getRevision(long)
    */
   @SuppressWarnings("unchecked")
   @Override
   public RevisionInfo getRevision(long revision) throws IOException {
      initializeRepository();
      
      Collection<SVNLogEntry> logEntries = null;
      try {
      logEntries = repositoryReference.log(
            new String[] { "/" },
            logEntries,
            revision,
            revision,
            true,
            false);
      } catch (SVNException e) {
         throw new IOException("Could not retrieve revision history from " +
               "Subversion", e);
      }
      
      RevisionInfo result = null;
      
      if (logEntries != null && logEntries.isEmpty() == false) {
         for (Iterator<SVNLogEntry> entries = logEntries.iterator(); entries.hasNext(); ) {
            SVNLogEntry entry = entries.next();
            result = buildRevisionInfo(entry);
            break;
         }
      }
      
      return result;
   }

   /* (non-Javadoc)
    * @see com.fuerve.villageelder.sourcecontrol.Repository#getRevisionRange(long, long)
    */
   @SuppressWarnings("unchecked")
   @Override
   public List<RevisionInfo> getRevisionRange(long begin, long end)
         throws IOException {
      initializeRepository();
      
      Collection<SVNLogEntry> logEntries = null;
      try {
      logEntries = repositoryReference.log(
            new String[] { "/" },
            logEntries,
            begin,
            end,
            true,
            false);
      } catch (SVNException e) {
         throw new IOException("Could not retrieve revision history from " +
               "Subversion", e);
      }
      
      List<RevisionInfo> result = new ArrayList<RevisionInfo>();
      
      if (logEntries != null && logEntries.isEmpty() == false) {
         for (Iterator<SVNLogEntry> entries = logEntries.iterator(); entries.hasNext(); ) {
            SVNLogEntry entry = entries.next();
            result.add(buildRevisionInfo(entry));
         }
      }
      
      return result;
   }
   
   /**
    * Builds a ChangePath from an SVNLogEntryPath object.
    * @param svnLogEntryPath The SVNLogEntryPath object from which to
    * build the ChangePath.
    * @return The populated ChangePath object.
    */
   private ChangePath buildChangePath(final SVNLogEntryPath svnLogEntryPath) {
      ChangePath result = null;
      
      final String svnChangePath = svnLogEntryPath.getPath();
      final String svnChangeType =
            Character.toString(svnLogEntryPath.getType());
      
      if (svnLogEntryPath.getCopyPath() != null) {
         final String svnCopyPath = svnLogEntryPath.getCopyPath();
         final long svnCopyRevision =
               svnLogEntryPath.getCopyRevision();
         result =
               new ChangePath(
                     svnChangePath,
                     svnChangeType,
                     svnCopyPath,
                     svnCopyRevision
               );
      } else {
         result = new ChangePath(svnChangePath, svnChangeType);
      }

      return result;
   }
   
   /**
    * Builds a RevisionInfo object from an SVNLogEntry.
    * @param entry The SVNLogEntry from which to build the
    * RevisionInfo.
    * @return The populated RevisionInfo object.
    */
   private RevisionInfo buildRevisionInfo(final SVNLogEntry entry) {
      final long svnRevision = entry.getRevision();
      final String svnAuthor = entry.getAuthor();
      final Date svnDate = entry.getDate();
      final String svnMessage = entry.getMessage();
      
      RevisionInfo result =
            new RevisionInfo(svnRevision, svnAuthor, svnDate, svnMessage);
      
      if (entry.getChangedPaths().size() > 0) {
         final Map<String, SVNLogEntryPath> changedPaths =
               entry.getChangedPaths();
         for (Entry<String, SVNLogEntryPath> changedPath :
            changedPaths.entrySet()) {
            
            final SVNLogEntryPath svnLogEntryPath = changedPath.getValue();
            final ChangePath changePath = buildChangePath(svnLogEntryPath);
            if (changePath != null) {
               result.addChangePath(changePath);
            }
         }
      }
      
      return result;
   }
}
