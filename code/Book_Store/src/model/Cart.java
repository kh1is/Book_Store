package model;

import java.util.ArrayList;
import java.lang.Integer;

import application.Controller;
import application.DatabaseHelperMethods;
import application.DatabaseMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;

public class Cart {
	
	private static ObservableList<Book> cartData = FXCollections.observableArrayList();
	private static TableView<Book> cartTable = new TableView<Book>();
	
	private static Label cartCounter;
	private static Label totalPrice;
	private static ProgressIndicator progressIndicator;
	
	public static void initCart(TableView<Book> table , Label counter , Label price , ProgressIndicator indicator) {
		cartTable = table;
		cartCounter = counter;
		totalPrice = price;
		progressIndicator = indicator;
	}
	
	public static void addCartBook(Book book) {
		System.out.println("Adding book to cart");
		progressIndicator.setProgress(-1);
		
		cartData.add(book);
		
		cartTable.setItems(cartData);
		
		updateCartCounter();
		updateTotalPrice();
		
		progressIndicator.setProgress(1);
		System.out.println("Adding book to cart has finished");
	}
	
	public static void removeCartBook(Book book) {
		System.out.println("Removing book from cart");
		progressIndicator.setProgress(-1);
		
		cartData.remove(book);
		
		cartTable.setItems(cartData);
		
		updateCartCounter();
		updateTotalPrice();
		
		progressIndicator.setProgress(1);
		System.out.println("Removing book from cart has finished");
	}
	
	private static void updateCartCounter() {
		cartCounter.setText(String.valueOf(cartData.size()));
	}
	
	private static void updateTotalPrice() {
		float price = 0;
		for(int i = 0 ; i < cartData.size() ; i++) {
			price = price + Float.valueOf(cartData.get(i).getPrice());                        
		}
		totalPrice.setText(String.valueOf(price));
	}
	
	public static void confirmCart() {
		System.out.println("Confirming cart");
		progressIndicator.setProgress(-1);
		
                //int i=0;
		while(cartData.size()>0){
                   
                    if(DatabaseMethods.request_quantity_books(cartData.get(0).getIsbn(),"1")){
                        cartData.remove(cartData.get(0));
                        //i++;
                    }
                    
                }
		
		cartTable.setItems(cartData);
		
		updateCartCounter();
		updateTotalPrice();
                
                DatabaseHelperMethods.refreshOrderTable();
		
		progressIndicator.setProgress(1);
		System.out.println("Confirming cart is done");
	}
	
	public static void cancelCart() {
		System.out.println("Canceling cart");
		progressIndicator.setProgress(-1);
		
		cartData.clear();
		
		cartTable.setItems(cartData);
		
		updateCartCounter();
		updateTotalPrice();
		
		progressIndicator.setProgress(1);
		System.out.println("Canceling cart is done");
	}

}
