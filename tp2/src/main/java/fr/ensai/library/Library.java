package fr.ensai.library;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Library {
    
    private String name;
    private List<Item> items;
    private List<Loan> activeLoans;
    private List<Loan> completedLoans;


    public Library(String name, List<Item> items) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public void addIem(Item item) {
        this.items.add(item);
    }

    public void displayItems() {
        if (items.isEmpty()) {
            System.out.println("No items available in the library.");
        } else {
            System.out.println("Items in " + name + " Library:");
            for (Item item : items) {
                System.out.println(item); // Uses Book's toString() method
            }
        }
    }

    public Loan findActiveLoanForItem(Item item) {
        for (Loan loan : activeLoans) {
            if (loan.getItem().equals(item)) {
                return loan;
            }
        }
        return null; // No active loan found for the item
    }

    // Method to get books by author
    public ArrayList<Book> getBooksByAuthor(Author author) {
        ArrayList<Book> booksByAuthor = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equals(author)) {
                booksByAuthor.add(book);
            }
        }
        return booksByAuthor;
    }

    // Method to loan an item to a student
    public boolean loanItem(Item item, Student student) {
        Loan activeLoan = findActiveLoanForItem(item);
        if (activeLoan == null) {
            Loan newLoan = new Loan(student, item, LocalDate.now());
            activeLoans.add(newLoan);
            return true; // Loan successful
        }
        return false; // Item is already loaned out
    }

    // Method to render an item (return it)
    public boolean renderItem(Item item) {
        Loan activeLoan = findActiveLoanForItem(item);
        if (activeLoan != null) {
            activeLoan.setReturnDate(LocalDate.now());
            activeLoans.remove(activeLoan);
            completedLoans.add(activeLoan);
            return true; // Item returned successfully
        }
        return false; // No active loan found
    }

    // Method to display active loans
    public void displayActiveLoans() {
        if (activeLoans.isEmpty()) {
            System.out.println("No active loans.");
        } else {
            for (Loan loan : activeLoans) {
                loan.displayLoanInfo();
            }
        }
    }


    /**
     * Loads books from a CSV file and adds them to the library.
     * 
     * @param filePath The path to the CSV file containing book data.
     * @throws IOException If there is an error reading the file, an
     *                     {@link IOException} will be thrown.
     */
    public void loadBooksFromCSV(String filePath) {

        URL url = getClass().getClassLoader().getResource(filePath);

        try (BufferedReader br = new BufferedReader(new FileReader(url.getFile()))) {
            Map<String, Author> authors = new HashMap<>();
            String line;
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 5) {
                    String isbn = data[0].trim();
                    String title = data[1].trim();
                    String authorName = data[2].trim();
                    int year = Integer.parseInt(data[3].trim());
                    int pageCount = Integer.parseInt(data[4].trim());

                    // Check if author already exists in the map
                    Author author = authors.get(authorName);
                    if (author == null) {
                        author = new Author(authorName);
                        authors.put(authorName, author);
                        System.out.println(author.toString());
                    }
                    Book book = new Book(isbn, title, author, year, pageCount);

                    this.addIem(book);
                }
            }
        } catch (

        IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}