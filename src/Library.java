import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;
    class Library {
        private List<Book> books;
        private List<Member> members;
        private List<Loan> loans;

        private double fineRatePerDay; // Fine rate per day in currency units

        public Library() {
            this.books = new ArrayList<>();
            this.members = new ArrayList<>();
            this.loans = new ArrayList<>();

        }

        // Methods for adding items to the library
        public void addBook(Book book) {
            books.add(book);
        }

        public void addMember(Member member) {
            members.add(member);
        }


        public Book getAvailableBook() {
            return books.stream().filter(book -> !isBookBorrowed(book)).findFirst().orElse(null);
        }

        public List<Book> getBooks() {
            return books;
        }
        // New method to get a member by name from the library
        public Member getMemberByName(String memberName) {
            return members.stream().filter(member -> member.getName().equals(memberName)).findFirst().orElse(null);
        }

        // New method to get a borrowed book from the library
        public Book getBorrowedBook() {
            return loans.stream().filter(loan -> loan.getReturnDate() == null).map(Loan::getBook).findFirst().orElse(null);
        }
        public Double getFineRatePerDay(){
            return fineRatePerDay;
        }

        // New method to check if a book is currently borrowed
        public boolean isBookBorrowed(Book book) {
            return loans.stream().anyMatch(loan -> loan.getBook().equals(book) && loan.getReturnDate() == null);
        }

        // this method encapsulates the process of borrowing a book. It creates a new loan record, adds it to the list of active loans,
        public void borrowBook(Book book, Member member, LocalDate dueDate) {
            Loan loan = new Loan(book, member, Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            loans.add(loan);
            System.out.println("Book borrowed successfully.");
            System.out.println(loan);
        }



        // This method handles the process of returning a book. It updates the return date, displays information about the returned loan, and calculates any overdue fines for the book
        public double returnBook(Book book, Date returnDate) {
            for (Loan loan : loans) {
                if (loan.getBook().getTitle().equals(book.getTitle()) && loan.getReturnDate() == null) {
                    loan.setReturnDate(Date.from(returnDate.toInstant()));
                    System.out.println("Book returned successfully.");
                    System.out.println(loan);
                    return calculateFine(loan);
                }
            }
            System.out.println("Book not found in the returned list.");
            return 0.0;
        }




        //calculating fines associated with a loan transaction
        private double calculateFine(Loan loan) {
            long overdueDays = loan.getOverdueDays();
            if (overdueDays > 0) {
                return fineRatePerDay * overdueDays;
            }
            return 0.0;
        }



        // Method for calculating overdue fines for a member
        public double calculateOverdueFines(Member member) {
            double totalFines = 0.0;
            for (Loan loan : loans) {
                if (loan.getMember().equals(member)) {
                    totalFines += calculateFine(loan);
                }
            }
            return totalFines;
        }

        public void setFineRatePerDay(double fineRatePerDay) {
            this.fineRatePerDay = fineRatePerDay;

        }



        // Methods for generating reports
        public void listAllBooks() {
            System.out.println("List of all Books:");
            for (Book book : books) {
                System.out.println("Title: " + book.getTitle() + ", Author: " + book.getAuthor()+  " Publisher: "+ book.getPublisher()+ " Genre: " +book.getGenre()+ " Number of Pages: " +book.getNumberOfPages());
            }
        }

        public void listAllMembers() {
            System.out.println("List of all Members:");
            for (Member member : members) {
                System.out.println("Name: " + member.getName() + ", Phone: " + member.getPhoneNumber() + " Addresss: "+ member.getAddress()+ " Email: " +member.getEmailAddress() );
            }
        }

        public void listBorrowedBooks() {
            for (Loan loan : loans) {
                if (loan.getReturnDate() == null) {
                    System.out.println(loan);
                }
            }
        }

        public void listOverdueBooks() {
            System.out.println("List of overdue books");
            for (Loan loan : loans) {
                if (loan.getOverdueDays() > 0) {
                    System.out.println(loan);
                }
            }
        }
    }
