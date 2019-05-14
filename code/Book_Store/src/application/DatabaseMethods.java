/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Book;
import model.Order;
import model.User;

/**
 *
 * @author khalil
 */
public class DatabaseMethods {

    private static String DB_URL = "jdbc:mysql://localhost:3306/Database_project";
    private static String DB_USER = "khalil";
    private static String DB_PASSWD = "ahlawy74";

    private static ObservableList<Book> bookData = FXCollections.observableArrayList();
    private static ObservableList<Order> orderData = FXCollections.observableArrayList();
    
    private static String price;
    private static ObservableList<Book> top_ten_books = FXCollections.observableArrayList();
    private static ObservableList<User> top_five_user = FXCollections.observableArrayList();
    private static ObservableList<User> userData = FXCollections.observableArrayList();
    private static int user_id=0;
    
    public static boolean search_book(String word){
        
        bookData.clear();
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

         try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select ISBN, title, publisher_name, year, price, category_name, quantity, min_quantity, author_name, CATEGORY.Id\n" +
                "from BOOK,PUBLISHER,CATEGORY,AUTHOR where BOOK.publisher_Id = PUBLISHER.Id and \n" +
                "BOOK.category_Id = CATEGORY.Id and AUTHOR.ISBN_num = BOOK.ISBN and \n" +
                "(ISBN = ? or title = ? or publisher_name = ? or year = ? or price = ? or \n" +
                "category_name = ? or quantity = ? or min_quantity = ? or author_name = ?);");
            
            statement.setString(1, word);
            statement.setString(2, word);
            statement.setString(3, word);
            statement.setString(4, word);
            statement.setString(5, word);
            statement.setString(6, word);
            statement.setString(7, word);
            statement.setString(8, word);
            statement.setString(9, word);
            
            resultSet=statement.executeQuery();
   
            while(resultSet.next()){
             
                
               if(bookData.size() == 0){
                   Book b = new Book(Integer.toString(resultSet.getInt(1)), resultSet.getString(2), resultSet.getString(9)
                               , resultSet.getString(3), Integer.toString(resultSet.getInt(4))
                               , resultSet.getString(6), Float.toString(resultSet.getFloat(5)), 
                               Integer.toString(resultSet.getInt(7)),Integer.toString(resultSet.getInt(8)),
                                resultSet.getInt(10));
                       bookData.add(b);
                       
               }
               else{
                    for(int i=0;i<bookData.size();i++){
                        if(resultSet.getInt(1) == Integer.valueOf(bookData.get(i).getIsbn())){
                            setAuthorsList(resultSet.getString(9),bookData.get(i));
                            break;
                        }
                        else if(i == bookData.size()-1){
                            // create new object and add it to arrlist
                            Book b = new Book(Integer.toString(resultSet.getInt(1)), resultSet.getString(2), resultSet.getString(9)
                                    , resultSet.getString(3), Integer.toString(resultSet.getInt(4))
                                    , resultSet.getString(6), Float.toString(resultSet.getFloat(5)), 
                                    Integer.toString(resultSet.getInt(7)),Integer.toString(resultSet.getInt(8)),
                                    resultSet.getInt(10));
                            bookData.add(b);
                            break;
                        }
                    }
               }
               
            }
        }catch(SQLException ex){
        }
           finally{
            
            try {
            
            resultSet.close();
            statement.close();
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
         
        return true;
    }
    
    public static boolean select_all_books(){
        
        bookData.clear();
        
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement tmpStatement = null;
        ResultSet resultSet = null;
        ResultSet tmpResultSet = null;
        
        int flag=0;
        
         try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select ISBN, title, publisher_name, year, price, category_name, quantity, min_quantity, CATEGORY.Id\n" +
            "from BOOK,PUBLISHER,CATEGORY where BOOK.publisher_Id = PUBLISHER.Id and BOOK.category_Id = CATEGORY.Id;");
            
            resultSet=statement.executeQuery();
            
            
            tmpStatement =connection.prepareStatement("select author_name from AUTHOR where AUTHOR.ISBN_num = ?;");
            
            
            while(resultSet.next()){
                
                if(resultSet.getString(1) != null){
                    flag = 1;
                
                    // create new object and add it to arrlist

                    Book b = new Book(Integer.toString(resultSet.getInt(1)), resultSet.getString(2), ""
                                    , resultSet.getString(3), Integer.toString(resultSet.getInt(4))
                                    , resultSet.getString(6), Float.toString(resultSet.getFloat(5)), 
                                    Integer.toString(resultSet.getInt(7)),Integer.toString(resultSet.getInt(8)),
                                     resultSet.getInt(9));


                   tmpStatement.setInt(1, resultSet.getInt(1));

                   tmpResultSet=tmpStatement.executeQuery();

                   while(tmpResultSet.next()){
                       //set to author list in object
                        setAuthorsList(tmpResultSet.getString(1),b);
                   }

                   bookData.add(b);
               }
    
            }
        }catch(SQLException ex){
        }
        finally{
            
            try {
            
            resultSet.close();    
            statement.close();
            if(flag == 1){
                tmpResultSet.close();
                tmpStatement.close();
            }
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
         
         return true;
    }
   
    
    public static boolean insert_book(Book b){
        
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement tmpStatement = null;
        ResultSet tmpResultSet = null;
        PreparedStatement tmpStatement2 = null;
        PreparedStatement tmpStatement3 = null;
        PreparedStatement tmpStatement4 = null;
        ResultSet tmpResultSet4 = null;
        
        int flag=0;
        int publisher_id=0;
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("insert into BOOK values(?, ?, ?, ?, ?, ?, ?, ?);");
            
            tmpStatement =connection.prepareStatement("select Id, publisher_name from PUBLISHER;");

            tmpResultSet=tmpStatement.executeQuery();
            
            while(tmpResultSet.next()){
                  
                if(tmpResultSet.getString(2).equalsIgnoreCase(b.getPublisher())){
                    
                    publisher_id = tmpResultSet.getInt(1);
                    flag = 1;
                    break;
                }
            }
            
            if(flag == 0){
                
                tmpStatement2=connection.prepareStatement("insert into PUBLISHER(publisher_name, publisher_address, publisher_telephone) values(?, \"add4\", \"t4\");");
                tmpStatement2.setString(1,b.getPublisher());
                tmpStatement2.executeUpdate();
                
                tmpStatement4 =connection.prepareStatement("select Id from PUBLISHER where PUBLISHER.publisher_name=?;");
                tmpStatement4.setString(1,b.getPublisher());
                tmpResultSet4=tmpStatement4.executeQuery();
                while(tmpResultSet4.next()){
                    publisher_id = tmpResultSet4.getInt(1);
                }
            }
            
            
            statement.setString(1, b.getIsbn());
            statement.setString(2, b.getTitle());
            statement.setInt(3, publisher_id);
            statement.setString(4, b.getYear());
            statement.setString(5, b.getPrice());
            statement.setInt(6, b.getCategoryId());
            statement.setString(7, b.getQuantity());
            statement.setString(8, b.getMinQuantity());
            
            
            
            statement.executeUpdate();
            
            tmpStatement3=connection.prepareStatement("insert into AUTHOR values(?, ?);");
            
            
            for(int i =0;i<getAuthorsList(b).size(); i++){
                tmpStatement3.setString(1, b.getIsbn());
                tmpStatement3.setString(2, getAuthorsList(b).get(i));
                tmpStatement3.executeUpdate();
            }
            
            
       
        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
            statement.close();
            if(flag==0){
                tmpStatement2.close();
                tmpResultSet4.close();
                tmpStatement4.close();
            }
            tmpStatement3.close();
            tmpResultSet.close();
            tmpStatement.close();
            connection.close();
            
            
            } 
            catch (SQLException ex) {
                
            }
        }
        return true;
    }
    
    
    public static boolean update_book(Book b,String isbn){
        //there are "object and id" sent
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement tmpStatement = null;
        ResultSet tmpResultSet = null;
        PreparedStatement tmpStatement2 = null;
        PreparedStatement tmpStatement3 = null;
        ResultSet tmpResultSet3 = null;
        PreparedStatement tmpStatement4 = null;
        ResultSet tmpResultSet4 = null;
        PreparedStatement tmpStatement5 = null;
        PreparedStatement tmpStatement6 = null;
        PreparedStatement tmpStatement7 = null;
        ResultSet tmpResultSet7 = null;
        
        int flag=0;
        int flag2=0;
        int flag3=0;
        int flag4=0;
        
        int publisher_id=0;
        String publisher_name = null;
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("update BOOK\n" +
                "set ISBN=?, title= ?, publisher_Id= ?, year=?, price=?, category_Id=?, quantity=?, min_quantity=?\n" +
                "where BOOK.ISBN=?;");
            
            tmpStatement3=connection.prepareStatement("select publisher_Id from BOOK where BOOK.ISBN=?;");
            
            tmpStatement3.setString(1, isbn);
            
            tmpResultSet3=tmpStatement3.executeQuery();
            
            while(tmpResultSet3.next()){
                publisher_id = tmpResultSet3.getInt(1);
            }
            
           
            tmpStatement4=connection.prepareStatement("select publisher_name from PUBLISHER where PUBLISHER.Id=?;");
            
            tmpStatement4.setInt(1, publisher_id);
            
            tmpResultSet4=tmpStatement4.executeQuery();
            
            while(tmpResultSet4.next()){
                publisher_name = tmpResultSet4.getString(1);
            }
            if(!(publisher_name.equalsIgnoreCase(b.getPublisher()))){
                
                flag3 = 1;
                tmpStatement =connection.prepareStatement("select Id, publisher_name from PUBLISHER;");

                tmpResultSet=tmpStatement.executeQuery();

                while(tmpResultSet.next()){

                    if(tmpResultSet.getString(2).equalsIgnoreCase(b.getPublisher())){
                        publisher_id = tmpResultSet.getInt(1);
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0){
                    tmpStatement2=connection.prepareStatement("insert into PUBLISHER(publisher_name, publisher_address, publisher_telephone) values(?, \"add4\", \"t4\");");
                    tmpStatement2.setString(1,b.getPublisher());
                    tmpStatement2.executeUpdate();

                    tmpStatement7 =connection.prepareStatement("select Id from PUBLISHER where PUBLISHER.publisher_name=?;");
                    tmpStatement7.setString(1,b.getPublisher());
                    tmpResultSet7=tmpStatement7.executeQuery();
                    while(tmpResultSet7.next()){
                        publisher_id = tmpResultSet7.getInt(1);
                    }
                    flag2 = 1;
                }
            }
            
            statement.setString(1, b.getIsbn());
            statement.setString(2, b.getTitle());
            statement.setInt(3, publisher_id);
            statement.setString(4, b.getYear());
            statement.setString(5, b.getPrice());
            statement.setInt(6, b.getCategoryId());
            statement.setString(7, b.getQuantity());
            statement.setString(8, b.getMinQuantity());
            statement.setString(9, isbn);
            
            
            if(statement.executeUpdate() == 1){
                flag4 = 1;
                
                tmpStatement5=connection.prepareStatement("delete from AUTHOR where AUTHOR.ISBN_num=?;");
                tmpStatement5.setString(1, isbn);
                tmpStatement5.executeUpdate();

                tmpStatement6=connection.prepareStatement("insert into AUTHOR values(?, ?);");

                for(int i =0;i<getAuthorsList(b).size(); i++){
                    tmpStatement6.setString(1, b.getIsbn());
                    tmpStatement6.setString(2, getAuthorsList(b).get(i));
                    tmpStatement6.executeUpdate();
                }
            }
            
            
            
       
        }catch(SQLException ex){
        }
        finally{
            
            try {
           if(flag2 == 1){ 
            tmpResultSet7.close();
            tmpStatement7.close();
            tmpStatement2.close();
           }
           if(flag4 == 1){
            tmpStatement6.close();
            tmpStatement5.close();
           }
            tmpStatement4.close();
            tmpResultSet4.close();
            tmpResultSet3.close();
            statement.close();
            
            tmpStatement3.close();
            if(flag3 == 1){
                tmpResultSet.close();
                tmpStatement.close();
            }
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
        if(flag4 == 1){
            return true;
        }
        else{
            return false;
        }
    }
    
    
    public static boolean confirm_order(String id){
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("delete from ORDERS where ORDERS.Id=?;");
            
            statement.setString(1, id);
            statement.executeUpdate();
       
        }catch(SQLException ex){
        }
        finally{
            
            try {
            
            statement.close();
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
        return true;
    }
    
    /*-----------delete book method added-----------------------*/
    
    public static boolean delete_book(String isbn){
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("delete from BOOK where BOOK.ISBN = ?;");
            
            statement.setString(1, isbn);
            statement.executeUpdate();
       
        }catch(SQLException ex){
        }
        finally{
            
            try {
            
            statement.close();
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
        return true;
    }
    
    public static boolean select_all_orders(){
        
        orderData.clear();
        
        Connection connection = null;
        PreparedStatement statement = null;       
        ResultSet resultSet = null;

         try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select ORDERS.ISBN, ORDERS.Id, ORDERS.quantity, PUBLISHER.publisher_name, BOOK.title from ORDERS,PUBLISHER,BOOK where BOOK.publisher_Id = PUBLISHER.Id and BOOK.ISBN = ORDERS.Isbn\n" +
"              and  PUBLISHER.Id = ORDERS.publisher_Id;");
            
            resultSet=statement.executeQuery();
            
            
            while(resultSet.next()){
                
               
                Order o = new Order(Integer.toString(resultSet.getInt(2)), Integer.toString(resultSet.getInt(1))
                               , resultSet.getString(5), resultSet.getString(4)
                               , Integer.toString(resultSet.getInt(3)));
                
                
                orderData.add(o);
                
            }
            
        }catch(SQLException ex){
        }
        finally{
            
            try {
            
            resultSet.close();
            statement.close();
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
         
        return true;
    }
    
    public static boolean request_quantity_books(String isbn, String quantity){
        
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement tmpStatement = null;
        PreparedStatement tmpStatement2 = null;
        ResultSet tmpResultSet2 = null;
        
        float price = 0;
        int flag=0;
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
            
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            
            
            statement=connection.prepareStatement("update BOOK set BOOK.quantity = BOOK.quantity - ? where BOOK.ISBN=?;");
            
            statement.setString(1, quantity);
            statement.setString(2, isbn);
            
            if(statement.executeUpdate() == 1){
                flag = 1;
                tmpStatement2=connection.prepareStatement("select BOOK.price from BOOK where BOOK.ISBN = ?;");
                tmpStatement2.setString(1, isbn);
                tmpResultSet2 = tmpStatement2.executeQuery();
                
                while(tmpResultSet2.next()){
                   price = tmpResultSet2.getFloat(1);
                }
                
                price = price * Float.valueOf(quantity);
                
                tmpStatement=connection.prepareStatement("insert into sales(Isbn,Date,user_Id,price) values (?,?,?,?);");
                
                tmpStatement.setString(1, isbn);
                tmpStatement.setString(2, dtf.format(localDate));
                tmpStatement.setInt(3, user_id);
                tmpStatement.setFloat(4, price);
                
                tmpStatement.executeUpdate();
                
            }
       
        }catch(SQLException ex){
        }
        finally{
            
            try {
            
            if(flag == 1){
                tmpStatement.close();
                tmpStatement2.close();
                tmpResultSet2.close();
            }
            statement.close();
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
        if(flag==1){
            return true;
        }
        else{
            return false;
        }
    }
    
    private static ArrayList<String> getAuthorsList(Book b) {
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(b.getAuthors().split(",")));
            return myList;
	}

    private static void setAuthorsList(String author, Book b) {
            StringBuilder s = new StringBuilder();
            
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(b.getAuthors().split(",")));
            myList.add(author);
            
            for(int i=0;i<myList.size();i++){
                s.append(myList.get(i));
                if(i<myList.size()-1){
                    s.append(',');
                }
            }
            
            b.setAuthors(s.toString());
    }
    
    public static ObservableList<Book> get_book_data(){
        return bookData;
    }
    
    public static ObservableList<Order> get_order_data(){
        return orderData;
    }
    
    
    public static boolean sign_up(User user){
        
        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement tmpStatement = null;
        ResultSet tmpResultSet = null;
        
        int flag=0;
        
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("insert into managers_and_customers(user_name,password,admin,first_name,last_name,e_mail,phone_number,shipping_address)\n" +
            "values (?,?,?,?,?,?,?,?);");
            
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getAdmin());
            statement.setString(4, user.getFirstName());
            statement.setString(5, user.getLastName());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getPhone());
            statement.setString(8, user.getAddress());

              if(statement.executeUpdate() == 1){
                    tmpStatement = connection.prepareStatement("select user_Id from managers_and_customers where user_name = ? and password = ?;");
                  
                    tmpStatement.setString(1, user.getUserName());
                    tmpStatement.setString(2, user.getPassword());

                    tmpResultSet=tmpStatement.executeQuery();
                    while(tmpResultSet.next()){
                            user_id = tmpResultSet.getInt(1);
                    }
                    flag = 1;
                    
              }
            

       
        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
                statement.close();
                if(flag==1){
                    tmpStatement.close();
                    tmpResultSet.close();
                }
                connection.close();
            
            
            } 
            catch (SQLException ex) {
                
            }
        }
        
        if(flag==1){
            return true;
        }
        else{
            return false;
        }
        
    } 
    
    public static boolean update_user(User user){
        
        
        Connection connection = null;
        PreparedStatement statement = null;

        
        int flag=0;
        
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("update managers_and_customers set user_name = ?, password = ? , first_name = ?, last_name = ?, e_mail = ?, phone_number = ?, shipping_address = ? where user_Id = ?;");
            
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());

            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhone());
            statement.setString(7, user.getAddress());
            statement.setInt(8, user_id);

              if(statement.executeUpdate() == 1){

                    flag = 1;
                    
              }

            

       
        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
                statement.close();

                connection.close();
            
            
            } 
            catch (SQLException ex) {
                
            }
        }
        
        if(flag==1){
            return true;
        }
        else{
            return false;
        }
        
    } 
    
    public static boolean log_in(String username,String password){
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        int flag=0;
        
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select user_Id from managers_and_customers where user_name = ? and password = ?;");
            
            statement.setString(1, username);
            statement.setString(2, password);
            
            resultSet=statement.executeQuery();
                while(resultSet.next()){
                   
                    if(resultSet.getString(1) != null){
                        user_id = resultSet.getInt(1);
                        flag = 1;
                    }
                }
       
        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
                resultSet.close();
                statement.close();
                connection.close();
            
            
            } 
            catch (SQLException ex) {
                
            }
        }
        
        if(flag==1){
            return true;
        }
        else{
            return false;
        }
        
        
    } 

    public static boolean sales_analaysis(){
        
        top_ten_books.clear();
        top_five_user.clear();
        price = "0";
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        PreparedStatement tmpStatement = null;
        ResultSet tmpResultSet = null;
        PreparedStatement tmpStatement2 = null;
        ResultSet tmpResultSet2 = null;
        PreparedStatement tmpStatement3 = null;
        ResultSet tmpResultSet3 = null;
        PreparedStatement tmpStatement4 = null;
        ResultSet tmpResultSet4 = null;
        
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        LocalDate date1 = localDate.minus(1, ChronoUnit.MONTHS);
        LocalDate date2 = localDate.minus(3, ChronoUnit.MONTHS);
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select sum(sales.price) from sales where sales.Date > ?;");
            
            statement.setString(1, dtf.format(date1));
            
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                price = resultSet.getString(1);
            }
            
            
            
            tmpStatement=connection.prepareStatement("select sales.user_Id,sum(sales.price) as payments from sales where sales.Date > ? \n" +
                "group by (sales.user_Id) order by payments DESC;");
            
            tmpStatement.setString(1, dtf.format(date2));

            tmpResultSet = tmpStatement.executeQuery();
            
            tmpStatement2=connection.prepareStatement("select user_name,password,admin,first_name,last_name,e_mail,phone_number,shipping_address from managers_and_customers\n" +
                "where user_Id = ?;");
            
            int counter = 0;
            while(tmpResultSet.next()){
                tmpStatement2.setString(1, tmpResultSet.getString(1));
                if(counter < 5){
                    tmpResultSet2 = tmpStatement2.executeQuery();
                    while(tmpResultSet2.next()){
                            if(tmpResultSet2.getString(1) != null){
                            User user = new User(tmpResultSet2.getString(1),tmpResultSet2.getString(2),
                            tmpResultSet2.getString(4),tmpResultSet2.getString(5),tmpResultSet2.getString(6),
                            tmpResultSet2.getString(7),tmpResultSet2.getString(8),tmpResultSet2.getInt(3));

                            top_five_user.add(user);
                            counter++;
                        }
                    }
                    
                }
                else{
                    break;
                }
            }

            
            tmpStatement3=connection.prepareStatement("select sales.Isbn,count(sales.Id) as num from sales where sales.Date > ? \n" +
                "group by (sales.Isbn) order by num DESC;");
            
            tmpStatement3.setString(1, dtf.format(date2));

            tmpResultSet3 = tmpStatement3.executeQuery();
            
            tmpStatement4=connection.prepareStatement("select * from BOOK where ISBN = ?;");
            
            int counter2 = 0;
            while(tmpResultSet3.next()){
                tmpStatement4.setString(1, tmpResultSet3.getString(1));
                if(counter2 < 10){
                    tmpResultSet4 = tmpStatement4.executeQuery();
                    while(tmpResultSet4.next()){
                        if(tmpResultSet4.getString(1) != null){
                            Book book = new Book(tmpResultSet4.getString(1),tmpResultSet4.getString(2),"",
                            tmpResultSet4.getString(3),tmpResultSet4.getString(4),"",tmpResultSet4.getString(5),
                            tmpResultSet4.getString(7),tmpResultSet4.getString(8),tmpResultSet4.getInt(6));

                            top_ten_books.add(book);
                            counter2++;
                        }
                    }
                    
                }
                else{
                    break;
                }
            }

       
        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
                tmpResultSet.close();
                tmpStatement.close();
                tmpResultSet2.close();
                tmpStatement2.close();
                tmpResultSet3.close();
                tmpStatement3.close();
                tmpResultSet4.close();
                tmpStatement4.close();
                resultSet.close();
                statement.close();
                connection.close();
            
            
            } 
            catch (SQLException ex) {
                
            }
        }
        
        
        return true;
        
    }
    
    public static boolean orders_from_publisher(String isbn, String quantity){
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        PreparedStatement tmpStatement = null;
        
        
        int flag=0;
        int publisher_id=0;
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select publisher_Id from BOOK where ISBN = ?;");
            
            statement.setString(1, isbn);

            resultSet = statement.executeQuery();
                        
            while(resultSet.next()){
                
                publisher_id = resultSet.getInt(1);
                
            }
            
            tmpStatement = connection.prepareStatement("insert into ORDERS(Isbn, publisher_Id, quantity) values(?, ?, ?);");
            
            tmpStatement.setString(1, isbn);
            tmpStatement.setInt(2, publisher_id);
            tmpStatement.setString(3, quantity);
            
            if(tmpStatement.executeUpdate() == 1){
                flag = 1;
            }
       
        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
                resultSet.close();
                statement.close();
                tmpStatement.close();
                connection.close();
                        
            } 
            catch (SQLException ex) {
                
            }
        }
        
        if(flag==1){
            return true;
        }
        else{
            return false;
        }
    }
    
    
    public static String get_total_sales(){
        return price;
    }
    
    public static ObservableList<User> get_top_five_user(){
        return top_five_user;
    }
        
    public static ObservableList<Book> get_top_ten_books(){
        return top_ten_books;
    }
    
    public static boolean select_all_users(){
        
        userData.clear();
        
        Connection connection = null;
        PreparedStatement statement = null;       
        ResultSet resultSet = null;

         try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select * from managers_and_customers;");
            
            resultSet=statement.executeQuery();
            
            
            while(resultSet.next()){
               
                User user = new User(resultSet.getString(2),resultSet.getString(3),
                resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),
                resultSet.getString(8),resultSet.getString(9),resultSet.getInt(4));
                
                
                userData.add(user);
                
            }
            
        }catch(SQLException ex){
        }
        finally{
            
            try {
            
            resultSet.close();
            statement.close();
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
         
        return true;
    }
    
    public static boolean check_admin(String username){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        int flag=0;
               
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select managers_and_customers.admin from managers_and_customers where managers_and_customers.user_name = ?;");
            
            statement.setString(1, username);

            resultSet=statement.executeQuery();
            
            
            while(resultSet.next()){
                if(resultSet.getInt(1) == 1){
                    flag = 1;
                }
                
            }
             
        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
                statement.close();

                connection.close();
            
            
            } 
            catch (SQLException ex) {
                
            }
        }
        
        if(flag==1){
            return true;
        }
        else{
            return false;
        }
    }
    
    public static ObservableList<User> get_user_data(){
        return userData;
    }
    
    public static User get_user_profile(){
        Connection connection = null;
        PreparedStatement statement = null;       
        ResultSet resultSet = null;
        
        User user = new User("","","","","","","",0);

         try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("select * from managers_and_customers where user_Id = ?;");
            
            statement.setInt(1, user_id);
                    
            resultSet=statement.executeQuery();
            
            
            while(resultSet.next()){
                              
                user.setUserName(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setFirstName(resultSet.getString(5));
                user.setLastName(resultSet.getString(6));
                user.setEmail(resultSet.getString(7));
                user.setPhone(resultSet.getString(8));
                user.setAddress(resultSet.getString(9));
                user.setAdmin(resultSet.getInt(4));
                
            }
            
        }catch(SQLException ex){
        }
        finally{
            
            try {
            
            resultSet.close();
            statement.close();
            connection.close();
            
            } 
            catch (SQLException ex) {
            }
        }
         
        return user;
    }
    
    public static boolean update_user_admin(User user){
        
        
        Connection connection = null;
        PreparedStatement statement = null;

        
        int flag=0;
        
        
        try{
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            
            statement=connection.prepareStatement("update managers_and_customers set admin=?  where user_name = ?;");
            
            statement.setInt(1, user.getAdmin());
            statement.setString(2, user.getUserName());

            if(statement.executeUpdate() == 1){

                flag = 1;
                    
            }


        }catch(SQLException ex){
            
        }
        finally{
            
            try {
            
                statement.close();

                connection.close();
            
            
            } 
            catch (SQLException ex) {
                
            }
        }
        
        if(flag==1){
            return true;
        }
        else{
            return false;
        }
        
    } 
    
}
