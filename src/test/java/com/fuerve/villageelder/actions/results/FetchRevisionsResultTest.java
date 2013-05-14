/**
 * 
 */
package com.fuerve.villageelder.actions.results;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fuerve.villageelder.sourcecontrol.RevisionInfo;

/**
 * @author lparker
 *
 */
public class FetchRevisionsResultTest {

   /**
    * Test method for {@link com.fuerve.villageelder.actions.results.FetchRevisionsResult#iterator()}.
    */
   @Test
   public final void testIterator() {
      boolean fail = true;
      FetchRevisionsResult target = new FetchRevisionsResult();
      List<RevisionInfo> testRevisions = new ArrayList<RevisionInfo>();
      testRevisions.add(getDummyRevisionInfo(1L, "testauthor1", new Date(), "testmessage1"));
      testRevisions.add(getDummyRevisionInfo(2L, "testauthor2", new Date(), "testmessage2"));
      
      target.aggregate(testRevisions);
      
      for (RevisionInfo rev : testRevisions) {
         assertEquals(true, rev.getAuthor().startsWith("testauthor"));
         assertEquals(true, rev.getMessage().startsWith("testmessage"));
         fail = false;
      }
      
      // If no values came through the iterator, this test is a failure.
      if (fail) {
         fail();
      }
   }

   /**
    * Gets a dummy {@link RevisionInfo} object.
    * @param revision The revision number.
    * @param author The author of the revision.
    * @param date The date on which the revision was committed.
    * @param message The message associated with the revision.
    * @return The populated {@link RevisionInfo} instance.
    */
   private RevisionInfo getDummyRevisionInfo(
         long revision,
         String author,
         Date date,
         String message
         ) {
      return new RevisionInfo(revision, author, date, message);
   }
}
