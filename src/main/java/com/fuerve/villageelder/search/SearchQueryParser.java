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
package com.fuerve.villageelder.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.queryparser.classic.CharStream;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserTokenManager;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.Version;

/**
 * This class parses Lucene-syntax search queries with special handling
 * for particular fields that behave a little differently than
 * QueryParser can understand on its own.  Specifically, numeric and
 * date ranges are beyond Lucene's own QueryParser, so this thin wrapper
 * is necessary in order to implement them.
 * @author lparker
 *
 */
public class SearchQueryParser extends QueryParser {

   /**
    * Initializes a new instance of SearchQueryParser with an input stream.
    * @param stream The character stream from which to derive the query.
    */
   public SearchQueryParser(CharStream stream) {
      super(stream);
   }

   /**
    * Initializes a new instance of SearchQueryParser with a token manager.
    * @param tm The token manager from which to parse the query.
    */
   public SearchQueryParser(QueryParserTokenManager tm) {
      super(tm);
   }

   /**
    * Initializes a new instance of SearchQueryParser with a Lucene version,
    * a default field and an analyzer instance.
    * @param matchVersion The version of Lucene for which this query
    * should match expected behavior.
    * @param f The default field to search.
    * @param a The default analyzer to use when searching.
    */
   public SearchQueryParser(Version matchVersion, String f, Analyzer a) {
      super(matchVersion, f, a);
   }
   
   /**
    * Called by Lucene's {@link QueryParserBase} to compose a range query when
    * a range field is detected in the input.  This is overridden here in order
    * to provide special handling for specific fields.
    * @param field The name of the field for which the query is being composed.
    * @param part1 The lower bound of the specified numeric range.
    * @param part2 The upper bound of the specified numeric range.
    * @param startInclusive Determines whether the lower bound is inclusive.
    * @param endInclusive Determines whether the upper bound is inclusive.
    * @return The Lucene {@link Query} object appropriate for the requested
    * field.
    */
   public Query getRangeQuery(
         final String field,
         final String part1,
         final String part2,
         final boolean startInclusive,
         final boolean endInclusive)
      throws ParseException {
      TermRangeQuery query =
            (TermRangeQuery) super.getRangeQuery(
                  field,
                  part1,
                  part2,
                  startInclusive,
                  endInclusive);
      
      if ("RevisionNumber".equals(field)) {
         try {
            return NumericRangeQuery.newLongRange(
                  field,
                  Long.parseLong(part1),
                  Long.parseLong(part2),
                  startInclusive,
                  endInclusive);
         } catch (NumberFormatException e) {
            return query;
         }
      } else if ("Date".equals(field)) {
         try {
            return NumericRangeQuery.newLongRange(
                  field,
                  DateTools.stringToTime(part1),
                  DateTools.stringToTime(part2),
                  startInclusive,
                  endInclusive);
         } catch (java.text.ParseException e) {
            return query;
         }
      }
      return query;
   }
}
