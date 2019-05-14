package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Book;
import model.Order;
import model.User;

public class DatabaseHelperMethods {
	
	private static ProgressIndicator progressIndicator;
	
	private static ObservableList<Book> bookData = FXCollections.observableArrayList();
	private static TableView<Book> bookTable = new TableView<Book>();
	
	private static ObservableList<Order> orderData = FXCollections.observableArrayList();
	private static TableView<Order> orderTable = new TableView<Order>();
        
        private static ObservableList<User> userData = FXCollections.observableArrayList();
	private static TableView<User> userTable = new TableView<User>();
        
        private static ObservableList<Book> topBookData = FXCollections.observableArrayList();
	private static TableView<Book> topBookTable = new TableView<Book>();
        
        private static ObservableList<User> topUserData = FXCollections.observableArrayList();
	private static TableView<User> topUserTable = new TableView<User>();
        
        private static TextField profileUserName;
        private static TextField profilePassword;
        private static TextField profileFirstName;
        private static TextField profileAddress;
        private static TextField profilePhone;
        private static TextField profileLastName;
        private static TextField profileEmail;
	
	public static void loadBookData(TableView<Book> table , ProgressIndicator indicator) {	
		System.out.println("Loading book table from database");
		
		bookTable = table;
		progressIndicator  = indicator;
		bookData.clear();
		
		progressIndicator.setProgress(-1);
		
                
                if(DatabaseMethods.select_all_books()){
                    bookData = DatabaseMethods.get_book_data();
                }
		
		bookTable.setItems(bookData);
		
		progressIndicator.setProgress(1);
		System.out.println("Loading book table has finished");
	}
        
        public static void refreshBookTable(){
            loadBookData(bookTable , progressIndicator);
        }
	
	public static void addBook(String isbn , String title , String publisher , String year , String price , String authors , String qunatity
			, String minQuantity , String category , int categoryId) {
		System.out.println("Adding book to database");
		progressIndicator.setProgress(-1);
		
		Book book = new Book(isbn,title,authors,publisher,year,category,price,qunatity,minQuantity,categoryId);
		if(DatabaseMethods.insert_book(book)){
                    bookData.add(book);
                }
                
		bookTable.setItems(bookData);
		
		progressIndicator.setProgress(1);
		System.out.println("Adding book has finished");
	}
	
	public static void searchBook(String key) {
		System.out.println("Searching in database");
		progressIndicator.setProgress(-1);
		
                bookData.clear();
                
                if(DatabaseMethods.search_book(key)){
                    bookData = DatabaseMethods.get_book_data();
                }		
		
		bookTable.setItems(bookData);
		
		progressIndicator.setProgress(1);
		System.out.println("Searching has finished");
	}
	
	public static boolean updateBook(String isbn , Book book) {
		System.out.println("Updating book in database");
		progressIndicator.setProgress(-1);
		
		if(DatabaseMethods.update_book(book, isbn)){
                    progressIndicator.setProgress(1);
                    System.out.println("Updating book has finished");
                    return true;
                }
                		
		progressIndicator.setProgress(1);
		System.out.println("Updating book has failed");
                
                return false;
	}
	
	public static void deleteBook(Book book) {
		System.out.println("Deleting book from database");
		progressIndicator.setProgress(-1);
		
		// TODO : delete book from databse
		bookData.remove(book);
		
		bookTable.setItems(bookData);
		
		progressIndicator.setProgress(1);
		System.out.println("Deleting book has finished");
	}
	
	public static void loadOrderData(TableView<Order> table) {
		System.out.println("Loading order table from database");
		progressIndicator.setProgress(-1);
		
		orderTable = table;
		
		if(DatabaseMethods.select_all_orders()){
                    orderData = DatabaseMethods.get_order_data();
                }
		
		orderTable.setItems(orderData);
		
		progressIndicator.setProgress(1);
		System.out.println("Loading order table has finished");
	}
        
        public static void refreshOrderTable(){
            loadOrderData(orderTable);
        }

	public static void confirmOrder(Order order) {
		System.out.println("Confirming order in database");
		progressIndicator.setProgress(-1);
		
		if(DatabaseMethods.confirm_order(order.getId())){
                    orderData.remove(order);
                }
		
		orderTable.setItems(orderData);
		
		progressIndicator.setProgress(1);
		System.out.println("Confirming order has finished");
	}
        
        public static boolean loginCheck(String userName , String password){
           if(DatabaseMethods.log_in(userName, password)){
               return true;
           }  
           else{
               return false;
           }
        }
        
        public static boolean loginAdminCheck(String userName){
           if(DatabaseMethods.check_admin(userName)){
               return true;
           }  
           else{
               return false;
           }
        }
        
        public static boolean signupCheck(String userName , String password , String lastName , String firstName , String email , String phone , String address){
            User user = new User(userName,password,firstName,lastName,email,phone,address,0);
            if(DatabaseMethods.sign_up(user)){
                return true;
            }
            else{
                return false;
            }
            
        }
        
        public static void loadUserTable(TableView<User> table) {
		userTable = table;

		userData.clear();
		
		progressIndicator.setProgress(-1);
		
		// TODO : load data from db
                if(DatabaseMethods.select_all_users()){
                    userData = DatabaseMethods.get_user_data();
                }
		
		userTable.setItems(userData);
		
		progressIndicator.setProgress(1);
		System.out.println("Loading user table has finished");
	}
	
	public static void loadTopBookTable(TableView<Book> table) {
		topBookTable = table;

		topBookData.clear();
		
		progressIndicator.setProgress(-1);
		
		// TODO : load data from db
                if(DatabaseMethods.sales_analaysis()){
                    topBookData = DatabaseMethods.get_top_ten_books();
                }
		
		topBookTable.setItems(topBookData);
		
		progressIndicator.setProgress(1);
		System.out.println("Loading top book table has finished");
	}
	
	public static void loadTopUserTable(TableView<User> table) {
		topUserTable = table;

		topUserData.clear();
		
		progressIndicator.setProgress(-1);
		
		// TODO : load data from db
                if(DatabaseMethods.sales_analaysis()){
                    topUserData = DatabaseMethods.get_top_five_user();
                }
		
		topUserTable.setItems(topUserData);
		
		progressIndicator.setProgress(1);
		System.out.println("Loading top user table has finished");
	}
	
	public static String getSells() {
		//TODO : get sells from db
            if(DatabaseMethods.sales_analaysis()){
                return DatabaseMethods.get_total_sales();
            }
            else{
               return null; 
            }
            
	}
        
        public static boolean promoteUser(User user){
            if(DatabaseMethods.update_user_admin(user)){
                
               return true;
            }
            else{
                
               return false; 
            }
        }
        
        public static void userTableRefresh(){
            userTable.refresh();
        }
        
        public static boolean addPublisherOrder(String isbn , String quantity) {
		// TODO : add to db
                if(DatabaseMethods.orders_from_publisher(isbn, quantity)){
                    refreshOrderTable();
                    return true;                  
                }
                
		return false;
	}
	
	public static boolean upDateProfile(String userName ,String password,String firstName,String address
			, String phone , String lastName , String email) {
		// TODO : update profile
                 User user = new User(userName,password,firstName,lastName,email,phone,address,0);
                 if(DatabaseMethods.update_user(user)){
                     return true;
                 }
                 else{
                    setProfile();
                     
                    return false;
                 }
		
	}
        
        
        public static void loadProfile(TextField profile_UserName , TextField profile_Password , TextField profile_FirstName
			, TextField profile_Address , TextField profile_Phone  , TextField profile_LastName , TextField profile_Email) {
		profileUserName = profile_UserName;
		profilePassword = profile_Password;
		profileFirstName = profile_FirstName;
		profileAddress = profile_Address;
		profilePhone = profile_Phone;
		profileLastName = profile_LastName;
		profileEmail = profile_Email;
                
                setProfile();
	}
        
        private static void setProfile(){
            // TODO 
                User user = DatabaseMethods.get_user_profile();
                
                
                
                profileUserName.setText(user.getUserName());
		profilePassword.setText(user.getPassword());
		profileFirstName.setText(user.getFirstName());
		profileAddress.setText(user.getAddress());
		profilePhone.setText(user.getPhone());
		profileLastName.setText(user.getLastName());
		profileEmail.setText(user.getEmail());
        }
}
