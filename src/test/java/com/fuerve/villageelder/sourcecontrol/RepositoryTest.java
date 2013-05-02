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

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the Repository class.
 * @author lparker
 *
 */
public class RepositoryTest {
   private static final long DUMB_REVISION_NUMBER = 0L;
   private static final String DUMB_REVISION_AUTHOR = "testauthor";
   private static final Date DUMB_REVISION_DATE = new Date();
   private static final String DUMB_REVISION_MESSAGE = "testmessage";

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.Repository#Repository(java.lang.String)}.
    */
   @Test
   public void testRepositoryString() throws Exception {
      final String pathExpected = "testpath";
      
      final Field pathField = Repository.class.getDeclaredField("path");
      
      pathField.setAccessible(true);
      
      Repository target = new VeryBasicRepository(pathExpected);
      
      final String pathActual = (String) pathField.get(target);
      
      assertEquals(pathExpected, pathActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.Repository#Repository(java.lang.String, java.lang.String, java.lang.String)}.
    */
   @Test
   public void testRepositoryStringStringString() throws Exception {
      final String pathExpected = "testpath";
      final String usernameExpected = "testuser";
      final String passwordExpected = "testpassword";
      
      final Field pathField = Repository.class.getDeclaredField("path");
      final Field usernameField = Repository.class.getDeclaredField("username");
      final Field passwordField = Repository.class.getDeclaredField("password");
      
      pathField.setAccessible(true);
      usernameField.setAccessible(true);
      passwordField.setAccessible(true);
      
      Repository target =
            new VeryBasicRepository(
                  pathExpected,
                  usernameExpected,
                  passwordExpected);
      
      final String pathActual = (String) pathField.get(target);
      final String usernameActual = (String) usernameField.get(target);
      final String passwordActual = (String) passwordField.get(target);
      
      assertEquals(pathExpected, pathActual);
      assertEquals(usernameExpected, usernameActual);
      assertEquals(passwordExpected, passwordActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.Repository#getPath()}.
    */
   @Test
   public void testGetPath() {
      final String pathExpected = "testpath";
      
      Repository target = new VeryBasicRepository(pathExpected);
      
      assertEquals(pathExpected, target.getPath());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.Repository#getUsername()}.
    */
   @Test
   public void testGetUsername() {
      final String pathExpected = "testpath";
      final String usernameExpected = "testuser";
      final String passwordExpected = "testpassword";
      
      Repository target =
            new VeryBasicRepository(
                  pathExpected,
                  usernameExpected,
                  passwordExpected);
      
      assertEquals(usernameExpected, target.getUsername());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.Repository#getPassword()}.
    */
   @Test
   public void testGetPassword() {
      final String pathExpected = "testpath";
      final String usernameExpected = "testuser";
      final String passwordExpected = "testpassword";
      
      Repository target =
            new VeryBasicRepository(
                  pathExpected,
                  usernameExpected,
                  passwordExpected);
      
      assertEquals(passwordExpected, target.getPassword());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.Repository#getRevision(long)}. 
    */
   @Test
   public void testGetRevision() throws Exception {
      final String pathExpected = "testpath";
      
      Repository target = new VeryBasicRepository(pathExpected);
      
      RevisionInfo actual = target.getRevision(DUMB_REVISION_NUMBER);
      assertEquals(DUMB_REVISION_NUMBER, actual.getRevision());
      assertEquals(DUMB_REVISION_AUTHOR, actual.getAuthor());
      assertEquals(DUMB_REVISION_DATE, actual.getDate());
      assertEquals(DUMB_REVISION_MESSAGE, actual.getMessage());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.Repository#getRevisionRange(long, long)}.
    */
   @Test
   public void testGetRevisionRange() throws Exception {
      final String pathExpected = "testpath";
      
      Repository target = new VeryBasicRepository(pathExpected);
      
      RevisionInfo actual = target.getRevisionRange(DUMB_REVISION_NUMBER, DUMB_REVISION_NUMBER).get(0);
      assertEquals(DUMB_REVISION_NUMBER, actual.getRevision());
      assertEquals(DUMB_REVISION_AUTHOR, actual.getAuthor());
      assertEquals(DUMB_REVISION_DATE, actual.getDate());
      assertEquals(DUMB_REVISION_MESSAGE, actual.getMessage());
   }

   private class VeryBasicRepository extends Repository {

      public VeryBasicRepository(String path) {
         super(path);
      }
      
      public VeryBasicRepository(String path, String username, String password) {
         super(path, username, password);
      }

      @Override
      public RevisionInfo getRevision(long revision) throws IOException {
         return buildDumbRevisionInfo();
      }

      @Override
      public List<RevisionInfo> getRevisionRange(long begin, long end) throws IOException {
         List<RevisionInfo> result = new ArrayList<RevisionInfo>();
         result.add(buildDumbRevisionInfo());
         
         return result;
      }
      
      private RevisionInfo buildDumbRevisionInfo() {
         return new RevisionInfo(DUMB_REVISION_NUMBER, DUMB_REVISION_AUTHOR, DUMB_REVISION_DATE, DUMB_REVISION_MESSAGE);
      }
   }
}
