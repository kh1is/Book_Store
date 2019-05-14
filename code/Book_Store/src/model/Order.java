package model;

import application.DatabaseHelperMethods;
import javafx.scene.control.Button;

public class Order {
	private String id;
	private String bookIsbn;
	private String bookTitle;
	private String publisher;
	private String orderQuantity;
	
	private Button confirmButton;
	
	public Order(String id, String bookIsbn, String bookTitle, String publisher, String orderQuantity) {
		this.id = id;
		this.bookIsbn = bookIsbn;
		this.bookTitle = bookTitle;
		this.publisher = publisher;
		this.orderQuantity = orderQuantity;

		this.initConfirmButton();
	}
	
	private void initConfirmButton() {
		this.confirmButton = new Button("Confirm");
		// TODO : add image to the button
		this.confirmButton.setOnAction(e ->{
			DatabaseHelperMethods.confirmOrder(this);
                        DatabaseHelperMethods.refreshBookTable();
		});
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookIsbn() {
		return bookIsbn;
	}

	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(String orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Button getConfirmButton() {
		return confirmButton;
	}

	public void setConfirmButton(Button confirmButton) {
		this.confirmButton = confirmButton;
	}
	
	
}
