/**
 * RepositoryFactoryTest - Unit tests for the RepositoryFactory class.
 */
package com.fuerve.villageelder.sourcecontrol;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author lparker
 *
 */
public class RepositoryFactoryTest {

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RepositoryFactory#getRepositoryInstance(com.fuerve.villageelder.sourcecontrol.RepositoryProviderType, java.lang.String)}.
    */
   @Test
   public void testGetRepositoryInstanceRepositoryProviderTypeString() {
      final String pathExpected = "testpath";
      Repository target =
            RepositoryFactory.getRepositoryInstance(
                  RepositoryProviderType.MOCK, pathExpected
            );
      
      assertEquals(pathExpected, target.getPath());
   }

   /**
    * Test method for {@link com.fuerve.villageelder.sourcecontrol.RepositoryFactory#getRepositoryInstance(com.fuerve.villageelder.sourcecontrol.RepositoryProviderType, java.lang.String, java.lang.String, java.lang.String)}.
    */
   @Test
   public void testGetRepositoryInstanceRepositoryProviderTypeStringStringString() {
      final String pathExpected = "testpath";
      final String usernameExpected = "testusername";
      final String passwordExpected = "testpassword";
      
      Repository target =
            RepositoryFactory.getRepositoryInstance(
                  RepositoryProviderType.MOCK,
                  pathExpected,
                  usernameExpected,
                  passwordExpected
            );
      
      assertEquals(pathExpected, target.getPath());
      assertEquals(usernameExpected, target.getUsername());
      assertEquals(passwordExpected, target.getPassword());
   }

}
