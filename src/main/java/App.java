import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      model.put("books", Book.all());
      model.put("authors", Author.all());
      model.put("patrons", Patron.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/add_book", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Book newBook = new Book(name);
      newBook.save();
      response.redirect("/");
      return null;
    });

    post("/add_author", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Author newAuthor = new Author(name);
      newAuthor.save();
      response.redirect("/");
      return null;
    });


      post("/add_patron", (request, response) -> {
          HashMap<String, Object> model = new HashMap<String, Object>();
          String name = request.queryParams("patron_name");
          Patron newPatron = new Patron(name);
          newPatron.save();
          response.redirect("/");
          return null;
        });

    get("/author/:id", (request,response) ->{
     HashMap<String, Object> model = new HashMap<String, Object>();

      int author_id = Integer.parseInt(request.params("id"));
      Author author = Author.find(author_id);
      List<Book> books = author.getBooks2();
     model.put("books", books);
     model.put("all_books", Book.all());
     model.put("author", author);

      model.put("template", "templates/author.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/update_author", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int author_id = Integer.parseInt(request.queryParams("author_id"));
      Author myAuthor = Author.find(author_id);
      String name = request.queryParams("name");
      String number = request.queryParams("number");
      myAuthor.update(name, number);
      response.redirect("/authors/" + author_id);
      return null;
    });

    post("/add_books", (request, response) -> {
      int authorId = Integer.parseInt(request.queryParams("author_id"));
      Author author = Author.find(authorId);
      int bookId = Integer.parseInt(request.queryParams("book_id"));
      Book book = Book.find(bookId);
      author.addBook(book);
      response.redirect("/authors/" + authorId);
      return null;
    });

    get("/book/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int book_id = Integer.parseInt(request.params("id"));
      Book myBook = Book.find(book_id);
      ArrayList<Author> myAuthors = myBook.getAuthors();
      model.put("book", myBook);
      model.put("myAuthors", myAuthors);
      model.put("authors", Author.all());
      model.put("majors", Department.all());
      model.put("template", "templates/book.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/update_book", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int book_id = Integer.parseInt(request.queryParams("book_id"));
      Book myBook = Book.find(book_id);
      String name = request.queryParams("name");
      String date = request.queryParams("date");
      int majorId = Integer.parseInt(request.queryParams("major_id"));

      myBook.update(name);
      response.redirect("/book/" + book_id);
      return null;
    });

    post("/add_authors", (request, response) -> {
      int book_id = Integer.parseInt(request.queryParams("book_id"));
      Book myBook = Book.find(book_id);
      int author_id = Integer.parseInt(request.queryParams("author_id"));
      Author author = Author.find(author_id);
      myBook.addAuthor(author);
      response.redirect("/book/" + book_id);
      return null;
    });

    get("/bookList", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/bookList.vtl");
      model.put("books", Book.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/authorList", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/authorList.vtl");
      model.put("authors", Author.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }
}
