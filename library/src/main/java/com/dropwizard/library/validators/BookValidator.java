package com.dropwizard.library.validators;

import java.util.ArrayList;
import java.util.List;

import com.dropwizard.library.dto.BookDTO;

public class BookValidator {
	public static List<String> validate(List<BookDTO> books) {
		List<String> validationMessages = new ArrayList<String>();
		books.forEach(b -> {
			if (b.getIsbn().length() != 13 && b.getIsbn().length() != 10) {
				validationMessages.add("Book with ISBN: " + b.getIsbn() + " has wrong ISBN");
			}
			if (b.getAuthors().isEmpty()) {
				validationMessages.add("Book cannot have 0 authors");
			}
		});
		return validationMessages;
	}
}
