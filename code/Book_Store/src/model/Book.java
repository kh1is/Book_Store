package model;

import application.Controller;
import application.DatabaseHelperMethods;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class Book {
	private String isbn;
	private String title;
	private String authors;
	private String publisher;
	private String year;
	private String price;
	private String category;
	private String quantity;
	private String minQuantity;
	private int categoryId;

	private Button buyButton;
	private Button deleteButton;
	private Button removeButton;
	
	private MenuButton categoryMenu = new MenuButton("Category");
	private MenuItem scienceItem = new MenuItem("Science");
	private MenuItem artItem = new MenuItem("Art");
	private MenuItem religionItem = new MenuItem("Religion");
	private MenuItem historyItem = new MenuItem("History");
	private MenuItem geographyItem = new MenuItem("Geography");

	public Book(String isbn, String title, String authors, String publisher, String year, String category, String price,
			String quantity, String minQuantity , int categoryId) {
		this.isbn = isbn;
		this.title = title;
		this.authors = authors;
		this.publisher = publisher;
		this.year = year;
		this.price = price;
		this.category = category;
		this.quantity = quantity;
		this.minQuantity = minQuantity;
		this.categoryId = categoryId;
		
		this.initBuyButton();
		this.initDeleteButton();
		this.initRemoveButton();
		this.initMenu();
	}
	
	private void initBuyButton() {
		this.buyButton = new Button("buy");	
		// TODO : add image to the button
		this.buyButton.setOnAction(e ->{
                        if(!(this.quantity.equals("0"))){
                            Cart.addCartBook(this);
                        }
		});
	}
	
	private void initDeleteButton() {
		this.deleteButton = new Button("Delete");	
		// TODO : add image to the button
		this.deleteButton.setOnAction(e ->{
			DatabaseHelperMethods.deleteBook(this);
		});
	}
	
	private void initRemoveButton() {
		this.removeButton = new Button("Remove");
		// TODO : add image to the button
		this.removeButton.setOnAction(e->{
			Cart.removeCartBook(this);
		});
	}
	
	private void initMenu() {
            categoryMenu.getItems().addAll(this.scienceItem , this.artItem , this.religionItem , this.historyItem , this.geographyItem);
            
            categoryMenu.setText(category);
                
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                String oldCategory = category;
                int oldCategoryId = categoryId;
                
            	if(e.getSource() == scienceItem) {
            		setCategory("Science");
            		setCategoryId(1);
            	}
				if(e.getSource() == artItem) {
					setCategory("Art");
            		setCategoryId(2);
				}
				if(e.getSource() == religionItem) {
					setCategory("Religion");
            		setCategoryId(3);
				}
				if(e.getSource() == historyItem) {
					setCategory("History");
            		setCategoryId(4);
				}
				if(e.getSource() == geographyItem) {
					setCategory("Geography");
            		setCategoryId(5);
				}
                                
                                if(!updateBook()){
                                    setCategory(oldCategory);
                                    setCategoryId(oldCategoryId); 
                                }
				categoryMenu.setText(category);
            } 
        };
        
        this.scienceItem.setOnAction(event);
        this.artItem.setOnAction(event);
        this.religionItem.setOnAction(event);
        this.historyItem.setOnAction(event);
        this.geographyItem.setOnAction(event);
    } 
	
	private boolean updateBook() {
		return DatabaseHelperMethods.updateBook(isbn , this);
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getMinQuantity() {
		return minQuantity;
	}

	public void setMinQuantity(String minQuantity) {
		this.minQuantity = minQuantity;
	}
	
	public Button getBuyButton() {
		return buyButton;
	}

	public void setBuyButton(Button buyButton) {
		this.buyButton = buyButton;
	}
	
	public Button getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}
	
	public Button getRemoveButton() {
		return removeButton;
	}

	public void setRemoveButton(Button removeButton) {
		this.removeButton = removeButton;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int id) {
		this.categoryId = id;
	}
	
	public MenuButton getCategoryMenu() {
		return categoryMenu;
	}
	
	public void setCategoryMenu(MenuButton button) {
		this.categoryMenu = button;
	}
}
