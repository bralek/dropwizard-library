package com.dropwizard.library;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.jdbi.v3.core.Jdbi;

import com.dropwizard.library.db.BookDAO;
import com.dropwizard.library.resources.BookResource;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
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
        bootstrap.addBundle(new JdbiExceptionsBundle());
    }

    @Override
    public void run(final LibraryConfiguration configuration,
                    final Environment environment) {
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
