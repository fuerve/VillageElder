/**
 * RevisionInfo - Represents a single source revision.
 */
package com.fuerve.villageelder.sourcecontrol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lparker
 *
 */
public class RevisionInfo {
   private final long revision;
   private final String author;
   private final Date date;
   private final String message;
   private final List<ChangePath> changePaths;
   
   /**
    * Initializes a new instance of RevisionInfo with a revision number, an
    * author name, a date and a revision message.  Upon construction, this
    * RevisionInfo does not yet possess any ChangePaths.  Those need to be
    * added via the {@link RevisionInfo#addChangePath} method.
    * @param rrevision The revision number.
    * @param aauthor The name of the author who committed the change.
    * @param ddate The date upon which the change was committed.
    * @param mmessage The revision message.
    */
   public RevisionInfo(
         final long rrevision,
         final String aauthor,
         final Date ddate,
         final String mmessage) {
      revision = rrevision;
      author = aauthor;
      date = ddate;
      message = mmessage;
      changePaths = new ArrayList<ChangePath>();
   }
   
   /**
    * Adds a ChangePath to this RevisionInfo.
    * @param changePath The ChangePath object to add to this RevisionInfo.
    */
   public void addChangePath(final ChangePath changePath) {
      changePaths.add(changePath);
   }
   
   /**
    * Gets the revision number of this RevisionInfo.
    * @return The revision number.
    */
   public long getRevision() {
      return revision;
   }
   
   /**
    * Gets the author of this RevisionInfo.
    * @return The revision author.
    */
   public String getAuthor() {
      return author;
   }
   
   /**
    * Gets the commit date of this RevisionInfo.
    * @return The date upon which this revision was committed.
    */
   public Date getDate() {
      return date;
   }
   
   /**
    * Gets the message associated with this RevisionInfo.
    * @return The revision message.
    */
   public String getMessage() {
      return message;
   }
   
   /**
    * Gets the full list of ChangePaths associated with this
    * RevisionInfo.
    * @return
    */
   public List<ChangePath> getChangePaths() {
      return changePaths;
   }
}
