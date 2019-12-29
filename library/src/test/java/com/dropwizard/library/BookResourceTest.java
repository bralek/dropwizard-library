package com.dropwizard.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.meta.When;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.assertj.core.api.Assertions;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;

import com.dropwizard.library.db.BookDAO;
import com.dropwizard.library.db.model.Book;
import com.dropwizard.library.dto.BookDTO;
import com.dropwizard.library.resources.BookResource;

import io.dropwizard.testing.junit.ResourceTestRule;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;

@ExtendWith(DropwizardExtensionsSupport.class)
public class BookResourceTest {
	private static final BookDAO bookDao = Mockito.mock(BookDAO.class);
	public static final ResourceExtension RULE = ResourceExtension.builder()
            .addResource(new BookResource(bookDao))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();
	
	private Book book;
	private List<Book> books = new ArrayList<Book>();
	private BookDTO dto;
	private List<BookDTO> dtos = new ArrayList<BookDTO>();
	private List<String> isbns = new ArrayList<String>();
	
	

	@BeforeEach
	public void setUp() {
		book = new Book();
		book.setId(1L);
		book.setAuthors("Test Author 1");
		book.setGenre("Test Genre");
		book.setIsbn("111");
		book.setPageNumber(12);
		books.add(book);
		
		dto = new BookDTO();
		dto.setGenre("Test Genre");
		dto.setIsbn("1111111111");
		dto.setPageNumber(12);
		List<String> authors = new ArrayList<String>();
		authors.add("JKKRowling");
		dto.setAuthors(authors);
		dtos.add(dto);
		
		isbns.add("111");
	}
	
	@AfterEach
	public void tearDown() {
		Mockito.reset(bookDao);
	}
	
	@Test
	public void getBookSuccess() {
		Mockito.when(bookDao.findByIsbns(isbns)).thenReturn(books);
		Response result = RULE.target("/book?isbn=111").request().get();
		List<BookDTO> books = (List<BookDTO>) result.getEntity();
		Assertions.assertThat(books.get(0).getId()).isEqualTo(book.getId());
		Assertions.assertThat(books.get(0).getGenre()).isEqualTo(book.getGenre());
		Assertions.assertThat(books.get(0).getIsbn()).isEqualTo(book.getIsbn());
		Assertions.assertThat(books.get(0).getPageNumber()).isEqualTo(book.getPageNumber());
		Assertions.assertThat(books.get(0).getId()).isEqualTo(Arrays.asList(book.getAuthors().split(",")));
		Mockito.verify(bookDao).findByIsbns(isbns);
	}
	
	@Test
	public void saveBookSuccess() {
		
		Response result = RULE.target("/book").request().post(Entity.entity(dtos, MediaType.APPLICATION_JSON));
		Assertions.assertThat(result.getStatus()).isEqualTo(Status.OK);
		Mockito.verify(bookDao).insertBooks(books);
	}
	
	@Test
	public void saveBookWrongIsbn() {
		dtos.get(0).setIsbn("111");
		Response result = RULE.target("/book").request().post(Entity.entity(dtos, MediaType.APPLICATION_JSON));
		Assertions.assertThat(result.getStatus()).isEqualTo(Status.BAD_REQUEST);
		Mockito.verify(bookDao).insertBooks(books);
	}
	
	@Test
	public void saveBookEmptyAuthors() {
		dtos.get(0).setAuthors(null);;
		Response result = RULE.target("/book").request().post(Entity.entity(dtos, MediaType.APPLICATION_JSON));
		Assertions.assertThat(result.getStatus()).isEqualTo(Status.BAD_REQUEST);
		Mockito.verify(bookDao).insertBooks(books);
	}
}
