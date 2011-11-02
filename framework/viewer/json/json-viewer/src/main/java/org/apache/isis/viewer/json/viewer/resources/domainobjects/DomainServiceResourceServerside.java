/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.viewer.json.viewer.resources.domainobjects;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.json.applib.JsonRepresentation;
import org.apache.isis.viewer.json.applib.RepresentationType;
import org.apache.isis.viewer.json.applib.RestfulMediaType;
import org.apache.isis.viewer.json.applib.domainobjects.DomainServiceResource;
import org.apache.isis.viewer.json.viewer.representations.RendererFactory;
import org.apache.isis.viewer.json.viewer.representations.RendererFactoryRegistry;
import org.apache.isis.viewer.json.viewer.resources.ResourceAbstract;

@Path("/services")
public class DomainServiceResourceServerside extends ResourceAbstract implements
        DomainServiceResource {

    @Override
    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_LIST, RestfulMediaType.APPLICATION_JSON_ERROR })
    public Response services() {
        RepresentationType representationType = RepresentationType.LIST;
        init(representationType);

        final List<ObjectAdapter> serviceAdapters = getResourceContext().getPersistenceSession().getServices();

        final RendererFactory factory = RendererFactoryRegistry.instance.find(representationType);
        
        final ListReprRenderer renderer = (ListReprRenderer) factory.newRenderer(getResourceContext(), null, JsonRepresentation.newMap());
        renderer.usingLinkToBuilder(new DomainServiceLinkTo())
                .withSelf("services")
                .with(serviceAdapters);
        
        return responseOfOk(renderer, Caching.ONE_DAY).build();
    }

    ////////////////////////////////////////////////////////////
    // domain service
    ////////////////////////////////////////////////////////////

    @GET
    @Path("/{serviceId}")
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_DOMAIN_OBJECT, RestfulMediaType.APPLICATION_JSON_ERROR })
    public Response service(
            @PathParam("serviceId") String serviceId) {
        init(RepresentationType.DOMAIN_OBJECT);
        
        final ObjectAdapter serviceAdapter = getServiceAdapter(serviceId);
        
        final RendererFactory rendererFactory = 
                rendererFactoryRegistry.find(RepresentationType.DOMAIN_OBJECT);
        
        final DomainObjectReprRenderer renderer = 
                (DomainObjectReprRenderer) rendererFactory.newRenderer(getResourceContext(), null, JsonRepresentation.newMap());
        renderer.usingLinkToBuilder(new DomainServiceLinkTo())
                    .with(serviceAdapter)
                    .includesSelf();

        return responseOfOk(renderer, Caching.ONE_DAY).build();
    }


    ////////////////////////////////////////////////////////////
    // domain service property
    ////////////////////////////////////////////////////////////

    @GET
    @Path("/{serviceId}/properties/{propertyId}")
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT_PROPERTY, RestfulMediaType.APPLICATION_JSON_ERROR })
    public Response propertyDetails(
            @PathParam("serviceId") final String serviceId,
            @PathParam("propertyId") final String propertyId) {
        init(RepresentationType.OBJECT_PROPERTY);

        final ObjectAdapter serviceAdapter = getServiceAdapter(serviceId);
        final DomainResourceHelper helper = new DomainResourceHelper(getResourceContext(), serviceAdapter).using(new DomainServiceLinkTo());

        return helper.propertyDetails(serviceAdapter, propertyId, Caching.ONE_DAY);
    }



    ////////////////////////////////////////////////////////////
    // domain service action
    ////////////////////////////////////////////////////////////
    
    @GET
    @Path("/{serviceId}/actions/{actionId}")
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_OBJECT_ACTION, RestfulMediaType.APPLICATION_JSON_ERROR })
    public Response actionPrompt(
            @PathParam("serviceId") final String serviceId, 
            @PathParam("actionId") final String actionId) {
        init(RepresentationType.OBJECT_ACTION);

        final ObjectAdapter serviceAdapter = getServiceAdapter(serviceId);
        final DomainResourceHelper helper = new DomainResourceHelper(getResourceContext(), serviceAdapter).using(new DomainServiceLinkTo());

        return helper.actionPrompt(actionId);
    }

    
    ////////////////////////////////////////////////////////////
    // domain service action invoke
    ////////////////////////////////////////////////////////////

    @GET
    @Path("/{serviceId}/actions/{actionId}/invoke")
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_ACTION_RESULT, RestfulMediaType.APPLICATION_JSON_ERROR })
    public Response invokeActionQueryOnly(
            @PathParam("serviceId") final String serviceId, 
            @PathParam("actionId") final String actionId,
            @QueryParam("args") final String arguments) {
        init(RepresentationType.ACTION_RESULT);

        final ObjectAdapter serviceAdapter = getServiceAdapter(serviceId);
        final DomainResourceHelper helper = new DomainResourceHelper(getResourceContext(), serviceAdapter).using(new DomainServiceLinkTo());

        return helper.invokeActionQueryOnly(actionId, arguments);
    }

    @PUT
    @Path("/{serviceId}/actions/{actionId}/invoke")
    @Consumes({ MediaType.WILDCARD })   // to save the client having to specify a Content-Type: application/json
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_ACTION_RESULT, RestfulMediaType.APPLICATION_JSON_ERROR })
    public Response invokeActionIdempotent(
            @PathParam("serviceId") final String serviceId, 
            @PathParam("actionId") final String actionId,
            final InputStream arguments) {
        init(RepresentationType.ACTION_RESULT);

        final ObjectAdapter serviceAdapter = getServiceAdapter(serviceId);
        final DomainResourceHelper helper = new DomainResourceHelper(getResourceContext(), serviceAdapter).using(new DomainServiceLinkTo());

        return helper.invokeActionIdempotent(actionId, arguments);
    }

    @POST
    @Path("/{serviceId}/actions/{actionId}/invoke")
    @Consumes({ MediaType.WILDCARD })   // to save the client having to specify a Content-Type: application/json
    @Produces({ MediaType.APPLICATION_JSON, RestfulMediaType.APPLICATION_JSON_ACTION_RESULT, RestfulMediaType.APPLICATION_JSON_ERROR })
    public Response invokeAction(
            @PathParam("serviceId") final String serviceId, 
            @PathParam("actionId") final String actionId,
            final InputStream arguments) {
        init(RepresentationType.ACTION_RESULT);

        final ObjectAdapter serviceAdapter = getServiceAdapter(serviceId);
        final DomainResourceHelper helper = new DomainResourceHelper(getResourceContext(), serviceAdapter).using(new DomainServiceLinkTo());

        return helper.invokeAction(actionId, arguments);
    }

}
