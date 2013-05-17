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

import java.io.StringWriter;

import org.junit.Test;

/**
 * Unit tests for the Command class.
 * @author lparker
 *
 */
public class CommandTest {
   /**
    * Test method for {@link com.fuerve.villageelder.client.commandline.commands.Command#getArgs()}.
    */
   @Test
   public final void testGetArgs() {
      Command target = new MockCommand(new String[] { "foo", "bar" });
      target.addOption("foo", false, "this is a foo option");
      
      assertArrayEquals(new String[] { "foo", "bar" }, target.getArgs());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.client.commandline.commands.Command#getCommandName()}.
    */
   @Test
   public final void testGetCommandName() {
      Command target = new MockCommand(new String[] { "foo", "bar" });
      target.addOption("foo", false, "this is a foo option");
      
      assertEquals("mock-command", target.getCommandName());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.client.commandline.commands.Command#execute()}.
    */
   @Test
   public final void testExecute() {
      Command target = new MockCommand(new String[] { "foo", "bar" });
      
      target.addOption("foo", false, "this is a foo option");
      
      assertEquals(0, target.execute());
      assertEquals(true, ((MockCommand)target).executed);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.client.commandline.commands.Command#printUsage(java.io.Writer)}.
    */
   @Test
   public final void testPrintUsage() {
      Command target = new MockCommand(new String[] { "foo", "bar" });
      StringWriter result = new StringWriter();
      
      target.addOption("foo", false, "this is a foo option");

      target.printUsage(result);
      
      assertEquals("usage: mock-command [-foo]", result.toString().trim());
      assertEquals(true, ((MockCommand)target).gotCommandName);
   }

   /**
    * Test method for {@link com.fuerve.villageelder.client.commandline.commands.Command#printHelp(int, java.lang.String, java.lang.String, int, int, boolean, java.io.Writer)}.
    */
   @Test
   public final void testPrintHelp() {
      Command target = new MockCommand(new String[] { "foo", "bar" });
      StringWriter result = new StringWriter();
      String expected = "usage: java -cp VillageElder.jar [-foo]\nheader!\n   -foo     this is a foo option\nfooter!";
      target.addOption("foo", false, "this is a foo option");

      target.printHelp(
            80,
            "header!",
            "footer!",
            3,
            5,
            true,
            result
      );
      
      assertEquals(expected, result.toString().trim());
      assertEquals(false, ((MockCommand)target).gotCommandName);
   }
   
   class MockCommand extends Command {
      public boolean executed = false;
      public boolean gotCommandName = false;
      
      public MockCommand(String[] aargs) {
         super(aargs);
      }

      @Override
      protected String getCommandName() {
         gotCommandName = true;
         return "mock-command";
      }

      @Override
      public int execute() {
         executed = true;
         return 0;
      }
   }
}