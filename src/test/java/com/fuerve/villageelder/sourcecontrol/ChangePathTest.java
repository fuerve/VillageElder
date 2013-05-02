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

import org.junit.Test;

/**
 * Unit tests for the ChangePath class.
 * @author lparker
 *
 */
public class ChangePathTest {

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#ChangePath(java.lang.String, java.lang.String)}.
    */
   @Test
   public void testChangePathStringString() throws Exception {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected);
      
      Field pathField = ChangePath.class.getDeclaredField("path");
      Field changeTypeField = ChangePath.class.getDeclaredField("changeType");
      Field copyPathField = ChangePath.class.getDeclaredField("copyPath");
      Field copyRevisionField = ChangePath.class.getDeclaredField("copyRevision");
      
      pathField.setAccessible(true);
      changeTypeField.setAccessible(true);
      copyPathField.setAccessible(true);
      copyRevisionField.setAccessible(true);
      
      String pathActual = (String) pathField.get(target);
      String changeTypeActual = (String) changeTypeField.get(target);
      
      assertEquals(pathExpected, pathActual);
      assertEquals(changeTypeExpected, changeTypeActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#ChangePath(java.lang.String, java.lang.String, java.lang.String, long)}.
    */
   @Test
   public void testChangePathStringStringStringLong() throws Exception {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      final String copyPathExpected = "testcopypath";
      final long copyRevisionExpected = 9999L;
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected, copyPathExpected, copyRevisionExpected);
      
      Field pathField = ChangePath.class.getDeclaredField("path");
      Field changeTypeField = ChangePath.class.getDeclaredField("changeType");
      Field copyPathField = ChangePath.class.getDeclaredField("copyPath");
      Field copyRevisionField = ChangePath.class.getDeclaredField("copyRevision");
      
      pathField.setAccessible(true);
      changeTypeField.setAccessible(true);
      copyPathField.setAccessible(true);
      copyRevisionField.setAccessible(true);
      
      String pathActual = (String) pathField.get(target);
      String changeTypeActual = (String) changeTypeField.get(target);
      String copyPathActual = (String) copyPathField.get(target);
      long copyRevisionActual = (Long) copyRevisionField.get(target);
      
      assertEquals(pathExpected, pathActual);
      assertEquals(changeTypeExpected, changeTypeActual);
      assertEquals(copyPathExpected, copyPathActual);
      assertEquals(copyRevisionExpected, copyRevisionActual);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#isCopy()}.
    */
   @Test
   public void testIsCopyFalse() {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected);
      assertEquals(false, target.isCopy());
   }
   
   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#isCopy()}.
    */
   @Test
   public void testIsCopyTrue() {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      final String copyPathExpected = "testcopypath";
      final long copyRevisionExpected = 9999L;
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected, copyPathExpected, copyRevisionExpected);
      assertEquals(true, target.isCopy());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#getPath()}.
    */
   @Test
   public void testGetPath() {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      final String copyPathExpected = "testcopypath";
      final long copyRevisionExpected = 9999L;
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected, copyPathExpected, copyRevisionExpected);
      assertEquals(pathExpected, target.getPath());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#getChangeType()}.
    */
   @Test
   public void testGetChangeType() {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      final String copyPathExpected = "testcopypath";
      final long copyRevisionExpected = 9999L;
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected, copyPathExpected, copyRevisionExpected);
      assertEquals(changeTypeExpected, target.getChangeType());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#getCopyPath()}.
    */
   @Test
   public void testGetCopyPath() {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      final String copyPathExpected = "testcopypath";
      final long copyRevisionExpected = 9999L;
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected, copyPathExpected, copyRevisionExpected);
      assertEquals(copyPathExpected, target.getCopyPath());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.ChangePath#getCopyRevision()}.
    */
   @Test
   public void testGetCopyRevision() {
      final String pathExpected = "testpath";
      final String changeTypeExpected = "testchangetype";
      final String copyPathExpected = "testcopypath";
      final long copyRevisionExpected = 9999L;
      ChangePath target = new ChangePath(pathExpected, changeTypeExpected, copyPathExpected, copyRevisionExpected);
      assertEquals(copyRevisionExpected, target.getCopyRevision());
   }
}
