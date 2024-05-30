import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;
    private AuthorBookDAO authorBookDAO;
    private JTextArea displayArea;

    public MainFrame() {
        authorDAO = new AuthorDAO();
        bookDAO = new BookDAO();
        authorBookDAO = new AuthorBookDAO();
        setTitle("Book Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 1));

        // Author Panel
        JPanel authorPanel = new JPanel();
        authorPanel.setLayout(new FlowLayout());
        JTextField authorNameField = new JTextField(20);
        JButton addAuthorButton = new JButton("Add Author");
        addAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    authorDAO.addAuthor(authorNameField.getText());
                    authorNameField.setText("");
                    displayAuthors();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        authorPanel.add(new JLabel("Author Name:"));
        authorPanel.add(authorNameField);
        authorPanel.add(addAuthorButton);
        controlPanel.add(authorPanel);

        // Book Panel
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new FlowLayout());
        JTextField bookTitleField = new JTextField(20);
        JTextField bookDescriptionField = new JTextField(20);
        JTextField bookAuthorIdField = new JTextField(5);
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = bookDAO.addBook(bookTitleField.getText(), bookDescriptionField.getText());
                    int authorId = Integer.parseInt(bookAuthorIdField.getText());
                    authorBookDAO.addAuthorBook(authorId, bookId);
                    bookTitleField.setText("");
                    bookDescriptionField.setText("");
                    bookAuthorIdField.setText("");
                    displayBooks();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        bookPanel.add(new JLabel("Book Title:"));
        bookPanel.add(bookTitleField);
        bookPanel.add(new JLabel("Description:"));
        bookPanel.add(bookDescriptionField);
        bookPanel.add(new JLabel("Author ID:"));
        bookPanel.add(bookAuthorIdField);
        bookPanel.add(addBookButton);
        controlPanel.add(bookPanel);

        // Update Book Panel
        JPanel updateBookPanel = new JPanel();
        updateBookPanel.setLayout(new FlowLayout());
        JTextField updateBookIdField = new JTextField(5);
        JTextField updateBookTitleField = new JTextField(20);
        JTextField updateBookDescriptionField = new JTextField(20);
        JButton updateBookButton = new JButton("Update Book");
        updateBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(updateBookIdField.getText());
                    String title = updateBookTitleField.getText();
                    String description = updateBookDescriptionField.getText();
                    bookDAO.updateBook(bookId, title, description);
                    updateBookIdField.setText("");
                    updateBookTitleField.setText("");
                    updateBookDescriptionField.setText("");
                    displayBooks();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        updateBookPanel.add(new JLabel("Book ID:"));
        updateBookPanel.add(updateBookIdField);
        updateBookPanel.add(new JLabel("New Title:"));
        updateBookPanel.add(updateBookTitleField);
        updateBookPanel.add(new JLabel("New Description:"));
        updateBookPanel.add(updateBookDescriptionField);
        updateBookPanel.add(updateBookButton);
        controlPanel.add(updateBookPanel);

        // Delete Book Panel
        JPanel deleteBookPanel = new JPanel();
        deleteBookPanel.setLayout(new FlowLayout());
        JTextField deleteBookIdField = new JTextField(5);
        JButton deleteBookButton = new JButton("Delete Book");
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(deleteBookIdField.getText());
                    bookDAO.deleteBook(bookId);
                    deleteBookIdField.setText("");
                    displayBooks();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        deleteBookPanel.add(new JLabel("Book ID:"));
        deleteBookPanel.add(deleteBookIdField);
        deleteBookPanel.add(deleteBookButton);
        controlPanel.add(deleteBookPanel);

        // Display Books Button
        JButton displayBooksButton = new JButton("Display All Books");
        displayBooksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    displayBooks();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        controlPanel.add(displayBooksButton);

        add(controlPanel, BorderLayout.NORTH);
    }

    private void displayAuthors() throws SQLException {
        List<String> authors = authorDAO.getAllAuthors();
        displayArea.setText("");
        for (String author : authors) {
            displayArea.append(author + "\n");
        }
    }

    private void displayBooks() throws SQLException {
        List<String> books = authorBookDAO.getAllBooksAndAuthors();
        displayArea.setText("");
        for (String book : books) {
            displayArea.append(book + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
