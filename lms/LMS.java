package lms;

import java.util.*;
import java.io.*;

class LMS {
    final List<Book> books = new ArrayList<>();
    final Map<Book, Student> borrowedBooks = new HashMap<>();

    void addBook(Book book) {
        books.add(book);
    }

    boolean removeBook(Book book) {
        if (borrowedBooks.containsKey(book)) {
            return false; // cannot remove a borrowed book
        }
        return books.remove(book);
    }

    boolean borrowBook(Book book, Student student) {
        if (books.contains(book) && !borrowedBooks.containsKey(book)) {
            borrowedBooks.put(book, student);
            return true;
        }
        return false;
    }

    boolean returnBook(Book book) {
        if (borrowedBooks.containsKey(book)) {
            borrowedBooks.remove(book);
            return true;
        }
        return false;
    }

    void saveState(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[BOOKS]\n");
            for (Book book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor() + "\n");
            }
            writer.write("[BORROWED]\n");
            for (Map.Entry<Book, Student> entry : borrowedBooks.entrySet()) {
                Book book = entry.getKey();
                Student student = entry.getValue();
                writer.write(book.getTitle() + "," + student.getName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static LMS loadState(String filePath) {
        LMS lms = new LMS();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inBooksSection = false;
            boolean inBorrowedSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.equals("[BOOKS]")) {
                    inBooksSection = true;
                    inBorrowedSection = false;
                } else if (line.equals("[BORROWED]")) {
                    inBooksSection = false;
                    inBorrowedSection = true;
                } else if (inBooksSection) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String title = parts[0];
                        String author = parts[1];
                        Book book = new Book(title, author);
                        lms.addBook(book);
                    }
                } else if (inBorrowedSection) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String title = parts[0];
                        String studentName = parts[1];
                        Book book = new Book(title, ""); // Author not available
                        Student student = new Student(studentName, "", ""); // Surname and ID not available
                        lms.borrowedBooks.put(book, student);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lms;
    }
}
