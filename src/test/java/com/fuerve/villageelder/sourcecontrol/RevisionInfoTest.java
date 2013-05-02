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

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the RevisionInfo class.
 * @author lparker
 *
 */
public class RevisionInfoTest {

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RevisionInfo#RevisionInfo(long, java.lang.String, java.util.Date, java.lang.String)}.
    */
   @Test
   public void testRevisionInfo() throws Exception {
      Field revisionField = RevisionInfo.class.getDeclaredField("revision");
      Field authorField = RevisionInfo.class.getDeclaredField("author");
      Field dateField = RevisionInfo.class.getDeclaredField("date");
      Field messageField = RevisionInfo.class.getDeclaredField("message");
      Field changePathsField = RevisionInfo.class.getDeclaredField("changePaths");
      
      revisionField.setAccessible(true);
      authorField.setAccessible(true);
      dateField.setAccessible(true);
      messageField.setAccessible(true);
      changePathsField.setAccessible(true);
      
      final long revisionExpected = 1000L;
      final String authorExpected = "testauthor";
      final Date dateExpected = new Date();
      final String messageExpected = "testmessage";
      
      final RevisionInfo target = new RevisionInfo(revisionExpected, authorExpected, dateExpected, messageExpected);
      
      final long revisionActual = revisionField.getLong(target);
      final String authorActual = (String) authorField.get(target);
      final Date dateActual = (Date) dateField.get(target);
      final String messageActual = (String) messageField.get(target);
      @SuppressWarnings("unchecked")
      final List<ChangePath> changePathsActual = (List<ChangePath>) changePathsField.get(target);
      
      assertEquals(revisionExpected, revisionActual);
      assertEquals(authorExpected, authorActual);
      assertEquals(dateExpected, dateActual);
      assertEquals(messageExpected, messageActual);
      assertEquals(0, changePathsActual.size());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RevisionInfo#addChangePath(com.fuerve.villageelder.sourcecontrol.ChangePath)}.
    */
   @Test
   public void testAddChangePath() throws Exception {
      Field changePathsField = RevisionInfo.class.getDeclaredField("changePaths");
      
      changePathsField.setAccessible(true);
      
      final long revisionExpected = 1000L;
      final String authorExpected = "testauthor";
      final Date dateExpected = new Date();
      final String messageExpected = "testmessage";
      final String pathExpected = "testpath";
      final String typeExpected = "testtype";
      
      final RevisionInfo target = new RevisionInfo(revisionExpected, authorExpected, dateExpected, messageExpected);
      target.addChangePath(new ChangePath(pathExpected, typeExpected));
      
      @SuppressWarnings("unchecked")
      final List<ChangePath> changePathsActual = (List<ChangePath>) changePathsField.get(target);
      
      ChangePath testChangePath = changePathsActual.get(0);
      
      assertEquals(pathExpected, testChangePath.getPath());
      assertEquals(typeExpected, testChangePath.getChangeType());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RevisionInfo#getRevision()}.
    */
   @Test
   public void testGetRevision() {
      final long revisionExpected = 1000L;
      final String authorExpected = "testauthor";
      final Date dateExpected = new Date();
      final String messageExpected = "testmessage";
      
      final RevisionInfo target = new RevisionInfo(revisionExpected, authorExpected, dateExpected, messageExpected);
      
      assertEquals(revisionExpected, target.getRevision());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RevisionInfo#getAuthor()}.
    */
   @Test
   public void testGetAuthor() {
      final long revisionExpected = 1000L;
      final String authorExpected = "testauthor";
      final Date dateExpected = new Date();
      final String messageExpected = "testmessage";
      
      final RevisionInfo target = new RevisionInfo(revisionExpected, authorExpected, dateExpected, messageExpected);
      
      assertEquals(authorExpected, target.getAuthor());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RevisionInfo#getDate()}.
    */
   @Test
   public void testGetDate() {
      final long revisionExpected = 1000L;
      final String authorExpected = "testauthor";
      final Date dateExpected = new Date();
      final String messageExpected = "testmessage";
      
      final RevisionInfo target = new RevisionInfo(revisionExpected, authorExpected, dateExpected, messageExpected);
      
      assertEquals(dateExpected, target.getDate());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RevisionInfo#getMessage()}.
    */
   @Test
   public void testGetMessage() {
      final long revisionExpected = 1000L;
      final String authorExpected = "testauthor";
      final Date dateExpected = new Date();
      final String messageExpected = "testmessage";
      
      final RevisionInfo target = new RevisionInfo(revisionExpected, authorExpected, dateExpected, messageExpected);
      
      assertEquals(messageExpected, target.getMessage());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RevisionInfo#getChangePaths()}.
    */
   @Test
   public void testGetChangePaths() {
      final long revisionExpected = 1000L;
      final String authorExpected = "testauthor";
      final Date dateExpected = new Date();
      final String messageExpected = "testmessage";
      final String pathExpected = "testpath";
      final String typeExpected = "testtype";
      
      final RevisionInfo target = new RevisionInfo(revisionExpected, authorExpected, dateExpected, messageExpected);
      target.addChangePath(new ChangePath(pathExpected, typeExpected));
      
      List<ChangePath> changePathsActual = target.getChangePaths();
      
      assertEquals(1, changePathsActual.size());
      assertEquals(pathExpected, changePathsActual.get(0).getPath());
      assertEquals(typeExpected, changePathsActual.get(0).getChangeType());
   }
}
