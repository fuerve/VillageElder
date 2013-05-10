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

import java.io.File;
import java.io.StringReader;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

/**
 * Unit tests for the CommonProperties class.
 * @author lparker
 *
 */
public class CommonPropertiesTest {

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.CommonProperties#CommonProperties(java.io.Reader)}.
    */
   @Test
   public final void testCommonProperties() throws Exception {
      final String indexDirectoryExpected = "foo";
      final String taxonomyDirectoryExpected = "bar/baz";
      final Directory indexDirectory = FSDirectory.open(new File(indexDirectoryExpected));
      final Directory taxonomyDirectory = FSDirectory.open(new File(taxonomyDirectoryExpected));
      final String testProps =
            String.format(
                  "Common.IndexDirectory = %s\nCommon.TaxonomyDirectory= %s\n",
                  indexDirectoryExpected,
                  taxonomyDirectoryExpected
            );
      PropertyHandler target = new CommonProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(indexDirectory.getClass(), target.get("Common.IndexDirectory").getValue().getClass());
      assertEquals(taxonomyDirectory.getClass(), target.get("Common.TaxonomyDirectory").getValue().getClass());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.CommonProperties#getIndexDirectory()}.
    */
   @Test
   public final void testGetIndexDirectory() throws Exception {
      final String indexDirectoryExpected = "foo";
      final String taxonomyDirectoryExpected = "bar/baz";
      final String testProps =
            String.format(
                  "Common.IndexDirectory = %s\nCommon.TaxonomyDirectory= %s\n",
                  indexDirectoryExpected,
                  taxonomyDirectoryExpected
            );
      CommonProperties target = new CommonProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(true, target.getIndexDirectory().toString().contains(indexDirectoryExpected));
   }
   
   /**
    * Test method for {@link com.fuerve.villageelder.configuration.CommonProperties#getIndexDirectory()}.
    */
   @Test
   public final void testGetIndexDirectoryNull() throws Exception {
      final String indexDirectoryExpected = "";
      final String taxonomyDirectoryExpected = "bar/baz";
      final String testProps =
            String.format(
                  "Common.IndexDirectory = %s\nCommon.TaxonomyDirectory= %s\n",
                  indexDirectoryExpected,
                  taxonomyDirectoryExpected
            );
      CommonProperties target = new CommonProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(null, target.getIndexDirectory());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.configuration.CommonProperties#getTaxonomyDirectory()}.
    */
   @Test
   public final void testGetTaxonomyDirectory() throws Exception {
      final String indexDirectoryExpected = "foo";
      final String taxonomyDirectoryExpected = "bar/baz";
      final String testProps =
            String.format(
                  "Common.IndexDirectory = %s\nCommon.TaxonomyDirectory= %s\n",
                  indexDirectoryExpected,
                  taxonomyDirectoryExpected
            );
      CommonProperties target = new CommonProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(true, target.getTaxonomyDirectory().toString().contains(taxonomyDirectoryExpected));
   }


   /**
    * Test method for {@link com.fuerve.villageelder.configuration.CommonProperties#getTaxonomyDirectory()}.
    */
   @Test
   public final void testGetTaxonomyDirectoryNull() throws Exception {
      final String indexDirectoryExpected = "foo";
      final String taxonomyDirectoryExpected = "";
      final String testProps =
            String.format(
                  "Common.IndexDirectory = %s\nCommon.TaxonomyDirectory= %s\n",
                  indexDirectoryExpected,
                  taxonomyDirectoryExpected
            );
      CommonProperties target = new CommonProperties(new StringReader(testProps));
      target.load();
      
      assertEquals(null, target.getTaxonomyDirectory());
   }
}
