package com.dropwizard.library.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dropwizard.library.db.BookDAO;
import com.dropwizard.library.db.model.Book;
import com.dropwizard.library.dto.BookDTO;
import com.dropwizard.library.mappers.BookMapper;
import com.dropwizard.library.validators.BookValidator;

@Path("/book")
public class BookResource {
//	@Inject
	private BookDAO bookDao;
	
	public BookResource(BookDAO bookDao) {
		this.bookDao = bookDao;
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response saveBooks(List<BookDTO> bookDtos) {
		List<String> validationMessages = BookValidator.validate(bookDtos);
		if (!validationMessages.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
		}
		List<Book> books = BookMapper.bookDtoToBook(bookDtos);
		bookDao.insertBooks(books);
		return Response.status(Status.OK).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getBooks(@QueryParam(value = "isbn") List<String> isbns) {
		List<Book> books = bookDao.findByIsbns(isbns);
		List<BookDTO> dtos = BookMapper.bookToBookDto(books);
		return Response.ok().entity(dtos).build();
	}

}
