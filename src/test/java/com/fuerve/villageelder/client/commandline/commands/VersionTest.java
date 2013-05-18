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

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.fuerve.villageelder.common.VersionInfo;

/**
 * Unit tests for the Version command class.
 * @author lparker
 *
 */
public class VersionTest {

   /**
    * Test method for {@link com.fuerve.villageelder.client.commandline.commands.Version#getCommandName()}.
    */
   @Test
   public final void testGetCommandName() {
      assertEquals("version", new Version().getCommandName());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.client.commandline.commands.Version#execute(java.lang.String[])}.
    */
   @Test
   public final void testExecuteNoArgs() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getMajorRevision() + "." +
            VersionInfo.getMinorRevision() + "." +
            VersionInfo.getUpdateRevision() + "." +
            VersionInfo.getHotfixRevision() + "." +
            VersionInfo.getBuildRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteShort() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-s"});
      
      String result = outputCapture.toString().trim();
      String expected = 
            VersionInfo.getMajorRevision() + "." +
            VersionInfo.getMinorRevision() + "." +
            VersionInfo.getUpdateRevision() + "." +
            VersionInfo.getHotfixRevision() + "." +
            VersionInfo.getBuildRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongMajor() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-M"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getMajorRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongMinor() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-m"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getMinorRevision();
      
      assertEquals(expected, result);
   }

   @Test
   public final void testExecuteLongUpdate() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-u"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getUpdateRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongHotfix() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-h"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getHotfixRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongBuild() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-b"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getBuildRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongMajorMinor() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-M", "-m"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getMajorRevision() + "." +
            VersionInfo.getMinorRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongMajorUpdate() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-M", "-u"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getMajorRevision() + "." +
            VersionInfo.getUpdateRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongMajorHotfix() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-M", "-h"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getMajorRevision() + "." +
            VersionInfo.getHotfixRevision();
      
      assertEquals(expected, result);
   }
   
   @Test
   public final void testExecuteLongMajorBuild() {
      ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputCapture));
      
      Command target = new Version();
      target.execute(new String[] {"-M", "-b"});
      
      String result = outputCapture.toString().trim();
      String expected = "Village Elder version " +
            VersionInfo.getMajorRevision() + "." +
            VersionInfo.getBuildRevision();
      
      assertEquals(expected, result);
   }
}