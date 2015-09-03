import java.util.List;
import java.util.ArrayList;
import java.util.*;
import org.sql2o.*;

public class Book {
  private int id;
  private String book_name;

  public int getId() {
    return id;
  }

  public String getName() {
    return book_name;
  }

  public void saveCheckout(int patron_id, String dueDate) {
    //make it so there is a row in the checkouts table which
    // contains book_id, patron_id, and dueDate
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO checkouts (patron_id, book_id, dueDate) values (:patron_id, :book_id, :dueDate)";
        con.createQuery(sql)
        .addParameter("patron_id", patron_id)
        .addParameter("book_id", this.id)
        .addParameter("dueDate", dueDate)
        .executeUpdate()
        .getKey();
    }

  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO books (book_name) VALUES (:book_name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("book_name", book_name)
        .executeUpdate()
        .getKey();
    }
  }

  public Book(String book_name) {
    this.book_name = book_name;
  }

  @Override
  public boolean equals(Object otherBook){
    if (!(otherBook instanceof Book)) {
      return false;
    } else {
      Book newBook = (Book) otherBook;
      return this.getName().equals(newBook.getName()) &&
             this.getId() == newBook.getId();
    }
  }


  public static List<Book> all() {
    String sql = "SELECT * FROM books ORDER BY book_name";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Book.class);
    }
  }


  public static Book find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM books where id=:id";
      Book book = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
      return book;
    }
  }

  public void update(String book_name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE books SET book_name = :book_name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("book_name", book_name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void addAuthor(Author author) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO authors_books (author_id, book_id, iscompleted) VALUES (:author_id, :book_id)";
      con.createQuery(sql)
        .addParameter("author_id", author.getId())
        .addParameter("book_id", this.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Author> getAuthors() {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT author_id FROM authors_books WHERE book_id = :book_id";
      List<Integer> authorIds = con.createQuery(sql)
        .addParameter("book_id", this.getId())
        .executeAndFetch(Integer.class);

      ArrayList<Author> authors = new ArrayList<Author>();

      for (Integer authorId : authorIds) {
          String bookQuery = "Select * From authors WHERE id = :authorId";
          Author author = con.createQuery(bookQuery)
            .addParameter("authorId", authorId)
            .executeAndFetchFirst(Author.class);
            authors.add(author);
      }
      return authors;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM books WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM authors_books WHERE book_id = :bookId";
        con.createQuery(joinDeleteQuery)
          .addParameter("bookId", this.getId())
          .executeUpdate();
    }
  }

}
