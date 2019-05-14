package model;

import application.DatabaseHelperMethods;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class User {
	
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private int admin;
	
	private CheckBox isAdmin = new CheckBox();
	
	public User(String userName, String password , String firstName, String lastName, String email, String phone, String address,
			int admin) {
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.admin = admin;
		
		this.initIsAdminCheckBox();
	}
	
        private void initIsAdminCheckBox() {
		if(this.admin == 1) {
			this.isAdmin.setSelected(true);
		}
		
		this.isAdmin.setOnAction(e ->{
			if(this.isAdmin.isSelected()) {
				this.admin = 1;
			}else {
				this.admin = 0;
			}
			// TODO update in db
			if(!DatabaseHelperMethods.promoteUser(this)) {
				if(this.isAdmin.isSelected()) {
					this.admin = 0;
					this.isAdmin.setSelected(false);
				}else {
					this.admin = 1;
					this.isAdmin.setSelected(true);
				}
			}
		});
	}
        


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}
        
        public CheckBox getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(CheckBox isAdmin) {
		this.isAdmin = isAdmin;
	}
}
