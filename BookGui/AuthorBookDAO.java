import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorBookDAO {
    private Connection connection;

    public AuthorBookDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAuthorBook(int authorId, int bookId) throws SQLException {
        String query = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, authorId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
        }
    }

    public List<String> getBooksByAuthor(int authorId) throws SQLException {
        List<String> books = new ArrayList<>();
        String query = "SELECT b.book_id, b.title, b.description FROM books b " +
                "JOIN author_book ab ON b.book_id = ab.book_id WHERE ab.author_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(rs.getInt("book_id") + ": " + rs.getString("title") + " - " + rs.getString("description"));
                }
            }
        }
        return books;
    }

    public List<String> getAllBooksAndAuthors() throws SQLException {
        List<String> booksAndAuthors = new ArrayList<>();
        String query = "SELECT b.book_id, b.title, b.description, a.name FROM books b " +
                "JOIN author_book ab ON b.book_id = ab.book_id " +
                "JOIN authors a ON a.author_id = ab.author_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                booksAndAuthors.add(rs.getInt("book_id") + ": " + rs.getString("title") + " by " + rs.getString("name") + " - " + rs.getString("description"));
            }
        }
        return booksAndAuthors;
    }
}
