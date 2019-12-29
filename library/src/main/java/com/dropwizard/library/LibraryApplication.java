package com.dropwizard.library;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.jdbi.v3.core.Jdbi;

import com.dropwizard.library.db.BookDAO;
import com.dropwizard.library.resources.BookResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LibraryApplication extends Application<LibraryConfiguration> {

    public static void main(final String[] args) throws Exception {
        new LibraryApplication().run(args);
    }

    @Override
    public String getName() {
        return "Library";
    }

    @Override
    public void initialize(final Bootstrap<LibraryConfiguration> bootstrap) {
    	bootstrap.addBundle(new MigrationsBundle<LibraryConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(LibraryConfiguration configuration) {
				return configuration.getDatabaseConfiguration();
			}
		});
    }

    @Override
    public void run(final LibraryConfiguration configuration,
                    final Environment environment) {
    	final FilterRegistration.Dynamic cors =
    	        environment.servlets().addFilter("CORS", CrossOriginFilter.class);

	    // Configure CORS parameters
	    cors.setInitParameter("allowedOrigins", "*");
	    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
	    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
	    
	    // Add URL mapping
	    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

	    // DO NOT pass a preflight request to down-stream auth filters
	    // unauthenticated preflight requests should be permitted by spec
	    cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());

    	
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDatabaseConfiguration(), "postgresql");
        final BookDAO bookDao = jdbi.onDemand(BookDAO.class);
        environment.jersey().register(new AbstractBinder() {
			
        	@Override
            protected void configure() {
            	bind(bookDao).to(BookDAO.class);
            }
		});
        
        environment.jersey().register(BookResource.class);
        
    }

}
