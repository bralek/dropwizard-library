package com.dropwizard.library.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.dropwizard.library.db.model.Book;
import com.dropwizard.library.dto.BookDTO;

public class BookMapper {

	public static Book bookDtoToBook(BookDTO dto) {
		Book book = new Book();
		book.setGenre(dto.getGenre());
		book.setIsbn(dto.getIsbn());
		book.setPageNumber(dto.getPageNumber());
		book.setAuthors(dto.getAuthors().stream().collect(Collectors.joining(",")));
		return book;
	}
	
	public static List<Book> bookDtoToBook(List<BookDTO> dtos) {
		List<Book> books = new ArrayList<Book>();
		dtos.forEach(dto -> {
			books.add(bookDtoToBook(dto));
		});
		return books;
	}
	
	public static BookDTO bookToBookDto(Book book) {
		BookDTO dto = new BookDTO();
		dto.setGenre(book.getGenre());
		dto.setIsbn(book.getIsbn());
		dto.setPageNumber(book.getPageNumber());
		dto.setAuthors(Arrays.asList(book.getAuthors().split(",")));
		return dto;
	}
	
	public static List<BookDTO> bookToBookDto(List<Book> books){
		List<BookDTO> dtos = new ArrayList<BookDTO>();
		books.forEach(b -> {
			dtos.add(bookToBookDto(b));
		});
		return dtos;
	}
}
