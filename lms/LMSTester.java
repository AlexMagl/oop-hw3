package lms;

import java.util.*;
class LMSTester {
    public static void main(String[] args) {
        LMS iliauniLibrary = new LMS();

        Book lor = new Book("Lord of the rings", "tolkien");
        Book oop = new Book("OOP", "paata gogisvhili");
        iliauniLibrary.addBook(lor);
        iliauniLibrary.addBook(oop);

        Student gocha = new Student("Gocha", "Gegeshidze", "dfasdf");
        iliauniLibrary.borrowBook(lor, gocha);

        Student maka = new Student("Maka", "Lobjanidze", "3421325");
        iliauniLibrary.borrowBook(oop, maka);

        iliauniLibrary.saveState("libraryState.txt");

        LMS loadedLibrary = LMS.loadState("libraryState.txt");

        if (loadedLibrary != null) {
            System.out.println("Library state loaded successfully.");
            for (Book book : loadedLibrary.books) {
                System.out.println("Book: " + book.getTitle() + " by " + book.getAuthor());
            }
            for (Map.Entry<Book, Student> entry : loadedLibrary.borrowedBooks.entrySet()) {
                System.out.println("Borrowed Book: " + entry.getKey().getTitle() + " by " + entry.getValue().getName());
            }
        }
    }
}