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

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * This class contains constants and utility methods
 * common to Lucene indexing and search.
 * @author lparker
 *
 */
public final class Lucene {
   public static final Version LUCENE_VERSION = Version.LUCENE_42;
   public static final String DEFAULT_QUERY_FIELD = "Message";
   private static Analyzer analyzer;

   /**
    * Gets the per-field Lucene analyzer common to indexing and search.
    * @return The constructed instance of the Lucene per-field analyzer.
    */
   public static final Analyzer getPerFieldAnalyzer() {
      if (analyzer == null) {
         Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
         analyzerPerField.put("Author", new SimpleAnalyzer(LUCENE_VERSION));
         analyzerPerField.put("Revision", new WhitespaceAnalyzer(LUCENE_VERSION));
         analyzerPerField.put("RevisionNumber", new WhitespaceAnalyzer(LUCENE_VERSION));
         analyzerPerField.put("Date", new WhitespaceAnalyzer(LUCENE_VERSION));
         analyzerPerField.put("Message", new StandardAnalyzer(LUCENE_VERSION));
         analyzerPerField.put("ChangedPath", new KeywordAnalyzer());
         analyzerPerField.put("Path", new KeywordAnalyzer());
         analyzerPerField.put("Change", new KeywordAnalyzer());
         analyzerPerField.put("CopyPath", new KeywordAnalyzer());
         analyzerPerField.put("CopyRevisionNumber", new WhitespaceAnalyzer(LUCENE_VERSION));
         analyzerPerField.put("CopyRevision", new WhitespaceAnalyzer(LUCENE_VERSION));
         analyzer = new PerFieldAnalyzerWrapper(new SimpleAnalyzer(LUCENE_VERSION), analyzerPerField);
      }
      
      return analyzer;
   }
}
