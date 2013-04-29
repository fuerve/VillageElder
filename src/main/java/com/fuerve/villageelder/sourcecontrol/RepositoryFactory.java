/**
 * RepositoryFactory - Factory method container for source control repository
 * connectivity providers.
 */
package com.fuerve.villageelder.sourcecontrol;

/**
 * @author lparker
 *
 */
public final class RepositoryFactory {
   /**
    * Hidden constructor.
    */
   private RepositoryFactory() { }
   
   /**
    * Gets a Repository instance by type, calling the barebones constructor
    * that simply asks for a repository path.
    * @param repositoryType The type of repository provider 
    * @param path The repository path.
    * @return The appropriate Repository instance (or null if none exists).
    */
   public static Repository getRepositoryInstance(
         final RepositoryProviderType repositoryType,
         final String path) {
      switch (repositoryType) {
      case MOCK:
         return new MockRepository(path);
      case SUBVERSION:
         return new SubversionRepository(path);
      default:
         return null;
      }
   }
   
   /**
    * Gets a Repository instance by type, calling the simple authentication
    * constructor.
    * @param repositoryType The type of repository provider.
    * @param path The repository path.
    * @param username The repository username.
    * @param password The repository password.
    * @return The appropriate Repository instance (or null if none exists).
    */
   public static Repository getRepositoryInstance(
         final RepositoryProviderType repositoryType,
         final String path,
         final String username,
         final String password) {
      switch (repositoryType) {
      case MOCK:
         return new MockRepository(path, username, password);
      case SUBVERSION:
         return new SubversionRepository(path, username, password);
      default:
         return null;
      }
   }
}
