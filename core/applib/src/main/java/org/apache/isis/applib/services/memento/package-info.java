/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

/**
 * The {@link org.apache.isis.applib.services.memento.MementoService} was originally introduced to simplify the
 * implementation of ViewModels which are required by the framework to return string representation of all of their
 * backing state, moreover which is safe for use within a URL. This usage is deprecated; use JAXB view models instead.
 *
 * @see <a href="http://isis.apache.org/guides/rgsvc/rgsvc.html#_rgsvc_integration-api_MementoService">Reference guide</a>
 *
 * @deprecated
 */
package org.apache.isis.applib.services.memento;