package com.dropwizard.library.db;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBeanList;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.dropwizard.library.db.model.Book;

public interface BookDAO {
	@SqlUpdate("insert into books (authors, page_number, genre, isbn, title) values <books>")
	void insertBooks(@BindBeanList(value = "books", propertyNames = {"authors", "pageNumber", "genre", "isbn", "title"}) List<Book> books);
	
	@SqlQuery("select * from books where isbn in (<isbns>)")
	@RegisterRowMapper(BookRowMapper.class)
	List<Book> findByIsbns(@BindList(value = "isbns") List<String> isbns);
	
	@SqlQuery("select * from books")
	@RegisterRowMapper(BookRowMapper.class)
	List<Book> findAllBooks();

}
