/**
 * RepositoryProviderType - An explicit enumeration of supported source control
 * repositories to remove any ambiguity from the factory method instantiation
 * codepath.
 */
package com.fuerve.villageelder.sourcecontrol;

/**
 * @author lparker
 *
 */
public enum RepositoryProviderType {
   UNKNOWN,
   MOCK,
   SUBVERSION
}
