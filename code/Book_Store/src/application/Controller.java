package application;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import model.Book;
import model.Cart;
import model.Order;
import model.User;

public class Controller implements Initializable{
	
	@FXML
	private TextField IsbnTextField;
	@FXML
	private TextField titleTextField;
	@FXML
	private TextField publisherTextField;
	@FXML
	private TextField yearTextField;
	@FXML
	private TextField priceTextField;
	@FXML
	private TextField authorsTextField;
	@FXML
	private TextField quantityTextField;
	@FXML
	private TextField minQuantityTextField;
	
	private String category = null;
	private int categoryId = -1;
	
	private String intRegex = "[0-9]*";
	private String stringRegex = "^[a-zA-Z]+([a-zA-Z0-9-/?:.,\'+_\\s])$";
        private String passwordRegex = "^[a-zA-Z0-9]+([a-zA-Z0-9-/?:.,\'+_\\s])$";
        private String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        
                
	public void addNewBookButton() {
		if(validate()) {
			DatabaseHelperMethods.addBook(IsbnTextField.getText().trim() , titleTextField.getText().trim()
					, publisherTextField.getText().trim(), yearTextField.getText().trim() , priceTextField.getText().trim()
					, authorsTextField.getText().trim() , quantityTextField.getText().trim() , minQuantityTextField.getText().trim()
					, category , categoryId);
		}
	}
	
	private void showError(String error) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText("Ooops, there was an error! Please check the " + error + " input");

		alert.showAndWait();
	}
	
	private boolean validate() {
		if(IsbnTextField.getText().trim().isEmpty()) {
			showError("Isbn");
			return false;
		}
		if(!IsbnTextField.getText().trim().matches(intRegex)) {
			showError("Isbn");
			return false;
		}
		if(titleTextField.getText().trim().isEmpty()) {
			showError("Title");
			return false;
		}
		if(!titleTextField.getText().trim().matches(stringRegex)) {
			showError("Title");
			return false;
		}
		if(publisherTextField.getText().trim().isEmpty()) {
			showError("Publisher");
			return false;
		}
		if(!publisherTextField.getText().trim().matches(stringRegex)) {
			showError("Publisher");
			return false;
		}
		if(yearTextField.getText().trim().isEmpty()) {
			showError("Year");
			return false;
		}
		if(!yearTextField.getText().trim().matches(intRegex)) {
			showError("Year");
			return false;
		}
		if(priceTextField.getText().trim().isEmpty()) {
			showError("Price");
			return false;
		}
		if(!priceTextField.getText().trim().matches(intRegex)) {
			showError("Peice");
			return false;
		}
		if(authorsTextField.getText().trim().isEmpty()) {
			showError("Authors");
			return false;
		}
		if(!authorsTextField.getText().trim().matches(stringRegex)) {
			showError("Authors");
			return false;
		}
		if(quantityTextField.getText().trim().isEmpty()) {
			showError("Quantity");
			return false;
		}
		if(!quantityTextField.getText().trim().matches(intRegex)) {
			showError("Quantity");
			return false;
		}
		if(minQuantityTextField.getText().trim().isEmpty()) {
			showError("Min Quantity");
			return false;
		}
		if(!minQuantityTextField.getText().trim().matches(intRegex)) {
			showError("Min Quantity");
			return false;
		}
		if(categoryId == -1) {
			showError("Category");
			return false;
		}
		return true;
	}
	
	@FXML
    private MenuButton categoryMenu;
    @FXML
    private RadioMenuItem scienceMenuItem;
    @FXML
    private RadioMenuItem artMenuItem;
    @FXML
    private RadioMenuItem religionMenuItem;
    @FXML
    private RadioMenuItem historyMenuItem;
    @FXML
    private RadioMenuItem geographyMenuItem;
    
    private void initMenu() {
    	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	if(e.getSource() == scienceMenuItem) {
            		category = "Science";
            		categoryId = 1;
            		artMenuItem.setSelected(false);
            		religionMenuItem.setSelected(false);
            		historyMenuItem.setSelected(false);
            		geographyMenuItem.setSelected(false);
            	}
				if(e.getSource() == artMenuItem) {
					category = "Art";
					categoryId = 2;  
					scienceMenuItem.setSelected(false);
            		religionMenuItem.setSelected(false);
            		historyMenuItem.setSelected(false);
            		geographyMenuItem.setSelected(false);
				}
				if(e.getSource() == religionMenuItem) {
					category = "Religion";
					categoryId = 3;
					scienceMenuItem.setSelected(false);
					artMenuItem.setSelected(false);
            		historyMenuItem.setSelected(false);
            		geographyMenuItem.setSelected(false);
				}
				if(e.getSource() == historyMenuItem) {
					category = "History";
					categoryId = 4;
					scienceMenuItem.setSelected(false);
					artMenuItem.setSelected(false);
					religionMenuItem.setSelected(false);
            		geographyMenuItem.setSelected(false);
				}
				if(e.getSource() == geographyMenuItem) {
					category = "Geography";
					categoryId = 5;
					scienceMenuItem.setSelected(false);
					artMenuItem.setSelected(false);
					religionMenuItem.setSelected(false);
					historyMenuItem.setSelected(false);
				}
				categoryMenu.setText(category);
            } 
        };
        
        scienceMenuItem.setOnAction(event);
        artMenuItem.setOnAction(event);
        religionMenuItem.setOnAction(event);
        historyMenuItem.setOnAction(event);
        geographyMenuItem.setOnAction(event);
    } 
    
	@FXML
	private TextField searchTextField;
	public void searchButton() {
		if(!searchTextField.getText().trim().isEmpty()) {
			DatabaseHelperMethods.searchBook(searchTextField.getText().trim());
		}
	}
	
	@FXML
    private TableView<Book> bookTable;
	@FXML
    private TableColumn<Book , String> isbnCol;
    @FXML
    private TableColumn<Book , String> titleCol;
    @FXML
    private TableColumn<Book , String> authorsCol;
    @FXML
    private TableColumn<Book , String> publisherCol;
    @FXML
    private TableColumn<Book , String> yearCol;
    @FXML
    private TableColumn<Book , String> categoryCol;
    @FXML
    private TableColumn<Book , String> priceCol;
    @FXML
    private TableColumn<Book , String> quantityCol;
    @FXML
    private TableColumn<Book , String> minQuantityCol;
    @FXML
    private TableColumn<Book , String> buyCol;
    @FXML
    private TableColumn<Book , String> deleteCol;
    
    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> orderIdCol;
    @FXML
    private TableColumn<Order, String> orderIsbnCol;
    @FXML
    private TableColumn<Order, String> orderTitleCol;
    @FXML
    private TableColumn<Order, String> orderPublisherCol;
    @FXML
    private TableColumn<Order, String> orderQuantityCol;
    @FXML
    private TableColumn<Order, String> orderConfirmCol;
    
    @FXML
    private TableView<Book> itemTable;
    @FXML
    private TableColumn<Book, String> itemIsbnCol;
    @FXML
    private TableColumn<Book, String> itemTitleCol;
    @FXML
    private TableColumn<Book, String> itemAuthorsCol;
    @FXML
    private TableColumn<Book, String> itemPublisherCol;
    @FXML
    private TableColumn<Book, String> itemYearCol;
    @FXML
    private TableColumn<Book, String> itemPriceCol;
    @FXML
    private TableColumn<Book, String> itemCategoryCol;
    @FXML
    private TableColumn<Book, String> itemRemoveCol;
    
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> usersUserNameCol;
    @FXML
    private TableColumn<User, String> usersFirstNameCol;
    @FXML
    private TableColumn<User, String> usersLastNameCol;
    @FXML
    private TableColumn<User, String> usersEmailCol;
    @FXML
    private TableColumn<User, String> usersPhoneCol;
    @FXML
    private TableColumn<User, String> usersAddressCol;
    @FXML
    private TableColumn<User, String> usersManagerCol;
    
    @FXML
    private TableView<Book> topBookTable;
    @FXML
    private TableColumn<Book, String> topBookIsbnCol;
    @FXML
    private TableColumn<Book, String> topBookTitleCol;
    
    @FXML
    private TableView<User> topUserTable;
    @FXML
    private TableColumn<User, String> topUserNameCol;

    @FXML
    private Label totalSellsLabel;
    
    @FXML
    private Label cartCounter;
    @FXML
    private Label totalPrice;
    @FXML
	 private ProgressIndicator indicator;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initMenu();
		initTable();
		
		DatabaseHelperMethods.loadBookData(bookTable , indicator);
		DatabaseHelperMethods.loadOrderData(orderTable);
		Cart.initCart(itemTable , cartCounter , totalPrice , indicator);
                
                DatabaseHelperMethods.loadUserTable(usersTable);
                DatabaseHelperMethods.loadTopBookTable(topBookTable);
                DatabaseHelperMethods.loadTopUserTable(topUserTable);
                totalSellsLabel.setText(DatabaseHelperMethods.getSells());
                
                	   
	}
	
	private void initTable() {
		initCol();
	}
	
	private void initCol() {
		isbnCol.setCellValueFactory(new PropertyValueFactory<Book , String>("isbn"));
		titleCol.setCellValueFactory(new PropertyValueFactory<Book , String>("title"));
		authorsCol.setCellValueFactory(new PropertyValueFactory<Book , String>("authors"));
		publisherCol.setCellValueFactory(new PropertyValueFactory<Book , String>("publisher"));
		yearCol.setCellValueFactory(new PropertyValueFactory<Book , String>("year"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Book , String>("price"));
		categoryCol.setCellValueFactory(new PropertyValueFactory<Book , String>("categoryMenu"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<Book , String>("quantity"));
		minQuantityCol.setCellValueFactory(new PropertyValueFactory<Book , String>("minQuantity"));
		buyCol.setCellValueFactory(new PropertyValueFactory<Book , String>("buyButton"));
		deleteCol.setCellValueFactory(new PropertyValueFactory<Book , String>("deleteButton"));
		
		orderIdCol.setCellValueFactory(new PropertyValueFactory<Order , String>("id"));
		orderIsbnCol.setCellValueFactory(new PropertyValueFactory<Order , String>("bookIsbn"));
		orderTitleCol.setCellValueFactory(new PropertyValueFactory<Order , String>("bookTitle"));
		orderPublisherCol.setCellValueFactory(new PropertyValueFactory<Order , String>("publisher"));
		orderQuantityCol.setCellValueFactory(new PropertyValueFactory<Order , String>("orderQuantity"));
		orderConfirmCol.setCellValueFactory(new PropertyValueFactory<Order , String>("confirmButton"));
		
		itemIsbnCol.setCellValueFactory(new PropertyValueFactory<Book , String>("isbn"));
		itemTitleCol.setCellValueFactory(new PropertyValueFactory<Book , String>("title"));
		itemAuthorsCol.setCellValueFactory(new PropertyValueFactory<Book , String>("authors"));
		itemPublisherCol.setCellValueFactory(new PropertyValueFactory<Book , String>("publisher"));
		itemYearCol.setCellValueFactory(new PropertyValueFactory<Book , String>("year"));
		itemPriceCol.setCellValueFactory(new PropertyValueFactory<Book , String>("price"));
		itemCategoryCol.setCellValueFactory(new PropertyValueFactory<Book , String>("title"));
		itemRemoveCol.setCellValueFactory(new PropertyValueFactory<Book , String>("removeButton"));
		
                usersUserNameCol.setCellValueFactory(new PropertyValueFactory<User , String>("userName"));
		usersFirstNameCol.setCellValueFactory(new PropertyValueFactory<User , String>("firstName"));
		usersLastNameCol.setCellValueFactory(new PropertyValueFactory<User , String>("lastName"));
		usersEmailCol.setCellValueFactory(new PropertyValueFactory<User , String>("email"));
		usersPhoneCol.setCellValueFactory(new PropertyValueFactory<User , String>("phone"));
		usersAddressCol.setCellValueFactory(new PropertyValueFactory<User , String>("address"));
		usersManagerCol.setCellValueFactory(new PropertyValueFactory<User , String>("isAdmin"));
		
		topBookIsbnCol.setCellValueFactory(new PropertyValueFactory<Book , String>("isbn"));
		topBookTitleCol.setCellValueFactory(new PropertyValueFactory<Book , String>("title"));
		
		topUserNameCol.setCellValueFactory(new PropertyValueFactory<User , String>("userName"));
                
		editableCol();
	}
	
	private void editableCol() {
		isbnCol.setCellFactory(TextFieldTableCell.forTableColumn());
		isbnCol.setOnEditCommit(e ->{
                        Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
			String oldValue = tempBook.getIsbn();                        
                        tempBook.setIsbn(e.getNewValue());                        
			if(!updateBook(oldValue , tempBook)){
                            tempBook.setIsbn(oldValue);
                            bookTable.refresh();
                            showError("update");
                        }
		});
		
		titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
		titleCol.setOnEditCommit(e ->{
                        Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
                        String oldValue = tempBook.getTitle();
                        tempBook.setTitle(e.getNewValue());
			if(!updateBook(tempBook.getIsbn() , tempBook)){
                            tempBook.setTitle(oldValue);
                            bookTable.refresh();
                            showError("update");
                        }
		});
		
		authorsCol.setCellFactory(TextFieldTableCell.forTableColumn());
		authorsCol.setOnEditCommit(e ->{
                        Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
                        String oldValue = tempBook.getAuthors();
                        if(!(e.getNewValue().equals(""))){
                           tempBook.setAuthors(e.getNewValue());
                            if(!updateBook(tempBook.getIsbn() , tempBook)){
                                tempBook.setAuthors(oldValue);
                                bookTable.refresh();
                                showError("update");
                            }  
                        }
                        else{
                            bookTable.refresh();
                            showError("update");
                        }
                       
		});
		
		publisherCol.setCellFactory(TextFieldTableCell.forTableColumn());
		publisherCol.setOnEditCommit(e ->{
			Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
                        String oldValue = tempBook.getPublisher();
                        tempBook.setPublisher(e.getNewValue());
                        if(!(e.getNewValue().equals(""))){
                           if(!updateBook(tempBook.getIsbn() , tempBook)){
                            tempBook.setPublisher(oldValue);
                            bookTable.refresh();
                            showError("update");
                            } 
                        }
                        else{
                            bookTable.refresh();
                            showError("update");
                        }
			
		});
		
		yearCol.setCellFactory(TextFieldTableCell.forTableColumn());
		yearCol.setOnEditCommit(e ->{
			Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
                        String oldValue = tempBook.getYear();
                        tempBook.setYear(e.getNewValue());
			if(!updateBook(tempBook.getIsbn() , tempBook)){
                            tempBook.setYear(oldValue);
                            bookTable.refresh();
                            showError("update");
                        }                        
		});
		
		priceCol.setCellFactory(TextFieldTableCell.forTableColumn());
		priceCol.setOnEditCommit(e ->{
			Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
                        String oldValue = tempBook.getPrice();
                        tempBook.setPrice(e.getNewValue());
			if(!updateBook(tempBook.getIsbn() , tempBook)){
                            tempBook.setPrice(oldValue);
                            bookTable.refresh();
                            showError("update");
                        }
		});
		
		quantityCol.setCellFactory(TextFieldTableCell.forTableColumn());
		quantityCol.setOnEditCommit(e ->{
			Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
                        String oldValue = tempBook.getQuantity();
                        tempBook.setQuantity(e.getNewValue());
			if(!updateBook(tempBook.getIsbn() , tempBook)){
                            tempBook.setQuantity(oldValue);
                            bookTable.refresh();
                            showError("update");
                        }                        
		});
		
		minQuantityCol.setCellFactory(TextFieldTableCell.forTableColumn());
		minQuantityCol.setOnEditCommit(e ->{
			Book tempBook = e.getTableView().getItems().get(e.getTablePosition().getRow());
                        String oldValue = tempBook.getMinQuantity();
                        tempBook.setMinQuantity(e.getNewValue());
			if(!updateBook(tempBook.getIsbn() , tempBook)){
                            tempBook.setMinQuantity(oldValue);
                            bookTable.refresh();
                            showError("update");
                        }                        
		});
		
		bookTable.setEditable(true);
	}
	
	private boolean updateBook(String oldIsbn , Book book) {
		return DatabaseHelperMethods.updateBook(oldIsbn , book);
	}
	
	@FXML
	private TextField creditCardNumber;
	@FXML
	private TextField creditCardDate;
	 
	public void confirmCart() {
		if(checkCreditData()) {
			Cart.confirmCart();
		}
	}
	
	private boolean checkCreditData() {
		if(creditCardNumber.getText().trim().isEmpty()) {
			showError("Credit Card Number");
			return false;
		}
		// TODO : make regex for credit card number
		if(!creditCardNumber.getText().trim().matches(intRegex)) {
			showError("Credit Card Number");
			return false;
		}
		if(creditCardDate.getText().trim().isEmpty()) {
			showError("Credit Card Expiry Date");
			return false;
		}
		// TODO : make regex for credit card date
		if(!checkCardDate(creditCardDate.getText().trim())) {
			showError("Credit Card Expiry Date");
			return false;
		}
		return true;
	}
        
        private boolean checkCardDate(String input){
            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy");
            
            
            try {  
                Date date = new Date(); 
                Date date1 = new SimpleDateFormat("MM/yy").parse(input);
                if(date1.after(date)){
                    return true;
                }
            
            } catch (ParseException ex) {
                return false;
//                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           
            return false;
            
        }
	
	public void cancelCart() {
		Cart.cancelCart();
	}
	
	public void reloadBookTable() {
		DatabaseHelperMethods.loadBookData(bookTable , indicator);
	}
        
        @FXML
    private Pane starterPage;
	@FXML
	private Pane addNewBookDiv;
	@FXML
    private Tab publisherOrdersTab;
	@FXML
    private Tab usersTab;
	@FXML
    private Tab analysisTab;
	
	@FXML
    private TextField loginUserName;
    @FXML
    private TextField loginPassword;

	public void login() {
		if(checkLoginValidity()) {
			if(DatabaseHelperMethods.loginCheck(loginUserName.getText().trim(), loginPassword.getText().trim())){
                           starterPage.setVisible(false);
                            if(!(DatabaseHelperMethods.loginAdminCheck(loginUserName.getText().trim()))){
                                addNewBookDiv.setVisible(false);
                            bookTable.setEditable(false);
                            deleteCol.setVisible(false);
                             //TODO : disable category edit
                            publisherOrdersTab.setDisable(true);
                            usersTab.setDisable(true);
                            analysisTab.setDisable(true); 
                            }
                            else{
                                 addNewBookDiv.setVisible(true);
                            bookTable.setEditable(true);
                            deleteCol.setVisible(true);
                             //TODO : disable category edit
                            publisherOrdersTab.setDisable(false);
                            usersTab.setDisable(false);
                            analysisTab.setDisable(false); 
                            }
                             DatabaseHelperMethods.loadProfile(profileUserName , profilePassword , profileFirstName , profileAddress , profilePhone
                            , profileLastName , profileEmail);
			
                        }
                        else{
                            showError("login");
                        }
		}
	}
	
	private boolean checkLoginValidity() {
		if(loginUserName.getText().trim().isEmpty()) {
			showError("User Name");
			return false;
		}
		if(!loginUserName.getText().trim().matches(stringRegex)) {
			showError("User Name");
			return false;
		}
		// TODO : make regex for password
		if(loginPassword.getText().trim().isEmpty()) {
			showError("Password");
			return false;
		}
		if(!loginPassword.getText().trim().matches(passwordRegex)) {
			showError("Password");
			return false;
		}
		return true;
	}
	
	@FXML
    private TextField signupUserName;
    @FXML
    private TextField signupPassword;
    @FXML
    private TextField signupLastName;
    @FXML
    private TextField signupFirstName;
    @FXML
    private TextField signupEmail;
    @FXML
    private TextField signupPhone;
    @FXML
    private TextField signupShippingAddress;
    
	public void signup() {
		if(checkSignupValidity()) {
			if(DatabaseHelperMethods.signupCheck(signupUserName.getText().trim() , signupPassword.getText().trim() , signupLastName.getText().trim() , signupFirstName.getText().trim() ,
                                signupEmail.getText().trim(), signupPhone.getText().trim(), signupShippingAddress.getText().trim())){
                           starterPage.setVisible(false);
                            addNewBookDiv.setVisible(false);
                            bookTable.setEditable(false);
                            deleteCol.setVisible(false);
                            //TODO : disable category edit
                            publisherOrdersTab.setDisable(true);
                            usersTab.setDisable(true);
                            analysisTab.setDisable(true);
                             DatabaseHelperMethods.loadProfile(profileUserName , profilePassword , profileFirstName , profileAddress , profilePhone
                            , profileLastName , profileEmail);
                        }
			
		}
	}
	
	private boolean checkSignupValidity() {
		if(signupUserName.getText().trim().isEmpty()) {
			showError("User Name");
			return false;
		}
		if(!signupUserName.getText().trim().matches(stringRegex)) {
			showError("User Name");
			return false;
		}
		if(signupPassword.getText().trim().isEmpty()) {
			showError("Password");
			return false;
		}
		// TODO : make regex for password
		if(!signupPassword.getText().trim().matches(passwordRegex)) {
			showError("Password");
			return false;
		}
		if(signupLastName.getText().trim().isEmpty()) {
			showError("Last Name");
			return false;
		}
		if(!signupLastName.getText().trim().matches(stringRegex)) {
			showError("Last Name");
			return false;
		}
		if(signupFirstName.getText().trim().isEmpty()) {
			showError("First Name");
			return false;
		}
		if(!signupFirstName.getText().trim().matches(stringRegex)) {
			showError("First Name");
			return false;
		}
		if(signupEmail.getText().trim().isEmpty()) {
			showError("Email");
			return false;
		}
		// TODO : make regex for email
		if(!signupEmail.getText().trim().matches(emailRegex)) {
			showError("Email");
			return false;
		}
		if(signupPhone.getText().trim().isEmpty()) {
			showError("Phone");
			return false;
		}
		// TODO : make regex for phone
		if(!signupPhone.getText().trim().matches(intRegex)) {
			showError("Phone");
			return false;
		}
		if(signupShippingAddress.getText().trim().isEmpty()) {
			showError("Shipping Address");
			return false;
		}
		// TODO : make regex for address
		if(!signupShippingAddress.getText().trim().matches(stringRegex)) {
			showError("Shipping Address");
			return false;
		}
		return true;
	}
	
	public void logout() {
		Cart.cancelCart();
		loginUserName.clear();
		loginPassword.clear();
		signupUserName.clear();
		signupPassword.clear();
		signupLastName.clear();
		signupFirstName.clear();
		signupEmail.clear();
		signupPhone.clear();
		signupShippingAddress.clear();
		// TODO : return tab pane to book table
		starterPage.setVisible(true);
	}
        
        
        
        
        
       @FXML
	private TextField addPublisherOrderIsbn;
	@FXML
	private TextField addPublisherOrderQuantity;
	public void addPublisherOrder() {
		if(!addPublisherOrderIsbn.getText().trim().isEmpty() && !addPublisherOrderQuantity.getText().trim().isEmpty()) {
			if(addPublisherOrderIsbn.getText().trim().matches(intRegex) && addPublisherOrderQuantity.getText().trim().matches(intRegex)) {
				if(!DatabaseHelperMethods.addPublisherOrder(addPublisherOrderIsbn.getText().trim(), addPublisherOrderQuantity.getText().trim())) {
					showError("Add publisher order");
				}
			}
		}
	}
	
	@FXML
    private TextField profileUserName;
    @FXML
    private TextField profilePassword;
    @FXML
    private TextField profileFirstName;
    @FXML
    private TextField profileAddress;
    @FXML
    private TextField profilePhone;
    @FXML
    private TextField profileLastName;
    @FXML
    private TextField profileEmail;
    
	public void updateProfile() {
		if(checkprofileValidity()) {
			DatabaseHelperMethods.upDateProfile(profileUserName.getText().trim() , profilePassword.getText().trim() 
					, profileFirstName.getText().trim(), profileAddress.getText().trim() , profilePhone.getText().trim() 
					, profileLastName.getText().trim(), profileEmail.getText().trim());
		}
	}
	
	private boolean checkprofileValidity() {
		if(profileUserName.getText().trim().isEmpty()) {
			showError("User Name");
			return false;
		}
		if(!profileUserName.getText().trim().matches(stringRegex)) {
			showError("User Name");
			return false;
		}
		if(profilePassword.getText().trim().isEmpty()) {
			showError("Password");
			return false;
		}
		// TODO : make regex for password
		if(!profilePassword.getText().trim().matches(passwordRegex)) {
			showError("Password");
			return false;
		}
		if(profileLastName.getText().trim().isEmpty()) {
			showError("Last Name");
			return false;
		}
		if(!profileLastName.getText().trim().matches(stringRegex)) {
			showError("Last Name");
			return false;
		}
		if(profileFirstName.getText().trim().isEmpty()) {
			showError("First Name");
			return false;
		}
		if(!profileFirstName.getText().trim().matches(stringRegex)) {
			showError("First Name");
			return false;
		}
		if(profileEmail.getText().trim().isEmpty()) {
			showError("Email");
			return false;
		}
		// TODO : make regex for email
		if(!profileEmail.getText().trim().matches(emailRegex)) {
			showError("Email");
			return false;
		}
		if(profilePhone.getText().trim().isEmpty()) {
			showError("Phone");
			return false;
		}
		// TODO : make regex for phone
		if(!profilePhone.getText().trim().matches(intRegex)) {
			showError("Phone");
			return false;
		}
		if(profileAddress.getText().trim().isEmpty()) {
			showError("Shipping Address");
			return false;
		}
		// TODO : make regex for address
		if(!profileAddress.getText().trim().matches(stringRegex)) {
			showError("Shipping Address");
			return false;
		}
		return true;
	}
        
        
        
}
