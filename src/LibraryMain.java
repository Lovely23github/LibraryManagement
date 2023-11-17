import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import java.util.List;
import java.time.ZoneId;
import java.util.Scanner;


public class LibraryMain {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        LocalDate dueDate = LocalDate.now();



            while(true)
            {
                System.out.println("Welcome to Library Management System");
                System.out.println("1. Add Book\n" +
                        "2. Add Members\n" +
                        "3. Show Available Books\n" +
                        "4. Borrow Books\n" +
                        "5. Return Book\n" +
                        "6. Exit");

                System.out.println("Enter your choice!");
                int ch = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (ch) {
                    case 1:
                        // Add Book
                        Book newBook = getBookDetailsFromUser(scanner);
                        library.addBook(newBook);
                        break;
                    case 2:
                        // Add Member
                        Member newMember = getMemberDetailsFromUser(scanner);
                        library.addMember(newMember);
                        break;
                    case 3:
                        // Show Available Books
                        library.listAllBooks();
                        break;
                    case 4:
                        // Borrow Book
                        Book bookToBorrow = library.getAvailableBook();
                        if (bookToBorrow != null) {
                            Member memberToBorrow = getMemberByNameFromUser(library, scanner);
                            if (memberToBorrow != null) {
                                // Set the fine rate before borrowing
                               //setFineRate(library, scanner);
                                borrowBookFromUser(library, bookToBorrow, memberToBorrow, dueDate, scanner);
                            } else {
                                System.out.println("Member not found. Please add the member first.");
                            }
                        } else {
                            System.out.println("No available books for borrowing.");
                        }
                        break;
                    case 5:
                        // Return Book
                        Book bookToReturn = library.getBorrowedBook();
                        if (bookToReturn != null) {
                            // Set the fine rate before returning
                            //setFineRate(library, scanner);
                            returnBookFromUser(library, bookToReturn, scanner);
                        } else {
                            System.out.println("No borrowed books to return.");
                        }
                        break;
                    case 6:
                        System.out.println("Thank you for Using Application!!");
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Please Enter a valid choice");
                }
            }

    }


        private static void setFineRate(Library library, Scanner scanner) {
            System.out.print("Enter the fine rate per day: ");
            double fineRatePerDay = scanner.nextDouble();
            library.setFineRatePerDay(fineRatePerDay);
        }



    // Additional methods for user input handling
    private static Book getBookDetailsFromUser(Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        System.out.print("Enter book publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter book genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter number of pages: ");
        int numberOfPages = scanner.nextInt();
        scanner.nextLine();  // consume newline
        return new Book(title, author, publisher, genre, numberOfPages);
    }

    private static Member getMemberDetailsFromUser(Scanner scanner) {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        System.out.print("Enter member address: ");
        String address = scanner.nextLine();
        System.out.print("Enter member phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter member email address: ");
        String emailAddress = scanner.nextLine();
        return new Member(name, address, phoneNumber, emailAddress);
    }

    private static Member getMemberByNameFromUser(Library library, Scanner scanner) {
        System.out.print("Enter member name: ");
        String memberName = scanner.nextLine();
        return library.getMemberByName(memberName);
    }

    private static void borrowBookFromUser(Library library, Book book, Member member, LocalDate dueDate, Scanner scanner) {
        // method implementation

        System.out.println("Available Books:");
        library.listAllBooks();

        // Prompt the user to choose a book

        System.out.print("Enter the title of the book you want to borrow: ");
        String bookTitle = scanner.nextLine();


        // Get the chosen book from the library
        Book bookToBorrow = library.getBooks().stream()
                .filter(b -> b.getTitle().equals(bookTitle))
                .findFirst()
                .orElse(null);
        System.out.println(bookToBorrow);

        // Check if the chosen book is available
        if (bookToBorrow != null && !library.isBookBorrowed(bookToBorrow)) {
            // Ask for the member's name
            Member memberToBorrow = getMemberByNameFromUser(library, scanner);

            // Borrow the selected book
            library.borrowBook(bookToBorrow, memberToBorrow, dueDate);
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Invalid book selection or the book is not available for borrowing.");
        }
    }

    private static void returnBookFromUser(Library library, Book book, Scanner scanner) {
        // Returning  a book late
        System.out.println("Returning  a book ...");

        // Example: Taking input for returning a book
       System.out.print("Enter member name: ");
        String memberNameForCheckIn = scanner.nextLine();


        System.out.print("Enter book title to return: ");
        String bookToCheckIn = scanner.nextLine();


        // Simulating the return of a book three days after the due date
        Date returnDate = new Date();

        returnDate.setTime(returnDate.getTime() + ( 3* 24 * 60 * 60 * 1000)); // Adding 3 days

        // Call library.checkinBook with the provided inputs
        double overdueFine = library.returnBook(new Book(bookToCheckIn, "", "", "", 0), returnDate);


        if (overdueFine > 0) {
            System.out.println("Overdue fine: " + overdueFine);
        }

        // Generating reports
        System.out.println("Generating reports...");

        library.listAllBooks();
        library.listAllMembers();
        library.listBorrowedBooks();
        library.listOverdueBooks();


    }

    }


