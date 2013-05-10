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
package com.fuerve.villageelder.configuration;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

/**
 * Unit tests for the SourceControlProperties class.
 * @author lparker
 *
 */
public class SourceControlPropertiesTest {

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.SourceControlProperties#SourceControlProperties(java.io.Reader)}.
    */
   @Test
   public final void testSourceControlProperties() throws Exception {
      final String pathExpected = "testpath";
      final String usernameExpected = "foo";
      final String passwordExpected = "bar";
      final String testProps =
            String.format(
                  "SourceControl.RepositoryPath = %s\nSourceControl.SimpleAuthentication.Username= %s\nSourceControl.SimpleAuthentication.Password=%s\n",
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      PropertyHandler target = new SourceControlProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(pathExpected, target.get("SourceControl.RepositoryPath").getValue());
      assertEquals(usernameExpected, target.get("SourceControl.SimpleAuthentication.Username").getValue());
      assertEquals(passwordExpected, target.get("SourceControl.SimpleAuthentication.Password").getValue());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.SourceControlProperties#getRepositoryPath()}.
    */
   @Test
   public final void testGetRepositoryPath() throws Exception {
      final String pathExpected = "testpath";
      final String usernameExpected = "foo";
      final String passwordExpected = "bar";
      final String testProps =
            String.format(
                  "SourceControl.RepositoryPath = %s\nSourceControl.SimpleAuthentication.Username= %s\nSourceControl.SimpleAuthentication.Password=%s\n",
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      SourceControlProperties target = new SourceControlProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(pathExpected, target.getRepositoryPath());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.SourceControlProperties#getRepositoryPath()}.
    */
   @Test
   public final void testGetRepositoryPathNull() throws Exception {
      final String pathExpected = "";
      final String usernameExpected = "foo";
      final String passwordExpected = "bar";
      final String testProps =
            String.format(
                  "SourceControl.RepositoryPath = %s\nSourceControl.SimpleAuthentication.Username= %s\nSourceControl.SimpleAuthentication.Password=%s\n",
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      SourceControlProperties target = new SourceControlProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(null, target.getRepositoryPath());
   }
   
   /**
    * Test method for {@link com.fuerve.villageelder.configuration.SourceControlProperties#getSimpleAuthenticationUsername()}.
    */
   @Test
   public final void testGetSimpleAuthenticationUsername() throws Exception {
      final String pathExpected = "testpath";
      final String usernameExpected = "foo";
      final String passwordExpected = "bar";
      final String testProps =
            String.format(
                  "SourceControl.RepositoryPath = %s\nSourceControl.SimpleAuthentication.Username= %s\nSourceControl.SimpleAuthentication.Password=%s\n",
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      SourceControlProperties target = new SourceControlProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(usernameExpected, target.getSimpleAuthenticationUsername());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.SourceControlProperties#getSimpleAuthenticationUsername()}.
    */
   @Test
   public final void testGetSimpleAuthenticationUsernameNull() throws Exception {
      final String pathExpected = "testpath";
      final String usernameExpected = "";
      final String passwordExpected = "bar";
      final String testProps =
            String.format(
                  "SourceControl.RepositoryPath = %s\nSourceControl.SimpleAuthentication.Username= %s\nSourceControl.SimpleAuthentication.Password=%s\n",
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      SourceControlProperties target = new SourceControlProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(null, target.getSimpleAuthenticationUsername());
   }
   
   /**
    * Test method for {@link com.fuerve.villageelder.configuration.SourceControlProperties#getSimpleAuthenticationPassword()}.
    */
   @Test
   public final void testGetSimpleAuthenticationPassword() throws Exception {
      final String pathExpected = "testpath";
      final String usernameExpected = "foo";
      final String passwordExpected = "bar";
      final String testProps =
            String.format(
                  "SourceControl.RepositoryPath = %s\nSourceControl.SimpleAuthentication.Username= %s\nSourceControl.SimpleAuthentication.Password=%s\n",
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      SourceControlProperties target = new SourceControlProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(passwordExpected, target.getSimpleAuthenticationPassword());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.SourceControlProperties#getSimpleAuthenticationPassword()}.
    */
   @Test
   public final void testGetSimpleAuthenticationPasswordNull() throws Exception {
      final String pathExpected = "testpath";
      final String usernameExpected = "foo";
      final String passwordExpected = "";
      final String testProps =
            String.format(
                  "SourceControl.RepositoryPath = %s\nSourceControl.SimpleAuthentication.Username= %s\nSourceControl.SimpleAuthentication.Password=%s\n",
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      SourceControlProperties target = new SourceControlProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(null, target.getSimpleAuthenticationPassword());
   }
}
