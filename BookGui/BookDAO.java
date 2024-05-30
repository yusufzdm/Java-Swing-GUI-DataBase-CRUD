import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;

    public BookDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addBook(String title, String description) throws SQLException {
        String query = "INSERT INTO books (title, description) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  // Return the generated book_id
                }
            }
        }
        return -1;  // Indicate failure
    }

    public List<String> getAllBooks() throws SQLException {
        List<String> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                books.add(rs.getInt("book_id") + ": " + rs.getString("title") + " - " + rs.getString("description"));
            }
        }
        return books;
    }

    public void updateBook(int bookId, String title, String description) throws SQLException {
        String query = "UPDATE books SET title = ?, description = ? WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, bookId);
            stmt.executeUpdate();
        }
    }

    public void deleteBook(int bookId) throws SQLException {
        // Önce author_book tablosundaki ilişkileri sil
        String deleteAuthorBookQuery = "DELETE FROM author_book WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteAuthorBookQuery)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }

        // Sonra books tablosundaki kaydı sil
        String deleteBookQuery = "DELETE FROM books WHERE book_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteBookQuery)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }
}
