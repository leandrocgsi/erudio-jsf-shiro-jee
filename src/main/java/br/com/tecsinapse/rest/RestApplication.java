package br.com.tecsinapse.rest;


import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rest")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        resources.add(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(br.com.tecsinapse.rest.endpoints.HelloResource.class);
        resources.add(br.com.tecsinapse.rest.endpoints.JogadorResource.class);
        resources.add(br.com.tecsinapse.rest.endpoints.LocalResource.class);
        resources.add(br.com.tecsinapse.rest.endpoints.QuadraResource.class);
    }
}