import java.time.temporal.Temporal;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;


    class Loan {
        private Book book;
        private Member member;
        private Date dueDate;
        // yyyy-mm-dd
        private Date returnDate;

        public Loan(Book book, Member member, Date dueDate) {
            this.book = book;
            this.member = member;
            this.dueDate = dueDate;
            this.returnDate = null;
        }

        // Getters for loan attributes

        public Book getBook() {

            return book;
        }

        public Member getMember() {
            return member;
        }


        public Date getDueDate(){
            return dueDate;
        }

        public Date getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(Date returnDate) {
            this.returnDate = returnDate;
        }

        public long getOverdueDays() {
            if (returnDate != null && returnDate.after(dueDate)) {
                return (returnDate.getTime() - dueDate.getTime()) / (24 * 60 * 60 * 1000);
            }
            return 0;
        }


        @Override
        public String toString() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return "User: " + member.getName() +
                    ", Book: " + book.getTitle() +
                    ", Action: " + (returnDate == null ? "Borrowed Book" : "Returned Book") +
                    ", Date: " + sdf.format(returnDate != null ? returnDate : dueDate) +
                    ", Overdue days: " + getOverdueDays();
        }
    }



