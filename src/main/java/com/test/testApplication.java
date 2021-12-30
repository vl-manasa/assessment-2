package com.test;



import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.controller.EmployeeRESTController;

import com.test.auth.AppAuthorizer;
import com.test.auth.AppBasicAuthenticator;
import com.test.auth.User;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;



public class testApplication extends Application<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(testApplication.class);

    @Override
    public void initialize(Bootstrap<Configuration> b) {
    }

    @Override
    public void run(Configuration c, Environment e) throws Exception {
        LOGGER.info("Registering REST resources");

        e.jersey().register(new EmployeeRESTController(e.getValidator()));

        e.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new AppBasicAuthenticator())
                .setAuthorizer(new AppAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));
        e.jersey().register(RolesAllowedDynamicFeature.class);
        e.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

    public static void main(String[] args) throws Exception {
        new testApplication().run(args);
    }
}

