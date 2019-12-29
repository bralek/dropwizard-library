package com.dropwizard.library.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import com.dropwizard.library.db.model.Book;

public class BookRowMapper implements RowMapper<Book>{

	@Override
	public Book map(ResultSet rs, StatementContext ctx) throws SQLException {
		// TODO Auto-generated method stub
		Book book = new Book();
		book.setId(rs.getLong("id"));
		book.setAuthors(rs.getString("authors"));
		book.setGenre(rs.getString("genre"));
		book.setIsbn(rs.getString("isbn"));
		book.setPageNumber(rs.getInt("page_number"));
		return book;
	}

}
