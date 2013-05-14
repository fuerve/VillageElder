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
/**
 * This package contains classes that support returning results
 * from system actions through layers of abstraction that allow
 * the core engine to surface an API that can be used by pretty
 * much any consumer.  This design is intended to facilitate
 * building both a command line and a Web interface (or even an
 * RPC interface, if we get ambitious).
 * @author lparker
 *
 */
package com.fuerve.villageelder.actions.results;