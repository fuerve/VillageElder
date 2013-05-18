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

import java.io.Reader;

import com.fuerve.villageelder.configuration.types.RepositoryProviderTypeProperty;
import com.fuerve.villageelder.configuration.types.StringProperty;
import com.fuerve.villageelder.configuration.types.TypedProperty;
import com.fuerve.villageelder.sourcecontrol.RepositoryProviderType;

/**
 * This class contains properties that are specific to source control.
 * Things that go in here might include relevant paths to source
 * control working copies or repositories, authentication information
 * and other source control configuration arguments.
 * 
 * @author lparker
 *
 */
public class SourceControlProperties extends PropertyHandler {
   private static final String REPOSITORY_PATH = "SourceControl.RepositoryPath";
   // Enable this once authentication has been wrapped and extended.
   //private static final String REPOSITORY_SIMPLEAUTH_PROPERTY = "SourceControl.SimpleAuthentication";
   // Disable these once the authentication refactor has taken place.
   private static final String REPOSITORY_SIMPLEAUTHENTICATION_USERNAME =
         "SourceControl.SimpleAuthentication.Username";
   private static final String REPOSITORY_SIMPLEAUTHENTICATION_PASSWORD =
         "SourceControl.SimpleAuthentication.Password";
   private static final String REPOSITORY_PROVIDERTYPE =
         "SourceControl.ProviderType";
   
   /**
    * Initializes a new instance of SourceControlProperties by reading
    * from the default properties file.
    */
   public SourceControlProperties() {
      super();
      
      requestProperties();
   }
   
   /**
    * Initializes a new instance of SourceControlProperties by reading
    * from a specified file on disk.
    * @param propertyFilename The pathname of the properties file.
    */
   public SourceControlProperties(final String propertyFilename) {
      super(propertyFilename);
      
      requestProperties();
   }
   
   /**
    * Initializes a new instance of SourceControlProperties.
    * @param ppropertySource The {@link Reader} from which to parse the
    * source control properties.
    */
   public SourceControlProperties(Reader ppropertySource) {
      super(ppropertySource);
      
      requestProperties();
   }
   
   private void requestProperties() {
      requestProperty(REPOSITORY_PATH, new StringProperty());
      requestProperty(REPOSITORY_SIMPLEAUTHENTICATION_USERNAME, new StringProperty());
      requestProperty(REPOSITORY_SIMPLEAUTHENTICATION_PASSWORD, new StringProperty());
      requestProperty(REPOSITORY_PROVIDERTYPE, new RepositoryProviderTypeProperty());
   }
   
   /**
    * Gets the path to the source control repository.
    * @return The path to the source control repository.
    */
   public String getRepositoryPath() {
      final TypedProperty<String> value = get(REPOSITORY_PATH);
      return value.getValue();
   }
   
   /**
    * Gets the simple authentication bare text username
    * to the source control repository.
    * @return The source control username.
    */
   public String getSimpleAuthenticationUsername() {
      final TypedProperty<String> value =
            get(REPOSITORY_SIMPLEAUTHENTICATION_USERNAME);
      return value.getValue();
   }
   
   /**
    * Gets the simple authentication bare text password
    * to the source control repository.
    * @return The source control password.
    */
   public String getSimpleAuthenticationPassword() {
      final TypedProperty<String> value =
            get(REPOSITORY_SIMPLEAUTHENTICATION_PASSWORD);
      return value.getValue();
   }
   
   /**
    * Gets the repository provider type (ie. Subversion).
    * @return The repository provider type.
    */
   public RepositoryProviderType getProviderType() {
      final TypedProperty<RepositoryProviderType> value =
            get(REPOSITORY_PROVIDERTYPE);
      return value.getValue();
   }
}
