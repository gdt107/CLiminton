
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import Objects.Cart;
import Objects.MyTransaction;
import Objects.Product;
import Objects.TransactionDetail;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Main extends Application{
	
	Stage loginStage, homeStage, registerStage, cartStage, transactionStage,historyStage, manageStage, viewAdminHistoryStage;

	Scene loginScene;
	TextField tfEmail;
	String loggedInEmail;
	String loggedInUserID;
	
	Scene registerScene;
	
	Scene homeScene;
	
	TableView<Product> tableHome;
	ObservableList<Product> obsProduct;
	Vector<Product> products = new Vector<>();
	Label productList, productName, productBrand, price, totalPrice;
	String tempProductID = null;
	Product selectedProduct;
	
	Scene cartScene;
	TableView<Cart> tableCart;
	ObservableList<Cart> listCart;
	Vector<Cart> carts = new Vector<>();
	 Cart cart;
	
	Scene transactionCard;
	int deliveryInsurance;
	
	Scene historyScene;
	TableView<MyTransaction> transactionTable;
	Vector<MyTransaction> myTransactions = new Vector<>();
	String selectedTransactionID;
	TableView<TransactionDetail> transactionDetail;
	Vector<TransactionDetail> transactionDetails = new Vector<>();
	int totalPriceLabel;
	
	Scene manageProduct;
	String selectedID;
	
	Scene viewHistory;
	TableView<MyTransaction> adminTransactionTable;
	Vector<MyTransaction> adminTransactions = new Vector<>();
	TableView<TransactionDetail> adminTransactionDetail;
	Vector<TransactionDetail> adminTransactionDetails = new Vector<>();
	
	private String temp = null;
	
	private Connect connect = Connect.getInstance();
	
	public String getUserID(String email) {
		String query = "SELECT UserID FROM MsUser WHERE UserEmail = '" + email + "'";
		ResultSet rs = connect.executeQuery(query);
		System.out.println(query);
		
		try {
			while(rs.next()) {
				loggedInUserID = rs.getString("UserID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loggedInUserID;
		
	}
	
	public void alert(AlertType type, String title, String message ) {
		Alert alert = new Alert(type);
		alert.setHeaderText(title);
		alert.setContentText(message);
		alert.show();
	}

	public String getRole(String username, String password) {
		String query1 = "SELECT* FROM msuser WHERE UserRole = 'Admin' AND UserEmail = '" + username + "' AND UserPassword = '" + password + "'";
		ResultSet resultSet = connect.executeQuery(query1);
//		getUserID(username);
		
		try {
			if (resultSet.next()) {
				return "Admin";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "User";
		
	}
	
	public boolean validateLogin(String username, String password) {
		
		String query = "SELECT* FROM msuser WHERE UserEmail = '" + username + "' AND UserPassword = '" + password + "'"; 
		ResultSet resultSet = connect.executeQuery(query);
		
		try {
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
	
	public void loginHandler(String username, String password) {
		if (username.isEmpty() || password.isEmpty()) {
			alert(AlertType.WARNING, "Warning", "Email or Password must be filled!");
			return;
		}
		
		if (validateLogin(username, password)) {
			
			loggedInUserID = getUserID(username);
			System.out.println(loggedInUserID);
			String role = getRole(username, password);
			if (role.equals("User")) {
				
				loginStage.close();
				homeStage = new Stage();
				homeStage.setTitle("Home");
				homeStage.setResizable(false);
				homeStage.setScene(homeScene);
				homeStage.show();
				
				
			}else if (role.equals("Admin")) {
				loginStage.close();
				manageStage.show();
				
			}
		}else {
			alert(AlertType.WARNING, "Warning", "Wrong Email or Password");
			return;
		}
		
	}
	
	public void login() {

		BorderPane borderpane1 = new BorderPane();
		Menu navbar = new Menu("Page");
		MenuItem Login = new MenuItem("Login");
		MenuItem Register = new MenuItem("Register");
		
		Register.setOnAction(e -> {
		    loginStage.close();
		    registerStage.show();
		});
		
		navbar.getItems().add(Login);
		navbar.getItems().add(Register);
		
		 MenuBar menubar = new MenuBar(); 
		 
		 menubar.getMenus().add(navbar);
		 
		VBox vbox1 = new VBox(10);
		vbox1.setAlignment(Pos.CENTER_LEFT);
		
		Label email = new Label("Email");
		tfEmail = new TextField();
		Label password = new Label("Password");
		PasswordField tfPassword = new PasswordField();
		Button loginbt = new Button("Login");
		
		email.setAlignment(Pos.CENTER_LEFT);
	    password.setAlignment(Pos.CENTER_LEFT);
		
		tfEmail.setPrefWidth(200);  
	    tfPassword.setPrefWidth(200); 
	    
	    tfEmail.setMaxWidth(200);
	    tfPassword.setMaxWidth(200);
		
		vbox1.getChildren().addAll(email, tfEmail, password, tfPassword, loginbt);
		vbox1.setAlignment(Pos.CENTER);
		borderpane1.setTop(menubar);
		borderpane1.setCenter(vbox1);
		
		loginScene = new Scene(borderpane1, 500, 500);
		
		loginbt.setOnAction(e -> {
			loginHandler(tfEmail.getText(), tfPassword.getText());
			login();
			registerScene();
			homeScene();
			cartScene();
			transactionCard();
			historyScene();
			manageProduct();
			viewHistory();
		});
		
	
	}
	
	public int countUser() {
		String count = "SELECT COUNT(*) FROM MsUser";
		ResultSet counts = connect.executeQuery(count);;
		
		int userNumber = 0;
		
		try {
			if (counts.next()) {
				userNumber = counts.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return userNumber;
		
	}
	
	public String generateID(int count) {
		count++;
		String id = String.format("UA%03d", count);
		return id;
		
	}
	
	public void registerHandler(String email, String password, String confirmPassword, int age, String gender, String nationality) {
		
		String query = "SELECT* FROM msuser WHERE UserEmail = '" + email + "'";
		ResultSet res1 = connect.executeQuery(query);
		String id = generateID(countUser());
		boolean isError = false;
		
		if (!email.endsWith("@gmail.com")) {
			alert(AlertType.WARNING, "Warning", "Email must ends with '@gmail.com'");
			isError = true;
		}
		
		try {
			if (res1.next()) {
				alert(AlertType.WARNING, "Warning", "Email has already been registered");
				isError = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (password.length() < 6) {
			alert(AlertType.WARNING, "Warning", "Password must contain minimun 6 characters");
			isError = true;
		}
		
		if (!confirmPassword.equals(password)) {
			alert(AlertType.WARNING, "Warning", "Confirm PAsswword must be the same as Password");
			isError = true;
		}
		
		if (age < 0) {
			alert(AlertType.WARNING, "Warning", "Age must be greater than 0");
			isError = true;
		}
		
		if (gender.isEmpty() || gender == null) {
			alert(AlertType.WARNING, "Warning", "Gender must be selected");
			isError = true;
		}
		
		if (nationality.isEmpty() || nationality == null ||"Select".equals(nationality)) {
			alert(AlertType.WARNING, "Warning", "Nationality must be selected");
			isError = true;
		}
		
		if(isError == false) {
			String insertQuery = "INSERT INTO MsUser "
				    + "VALUES('" + id + "', '" + email + "', '" + password + "', '" + age + "', '" + gender + "', '" + nationality + "', 'User')";

			connect.executeUpdate(insertQuery);
			registerStage.close();
			loginStage.show();
		}
		
		
	}
	
	private String selectGender(ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        return (selectedRadioButton != null) ? selectedRadioButton.getText() : "ini harus diisi";
    }
	
	public void registerScene() {

		Label emailLbl, passwordLbl, confirmpassLbl, ageLbl, genderLbl, nationalityLbl;
		TextField emailTF, ageTF;
		PasswordField passwordPF, confirmpassPF;
		Button registerBtn;
		
		emailLbl = new Label("Email");
		passwordLbl = new Label("Password");
		confirmpassLbl = new Label("Confirm Password");
		ageLbl = new Label("Age");
		genderLbl = new Label("Gender");
		nationalityLbl = new Label("Nationality");
		
		emailTF = new TextField();
		ageTF = new TextField();
		
		passwordPF = new PasswordField();
		confirmpassPF = new PasswordField();
		
		ToggleGroup genderToggleGroup = new ToggleGroup();
		RadioButton maleRB = new RadioButton("Male");
		maleRB.setToggleGroup(genderToggleGroup);
		RadioButton femaleRB = new RadioButton("Female");
		femaleRB.setToggleGroup(genderToggleGroup);
		
		ComboBox<String> nationalityCB = new ComboBox<>();
        nationalityCB.getItems().addAll("Select", "Indonesia", "Singapore", "Africa", "Binus Tercinta");
        nationalityCB.setValue("Select");
        
        Spinner<Integer> ageSP = new Spinner<>(1, 99, 1);
        
        registerBtn = new Button("Register");
        
        VBox leftvb = new VBox(10, emailLbl, emailTF, passwordLbl, passwordPF, confirmpassLbl, confirmpassPF, ageLbl, ageSP);
        VBox rightvb = new VBox(10, genderLbl, maleRB, femaleRB, nationalityLbl, nationalityCB, registerBtn);
        HBox hb = new HBox(10, leftvb, rightvb);
        hb.setAlignment(Pos.CENTER);

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Page");
        MenuItem registerMenuItem = new MenuItem("Register");
        MenuItem loginMenuItem = new MenuItem("Login");
        
        loginMenuItem.setOnAction(e ->{
        	registerStage.close();
        	loginStage.show();
        });
        
        menu.getItems().addAll(loginMenuItem, registerMenuItem);
        menuBar.getMenus().add(menu);
          
        BorderPane bp = new BorderPane();
        bp.setTop(menuBar);
        bp.setCenter(hb);

        registerScene = new Scene(bp, 500, 500);
        registerStage = new Stage();
        registerStage.setTitle("Register");
        registerStage.setResizable(false);
        registerStage.setScene(registerScene);
        
        registerBtn.setOnAction(e -> registerHandler(emailTF.getText() , passwordPF.getText(), confirmpassPF.getText(), ageSP.getValue(), selectGender(genderToggleGroup), nationalityCB.getValue()));
        

	}
	
	private void setHomeTable() {
		System.out.println(loggedInUserID);
		
		tableHome = new TableView<Product>();
		
		TableColumn<Product, String> idColumn = new TableColumn<Product, String>("id");
		idColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("ProductID"));
		idColumn.setPrefWidth(0);
		
		TableColumn<Product, String> nameColumn = new TableColumn<Product, String>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("ProductName"));
		nameColumn.setMinWidth(75);
		
		TableColumn<Product, String> brandColumn = new TableColumn<Product, String>("Brand");
		brandColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("ProductMerk"));
		brandColumn.setMinWidth(75);
		
		TableColumn<Product, Integer> stockColumn = new TableColumn<Product, Integer>("Stock");
		stockColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("ProductStock"));
		stockColumn.setMinWidth(75);
		
		TableColumn<Product, Integer> priceColumn = new TableColumn<Product, Integer>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("ProductPrice"));
		priceColumn.setMinWidth(75);
		
		tableHome.getColumns().addAll(idColumn, nameColumn, brandColumn, stockColumn, priceColumn);
		
		refreshProductTable();
		
	}
	
	private void getProductData() {
		products.removeAllElements();
		String query = "SELECT* FROM MsProduct WHERE ProductStock > 0";

		ResultSet rs = connect.executeQuery(query);
		
		try {
			while(rs.next()) {
				String id = rs.getString("ProductID");
				String name = rs.getString("ProductName");
				String brand = rs.getString("ProductMerk");
				int stock = rs.getInt("ProductStock");
				int price = rs.getInt("ProductPrice");
				products.add(new Product(id, name, brand, price, stock));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void refreshProductTable() {
		products.removeAllElements();
		getProductData();
		obsProduct = FXCollections.observableArrayList(products);
		tableHome.setItems(obsProduct);
	}
	
	public void addToCart(String productID, int quantity) {
		
		String CheckCartDuplicate = "SELECT* FROM CartTable WHERE ProductID = '" + productID +"' AND UserID = '" + loggedInUserID + "'";
		ResultSet rsCheck = connect.executeQuery(CheckCartDuplicate);
		
		try {
			if(rsCheck.next()) {
				int oldQuan = rsCheck.getInt("Quantity");
				int newQuan = oldQuan + quantity;
				String insertUpdate = "UPDATE CartTable SET Quantity = "+ newQuan + " WHERE UserID = '" + loggedInUserID + "' AND ProductID = '" + productID + "'";
				System.out.println(insertUpdate);
				connect.executeUpdate(insertUpdate);
				String updateQuery1 = "UPDATE MsProduct SET ProductStock = ProductStock - " + newQuan + " WHERE ProductID = '" + productID + "'";
				connect.executeUpdate(updateQuery1);
//				homeStage.close();
////				products.removeAllElements();
//			   	tableHome.getItems().clear();
//			   	refreshProductTable();
//			   	homeStage.show();
			}else {
				String insertQuery = "INSERT INTO CartTable VALUES ('" + loggedInUserID + "', '" + productID + "', " + quantity + ")";
				connect.executeUpdate(insertQuery);
				System.out.println(insertQuery);
				String updateQuery = "UPDATE MsProduct SET ProductStock = ProductStock - " + quantity + " WHERE ProductID = '" + productID + "'";
				connect.executeUpdate(updateQuery);
//				homeStage.close();
////				products.removeAllElements();
//			   	tableHome.getItems().clear();
//			   	refreshProductTable();
//			   	homeStage.show();
			}
			
			tableHome.getItems().clear();
			refreshProductTable();
			homeStage.close();
			homeStage.show();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	}

	public void homeScene() {
		setHomeTable();
		String userID = getUserID(loggedInEmail);
		System.out.println(userID);
		BorderPane bp = new BorderPane();
		
		
		Button addToCart = new Button("Add to Cart");
		
		Spinner<Integer> quan = new Spinner<>(1, 99, 1);
		
		Font font = new Font(20);
		productList = new Label("Product List");
		productList.setFont(font);
		
		productName = new Label("Product Name :");
		productBrand = new Label("Product Brand :");
		price = new Label("Price :");
		totalPrice = new Label("Total Price :");
		
		 MenuBar menuBar = new MenuBar();
	     Menu menu = new Menu("Page");
	     MenuItem homeItem = new MenuItem("Home");
	     MenuItem cartItem = new MenuItem("Cart");
	     MenuItem historyItem = new MenuItem("History");
	     MenuItem logoutItem = new MenuItem("Logout");
	     
	     menu.getItems().addAll(homeItem, cartItem, historyItem, logoutItem);
	     menuBar.getMenus().add(menu);
		
	     homeItem.setOnAction(e -> {
	    	 homeStage.close();
	    	 homeStage.show();
	     });
	     
	     cartItem.setOnAction(e -> {
	    	 homeStage.close();
	    	 cartScene();
	    	 cartStage.show();
	     });
	     
	     historyItem.setOnAction(e -> {
	    	 homeStage.close();
	    	 historyStage.show();
	     });
	     
	     logoutItem.setOnAction(e -> {
	    	 homeStage.close();
	    	 loginStage.show();
	     });
	     
		VBox vb = new VBox();
		vb.getChildren().addAll(productList, tableHome);
		
		tableHome.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		tableHome.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {

		        productName.setText("Product Name : " + newSelection.getProductName());
		        productBrand.setText("Product Brand : " + newSelection.getProductMerk());
		        price.setText("Price : " + newSelection.getProductPrice());
				totalPrice.setText("Total Price :" + (newSelection.getProductPrice() * quan.getValue()));
				
				selectedProduct = new Product(newSelection.getProductID(), newSelection.getProductMerk(), newSelection.getProductName(), newSelection.getProductPrice(), newSelection.getProductStock());
				
		    
		    }
		        
		});
			     
	     addToCart.setOnAction(e -> {
	    	 int qty = quan.getValue();
	    	 if (selectedProduct != null) {
				   	addToCart(selectedProduct.getProductID(), qty);
				   	tableHome.getItems().clear();
					refreshProductTable();	   	
			   }else {
				   alert(AlertType.WARNING, "WARNING", "Please choose 1 item");
			}
	    	 
	    	 
	     });
	     
		
		VBox vb2 = new VBox(10);
		vb2.getChildren().addAll(productName, productBrand, price, quan, totalPrice, addToCart);
		vb2.setAlignment(Pos.BASELINE_CENTER);
		
		HBox hb = new HBox(10, vb, vb2);
		hb.setAlignment(Pos.CENTER);
		
		bp.setTop(menuBar);
		bp.setCenter(hb);
		homeScene = new Scene(bp, 750, 550);

	}
	
	private void getCartData() {
		carts.removeAllElements();
//		String query = "SELECT ProductName, ProductMerk, ProductPrice, Quantity, (ProductPrice * Quantity) FROM CartTable CT JOIN MsProduct MP ON CT.ProductID = MP.ProductID WHERE UserID = '" + userID + "'";
		String query = "SELECT UserID, ProductName, ProductMerk, ProductPrice, Quantity, (ProductPrice * Quantity) FROM CartTable CT JOIN MsProduct MP ON CT.ProductID = MP.ProductID" + " WHERE UserID = '" + loggedInUserID + "'";
		System.out.println(query);
		
		connect.rs = connect.executeQuery(query);
		
		try {
			while(connect.rs.next()) {
				String name = connect.rs.getString("ProductName");
				String brand = connect.rs.getString("ProductMerk");
				int price = connect.rs.getInt("ProductPrice");
				int quantity = connect.rs.getInt("Quantity");
				int total = connect.rs.getInt("(ProductPrice * Quantity)");
				carts.add(new Cart(name, brand, price, quantity, total));
			};
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void refreshCartTable() {
		getCartData();
		listCart = FXCollections.observableArrayList(carts);
		tableCart.setItems(listCart);
	}
	
	public void setCartTable() {
		tableCart = new TableView<Cart>();
		
		TableColumn<Cart, String> nameColumn = new TableColumn<Cart, String>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("name"));
		nameColumn.setMinWidth(75);
		
		TableColumn<Cart, String> brandColumn = new TableColumn<Cart, String>("Brand");
		brandColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("brand"));
		brandColumn.setMinWidth(75);
		
		TableColumn<Cart, Integer> priceColumn = new TableColumn<Cart, Integer>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("Price"));
		priceColumn.setMinWidth(75);
		
		TableColumn<Cart, Integer> quantityColumn = new TableColumn<Cart, Integer>("Quantity");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("quantity"));
		quantityColumn.setMinWidth(75);
		
		TableColumn<Cart, Integer> total = new TableColumn<Cart, Integer>("total");
		total.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("total"));
		total.setMinWidth(75);
		
		tableCart.getColumns().addAll(nameColumn, brandColumn, priceColumn, quantityColumn, total);
		
		refreshCartTable();
		
	}
	
	public void cartScene() {
		setCartTable();
		BorderPane bp = new BorderPane();
		
		Label title, Name, Brand, Price, totalPrice;
		Button checkout, deleteProduct;
		
		title = new Label("Your Cart List");
		Name = new Label("Name :");
		Brand = new Label("Brand :");
		Price = new Label("Price :");
		totalPrice = new Label("Total Price :");
		
		checkout = new Button("Checkout");
		deleteProduct = new Button("Delete Product");
		checkout.setPrefWidth(300);
		deleteProduct.setPrefWidth(300);
		
		Font font = new Font(20);
		title.setFont(font);
		
		MenuBar menuBar = new MenuBar();
	     Menu menu = new Menu("Page");
	     MenuItem homeItem = new MenuItem("Home");
	     MenuItem cartItem = new MenuItem("Cart");
	     MenuItem historyItem = new MenuItem("History");
	     MenuItem logoutItem = new MenuItem("Logout");
	     
	     menu.getItems().addAll(homeItem, cartItem, historyItem, logoutItem);
	     menuBar.getMenus().add(menu);
		
	     homeItem.setOnAction(e -> {
	    	 cartStage.close();
	    	 homeScene();
	    	 homeStage.show();
	     });
	     
	     historyItem.setOnAction(e -> {
	    	 cartStage.close();
	    	 historyStage.show();
	     });
	     
	     logoutItem.setOnAction(e -> {
	    	 cartStage.close();
	    	 loginStage.show();
	     });
	     
	     VBox vb1 = new VBox();
	     vb1.getChildren().addAll(title, tableCart);
	     
	 	tableCart.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	 	
	     tableCart.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	            if (newSelection != null) {
	                Name.setText("Name :" + newSelection.getName());
	                Brand.setText("Brand : " + newSelection.getBrand());
	                Price.setText("Price : " + newSelection.getPrice());
	                totalPrice.setText("Total Price : " + newSelection.getTotal());
	                cart = new Cart(newSelection.getName(), newSelection.getBrand(), newSelection.getPrice(), newSelection.getQuantity(), newSelection.getTotal());
	                
	            } else {
	                productName.setText("Product Name :");
	                productBrand.setText("Product Brand :");
	                price.setText("Price :");
	                totalPrice.setText("Total Price :");
	            }
	        });
			
	     
	     deleteProduct.setOnAction(e -> {
	    	 if (cart == null) {
				alert(AlertType.WARNING, "WARNING", "Please select product to delete");
			}else {
				String productName = cart.getName();
				String getProductIDQuery = "SELECT ProductID FROM MsProduct WHERE ProductName = '" + productName +"'";
				ResultSet getProductID = connect.executeQuery(getProductIDQuery);
				
				try {
					if(getProductID.next()) {
						String productID = getProductID.getString("ProductID");
						String query = String.format("DELETE FROM CartTable WHERE UserID = '%s' AND ProductID = '%s'", loggedInUserID, productID);
						String queryUpdate = String.format("UPDATE MsProduct SET ProductStock = ProductStock + %s WHERE ProductID = '%s'", cart.getQuantity(), productID);
						connect.executeUpdate(query);
						connect.executeUpdate(queryUpdate);
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				tableCart.getItems().clear();
				refreshCartTable();
				cartStage.close();
				cartStage.setScene(cartScene);
				cartStage.show();
				
			}
	     });
	     
	    checkout.setOnAction(e -> {
	    	String checkCart = "SELECT* FROM CartTable WHERE UserID = '" + loggedInUserID + "'";
	    	ResultSet rsCart = connect.executeQuery(checkCart);
	    	
	    	try {
				if (rsCart.next()) {
					transactionStage.show();
				}else {
					alert(AlertType.WARNING, "WARNING", "Please insert item to your cart");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
	    });
	     
	     VBox vb2 = new VBox(10);
	     vb2.getChildren().addAll(Name, Brand, Price, totalPrice);
	     
	     HBox hb = new HBox(10, vb1, vb2);
	     hb.setAlignment(Pos.CENTER);
	     
	     VBox vb3 = new VBox(10);
	     vb3.getChildren().addAll(checkout, deleteProduct);
	     vb3.setAlignment(Pos.CENTER);
	     
	     bp.setTop(menuBar);
	     bp.setCenter(hb);
	     bp.setBottom(vb3);
	     
	     cartScene = new Scene(bp, 750, 550);
	     cartStage = new Stage();
	     cartStage.setTitle("Cart");
	     cartStage.setScene(cartScene);
	     cartStage.setResizable(false);
	     
		
		
	}
	
	public int countTransaction() {
		String count = "SELECT COUNT(*) FROM TransactionHeader";
		ResultSet counts = connect.executeQuery(count);;
		
		int userNumber = 0;
		
		try {
			if (counts.next()) {
				userNumber = counts.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return userNumber;
		
	}
	
	public String generateTransactionID(int count) {
		count++;
		String id = String.format("TH%03d", count);
		return id;
		
	}
	
	public void transactionCard() {
		jfxtras.labs.scene.control.window.Window window = new jfxtras.labs.scene.control.window.Window("Transaction Card");
		
		int count = countTransaction();
		
		ArrayList<String> listStringProduct = new ArrayList<>();
		
		StackPane sp = new StackPane();
		
		BorderPane bp = new BorderPane();
		
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		
		bp.setCenter(vb);
		
		Label list, CourierTitle, Courier, useInsurance, totalPrice;
		
		list = new Label("List");
		CourierTitle = new Label("Courier");
		
		Button checkout = new Button("Checkout");
		checkout.setPrefWidth(200);
		
		ComboBox<String> courier = new ComboBox<>();
        courier.getItems().addAll("J&E", "TeKi","Si Lama");
        courier.setValue("Select");
        
        useInsurance = new Label("Use Insurance");
		CheckBox insurance = new CheckBox();
		
		String query = "SELECT ProductName, ProductPrice, (ProductPrice * Quantity) FROM CartTable CT JOIN MsProduct MP ON CT.ProductID = MP.ProductID  WHERE UserID = '" + loggedInUserID + "'";
		ResultSet rs = connect.executeQuery(query);
		
		System.out.println(query);
		
		vb.getChildren().add(list);
		
		int totalPrices = 0;
		
		try {
			while(rs.next()) {
				String productName = rs.getString("ProductName");
				int productPrice = rs.getInt("ProductPrice");
				int totalPricesPerQuan = rs.getInt("(ProductPrice * Quantity)");
				totalPrices += totalPricesPerQuan;
				
				Label priceLabel = new Label(productName + " : " + productPrice);
				vb.getChildren().add(priceLabel);
				
			}
			
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		FlowPane fp = new FlowPane();
		fp.setAlignment(Pos.CENTER);
		fp.setHgap(10);
		fp.getChildren().addAll(insurance, useInsurance);
		
		totalPrice = new Label("Total Price :" + totalPrices);
		
		int finalTotal = totalPrices;
		
			deliveryInsurance = 0;
		
		if (insurance.isSelected()) {
			finalTotal += 90000;
			deliveryInsurance = 1;
			totalPrice.setText("Total Price : " + finalTotal);
		}
		
		
		vb.getChildren().addAll(CourierTitle, courier, fp, totalPrice, checkout);
		vb.setSpacing(10);
		
		window.getContentPane().getChildren().add(bp);
		
		checkout.setOnAction(e -> {
			Alert alert1 = new Alert(AlertType.CONFIRMATION);
			alert1.setHeaderText("Confirmation");
			alert1.setContentText("Need Confirmation");
			
			Optional<ButtonType> yesorno = alert1.showAndWait();
			
			if (yesorno.get() == ButtonType.OK) {
				
				String querys = "SELECT ProductID, Quantity FROM CartTable CT JOIN MsProduct MP ON CT.ProductID = MP.ProductID  WHERE UserID = '" + loggedInUserID + "'";
				ResultSet rsInsert = connect.executeQuery(querys);
				
				String transactionID = generateTransactionID(count);
				
				String insertTransaction = "INSERT INTO TransactionHeader VALUES(' +" + transactionID +"', '" + loggedInUserID + "', GETDATE(), " + deliveryInsurance + "', '" + courier.getValue() + "')" ;
				
				connect.executeUpdate(insertTransaction);
				
				try {
					while(rsInsert.next()) {
						String productID = rsInsert.getString("ProductID");
						int quantity = rsInsert.getInt("Quantity");
						String insertTransactionDetail = "INSERT INTO TransactionDetail VALUES('" + productID +"', '" + transactionID + "', " + quantity + ")";
						connect.executeUpdate(insertTransactionDetail);
						
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String removeCart = "DELETE FROM CartTable WHERE UserID = '" + loggedInUserID +"'";
				connect.executeUpdate(removeCart);
				carts.removeAllElements();
				refreshCartTable();
				
				transactionStage.close();
				cartStage.show();
			}else {
				
			}
			
			
			
		});
	
		sp.getChildren().addAll(window);
		
		transactionCard = new Scene(sp, 750, 500);
		transactionStage = new Stage();
		transactionStage.setResizable(false);
		transactionStage.setScene(transactionCard);
		
	}
	
	public void getMyTransactionData() {
		myTransactions.removeAllElements();
		
		String query = "SELECT* FROM MsUser MU JOIN TransactionHeader TH ON MU.UserID = TH.UserID WHERE TH.UserID = '" + loggedInUserID + "'";
		System.out.println(query);
		connect.rs = connect.executeQuery(query);
		
		try {
			while(connect.rs.next()) {
				String transactionID = connect.rs.getString("TransactionID");
				String userEmail = connect.rs.getString("UserEmail");
				String transactionDate = connect.rs.getString("TransactionDate");
				myTransactions.add(new MyTransaction(transactionID, userEmail, transactionDate));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void refreshMyTransactionTable() {
		getMyTransactionData();
		ObservableList<MyTransaction> myTransactionObs = FXCollections.observableArrayList(myTransactions);
		transactionTable.setItems(myTransactionObs);
	}
	
	public void setMyTransactionTable() {
		transactionTable = new TableView<MyTransaction>();
		
		TableColumn<MyTransaction, String> idColumn = new TableColumn<MyTransaction, String>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<MyTransaction, String>("id"));
		idColumn.setMinWidth(75);
		
		TableColumn<MyTransaction, String> emailColumn = new TableColumn<MyTransaction, String>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<MyTransaction, String>("email"));
		emailColumn.setMinWidth(75);
		
		TableColumn<MyTransaction, String> dateColumn = new TableColumn<MyTransaction, String>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<MyTransaction, String>("date"));
		dateColumn.setMinWidth(75);
		
		transactionTable.getColumns().addAll(idColumn, emailColumn, dateColumn);
		
		refreshMyTransactionTable();
		
	}
	
	public void setTransactionDetailTable() {
		transactionDetail = new TableView<TransactionDetail>();
		
		TableColumn<TransactionDetail, String> idColumn = new TableColumn<TransactionDetail, String>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("id"));
		idColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> productColumn = new TableColumn<TransactionDetail, String>("Product");
		productColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("product"));
		productColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> priceColumn = new TableColumn<TransactionDetail, String>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("price"));
		priceColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> quanColumn = new TableColumn<TransactionDetail, String>("Quantity");
		quanColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("quantity"));
		quanColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> totalColumn = new TableColumn<TransactionDetail, String>("Total Price");
		totalColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("totalPrice"));
		totalColumn.setMinWidth(75);
		
		transactionDetail.getColumns().addAll(idColumn, productColumn, priceColumn, quanColumn, totalColumn);
		
	}
	
	public void historyScene() {
		setMyTransactionTable();
		setTransactionDetailTable();
		
		StackPane sp = new StackPane();
		BorderPane bp = new BorderPane();
		GridPane gp = new GridPane();
		
		Label myTransaction, transactionDetailL, totalPrice;
		
		myTransaction = new Label("My Transaction");
		transactionDetailL = new Label("Transaction Detail");
		totalPrice = new Label("Total Price :");
		
		MenuBar menuBar = new MenuBar();
	     Menu menu = new Menu("Page");
	     MenuItem homeMenuItem = new MenuItem("Home");
	     MenuItem cartMenuItem = new MenuItem("Cart");
	     MenuItem historyMenuItem = new MenuItem("History");
	     MenuItem logoutMenuItem = new MenuItem("Logout");
	     menu.getItems().addAll(homeMenuItem, cartMenuItem, historyMenuItem, logoutMenuItem);
	     menuBar.getMenus().add(menu);
	     
	     homeMenuItem.setOnAction(e -> {
	    	 historyStage.close();
	    	 homeStage.show();
	     });
	     
	     cartMenuItem.setOnAction(e -> {
	    	 historyStage.close();
	    	 cartStage.show();
	     });
	     
	     historyMenuItem.setOnAction(e -> {
	    	 historyStage.close();
	    	 historyStage.show();
	     });
	     
	     logoutMenuItem.setOnAction(e -> {
	    	 historyStage.close();
	    	 loginStage.show();
	     });
	     
	     transactionTable.setOnMouseClicked(e -> {
	    	 transactionDetails.removeAllElements();
	    	 totalPriceLabel = 0;
	    	 TableSelectionModel<MyTransaction> myTransactionSelect = transactionTable.getSelectionModel();
	    	 MyTransaction selectedTransaction = myTransactionSelect.getSelectedItem();
	    	 selectedTransactionID = selectedTransaction.getId();
	    	 
	    	 String query = "SELECT TransactionID, ProductName, ProductPrice, Quantity, (ProductPrice * Quantity) AS `TotalPrice` FROM TransactionDetail TD JOIN MsProduct MP ON TD.ProductID = MP.ProductID  WHERE TransactionID = '" + selectedTransactionID + "'";
	    	 connect.rs = connect.executeQuery(query);
	    	 
	    	 try {
				while(connect.rs.next()) {
					 String ID = connect.rs.getString("TransactionID");
					 String product = connect.rs.getString("ProductName");
					 int price = connect.rs.getInt("ProductPrice");
					 int quantity = connect.rs.getInt("Quantity");
					 int totalPrices = connect.rs.getInt("TotalPrice");
					 transactionDetails.add(new TransactionDetail(ID, product, price, quantity, totalPrices));
					 totalPriceLabel = totalPriceLabel + totalPrices;
				 }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	 
	    	 totalPrice.setText("Total Price : " + totalPriceLabel);
	    	 ObservableList<TransactionDetail> TransactionDetailObs = FXCollections.observableArrayList(transactionDetails);
	 		transactionDetail.setItems(TransactionDetailObs);
	
	     });
	     
	     bp.setAlignment(totalPrice, Pos.CENTER);
	     
	     bp.setTop(menuBar);
	     
	     gp.add(myTransaction, 0, 0);
	     gp.add(transactionTable, 0, 1);
	     gp.add(transactionDetailL, 1, 0);
	     gp.add(transactionDetail, 1, 1);
	     gp.setHgap(15);
	     gp.setVgap(15);
	     gp.setAlignment(Pos.CENTER);
	     
	     bp.setCenter(gp);
	     
	     
	     bp.setBottom(totalPrice);
	     
	     historyScene = new Scene(bp, 1000, 500);
	     historyStage = new Stage();
	     historyStage.setScene(historyScene);
	     historyStage.setTitle("My History");
	     historyStage.setResizable(false);
	     
		
	}
	
	public int countProduct() {
		String count = "SELECT COUNT(*) FROM MsProduct";
		ResultSet counts = connect.executeQuery(count);;
		
		int userNumber = 0;
		
		try {
			if (counts.next()) {
				userNumber = counts.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return userNumber;
		
	}
	
	
	public void manageProduct() {
		setHomeTable();
		
		Font font = new Font(20);
		
		Label productList, productName, productBrand, productPrice, Name, addStock, deleteProduct;
		productList = new Label("Product List");
		productList.setFont(font);
		productName = new Label("Product Name");
		productBrand = new Label("Product Brand");
		productPrice = new Label("Product Price");
		Name = new Label("Name");
		addStock = new Label("Add Stock");
		deleteProduct = new Label("Delete Product");
		
		TextField plTF, ppTF;
		plTF = new TextField();
		ppTF = new TextField();
		
		ComboBox<String> selectBrand = new ComboBox<>();
		selectBrand.getItems().addAll("Yonex", "Wilson", "Brand A", "Brand B", "Brand C");
		selectBrand.setValue("Select");
		
		Spinner<Integer> stock = new Spinner<>(1, 99, 1);
		
		Button addProduct, addStockbt ,deletebt;
		addProduct = new Button("Add Product");
		addStockbt = new Button("Add Stock");
		deletebt = new Button("Delete");
		
		BorderPane bp = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
	     Menu menu = new Menu("Admin");
	     MenuItem manageItem = new MenuItem("Manage Product");
	     MenuItem viewItem = new MenuItem("View History");
	     MenuItem logoutItem = new MenuItem("Logout");
	     menu.getItems().addAll(manageItem, viewItem, logoutItem);
	     menuBar.getMenus().add(menu);
	     
	     addProduct.setOnAction(e -> {
	    		int amountProduct = countProduct();
	    		amountProduct++;
	    		String formatID = String.format("PD%03d", amountProduct);
	    		String productNames = plTF.getText();
	    		String merek = selectBrand.getValue();
	    		String harga = ppTF.getText();
	    		
//	    		String query = String.format("INSERT INTO MsProduct VALUES(%s, %s, %s, %d, %d)", formatID, productNames, merek, harga, 1);
	    		
	    		String query = "INSERT INTO MsProduct VALUES('" + formatID +"', '" + productNames +"', '" + merek +"', " + harga + ", 1)";
	    		System.out.println(query);
	    		
	    		connect.executeUpdate(query);
	    		tableHome.getItems().clear();
	    		refreshProductTable();
	    		manageStage.close();
		    	manageStage.show();
	     });
	     
	     manageItem.setOnAction(e -> {
	    	 manageStage.close();
	    	 manageStage.show();
	     });
	     
	     viewItem.setOnAction(e -> {
	    	 manageStage.close();
	    	 viewAdminHistoryStage.show();
	     });
	     
	     logoutItem.setOnAction(e -> {
	    	 manageStage.close();
	    	 loginStage.show();
	     });
	     
	     bp.setTop(menuBar);
	     
	     tableHome.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			tableHome.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			    if (newSelection != null) {
			    	Name.setText("Name : " + newSelection.getProductName());
			    	selectedID = newSelection.getProductID();
			    }
			        
			});
			
		deletebt.setOnAction(e -> {
			String query = "DELETE FROM MsProduct WHERE ProductID = '" + selectedID + "'";
			connect.executeUpdate(query);
			tableHome.getItems().clear();
    		refreshProductTable();
    		manageStage.close();
	    	manageStage.show();
	
		});	
		
		int newStock = stock.getValue();
		
		addStockbt.setOnAction(e -> {
			String query = "UPDATE MsProduct SET ProductStock = ProductStock + " + newStock + " WHERE ProductID = '" + selectedID +"'";
			connect.executeUpdate(query);
			tableHome.getItems().clear();
    		refreshProductTable();
			manageStage.close();
			manageStage.show();
		});
	     
	     VBox vb1 = new VBox();
	     
	     vb1.getChildren().addAll(productList, tableHome);
	     
	     GridPane gp = new GridPane();
	     gp.add(productName, 0, 1);
	     gp.add(plTF, 0, 2);
	     gp.add(productBrand, 0, 3);
	     gp.add(selectBrand, 0, 4);
	     gp.add(productPrice, 0, 5);
	     gp.add(ppTF, 0, 6);
	     gp.add(addProduct, 0, 7);
	     gp.setHgap(10);
	     gp.setVgap(10);
	     gp.setPadding(new Insets(10, 10, 10, 10));
	     
	     HBox hb = new HBox();
	     hb.getChildren().addAll(vb1, gp);
	     hb.setAlignment(Pos.CENTER);
	     
	     bp.setCenter(hb);
	     
	     GridPane gp2 = new GridPane();
	     gp2.add(Name, 0, 0);
	     gp2.add(addStock, 0, 1);
	     gp2.add(stock, 0, 2);
	     gp2.add(addStockbt, 0, 3);
	     
	     gp2.add(deleteProduct, 1, 1);
	     gp2.add(deletebt, 1, 3);
	     gp2.setVgap(10);
	     gp2.setHgap(10);
	     
	     gp2.setAlignment(Pos.CENTER);
	     
	     bp.setBottom(gp2);
	     
	     manageStage = new Stage();
	     manageStage.setTitle("Manage Product");
	     manageStage.setResizable(false);
	     manageProduct = new Scene(bp, 1000, 600);
	     manageStage.setScene(manageProduct);
		
	}
	
	public void setAdminTransactionTable() {
		adminTransactionTable = new TableView<MyTransaction>();
		
		TableColumn<MyTransaction, String> idColumn = new TableColumn<MyTransaction, String>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<MyTransaction, String>("id"));
		idColumn.setMinWidth(75);
		
		TableColumn<MyTransaction, String> emailColumn = new TableColumn<MyTransaction, String>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<MyTransaction, String>("email"));
		emailColumn.setMinWidth(75);
		
		TableColumn<MyTransaction, String> dateColumn = new TableColumn<MyTransaction, String>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<MyTransaction, String>("date"));
		dateColumn.setMinWidth(75);
		
		adminTransactionTable.getColumns().addAll(idColumn, emailColumn, dateColumn);
		
		refreshAdminTransactionTable();
		
	}
	
	public void getAdminTransactionData() {
		adminTransactions.removeAllElements();
		
		String query = "SELECT* FROM MsUser MU JOIN TransactionHeader TH ON MU.UserID = TH.UserID";
		System.out.println(query);
		connect.rs = connect.executeQuery(query);
		
		try {
			while(connect.rs.next()) {
				String transactionID = connect.rs.getString("TransactionID");
				String userEmail = connect.rs.getString("UserEmail");
				String transactionDate = connect.rs.getString("TransactionDate");
				adminTransactions.add(new MyTransaction(transactionID, userEmail, transactionDate));
				System.out.println(adminTransactions);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void refreshAdminTransactionTable() {
		getAdminTransactionData();
		ObservableList<MyTransaction> adminTransactionObs = FXCollections.observableArrayList(adminTransactions);
		adminTransactionTable.setItems(adminTransactionObs);
	}
	
	public void setAdminTransactionDetailTable() {
		adminTransactionDetail = new TableView<TransactionDetail>();
		
		TableColumn<TransactionDetail, String> idColumn = new TableColumn<TransactionDetail, String>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("id"));
		idColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> productColumn = new TableColumn<TransactionDetail, String>("Product");
		productColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("product"));
		productColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> priceColumn = new TableColumn<TransactionDetail, String>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("price"));
		priceColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> quanColumn = new TableColumn<TransactionDetail, String>("Quantity");
		quanColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("quantity"));
		quanColumn.setMinWidth(75);
		
		TableColumn<TransactionDetail, String> totalColumn = new TableColumn<TransactionDetail, String>("Total Price");
		totalColumn.setCellValueFactory(new PropertyValueFactory<TransactionDetail, String>("totalPrice"));
		totalColumn.setMinWidth(75);
		
		adminTransactionDetail.getColumns().addAll(idColumn, productColumn, priceColumn, quanColumn, totalColumn);
		
	}
	
	public void viewHistory() {
		setAdminTransactionTable();
		setAdminTransactionDetailTable();
		
		Label allTransaction, transactionDetaillb, totalPrice;
		allTransaction = new Label("All Transaction");
		transactionDetaillb = new Label("Transaction Detail");
		totalPrice = new Label("Total Price");
		
		MenuBar menuBar = new MenuBar();
	     Menu menu = new Menu("Admin");
	     MenuItem manageItem = new MenuItem("Manage Product");
	     MenuItem viewItem = new MenuItem("View History");
	     MenuItem logoutItem = new MenuItem("Logout");
	     menu.getItems().addAll(manageItem, viewItem, logoutItem);
	     menuBar.getMenus().add(menu);
	     
	     manageItem.setOnAction(e -> {
	    	 viewAdminHistoryStage.close();
	    	 manageStage.show();
	     });
	     
	     viewItem.setOnAction(e -> {
	    	viewAdminHistoryStage.close();
	    	viewAdminHistoryStage.show();
	     });
	     
	     logoutItem.setOnAction(e -> {
	    	 viewAdminHistoryStage.close();
	    	 loginStage.show();
	     });
	     BorderPane bp = new BorderPane();
	     bp.setTop(menuBar);
	     
	     adminTransactionTable.setOnMouseClicked(e -> {
	    	 adminTransactionDetails.removeAllElements();
	    	 totalPriceLabel = 0;
	    	 TableSelectionModel<MyTransaction> myTransactionSelect = adminTransactionTable.getSelectionModel();
	    	 MyTransaction selectedTransaction = myTransactionSelect.getSelectedItem();
	    	 selectedTransactionID = selectedTransaction.getId();
	    	 
	    	 String query = "SELECT TransactionID, ProductName, ProductPrice, Quantity, (ProductPrice * Quantity) AS `TotalPrice` FROM TransactionDetail TD JOIN MsProduct MP ON TD.ProductID = MP.ProductID  WHERE TransactionID = '" + selectedTransactionID + "'";
	    	 connect.rs = connect.executeQuery(query);
	    	 
	    	 try {
				while(connect.rs.next()) {
					 String ID = connect.rs.getString("TransactionID");
					 String product = connect.rs.getString("ProductName");
					 int price = connect.rs.getInt("ProductPrice");
					 int quantity = connect.rs.getInt("Quantity");
					 int totalPrices = connect.rs.getInt("TotalPrice");
					 adminTransactionDetails.add(new TransactionDetail(ID, product, price, quantity, totalPrices));
					 totalPriceLabel = totalPriceLabel + totalPrices;
				 }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	 
	    	 totalPrice.setText("Total Price : " + totalPriceLabel);
	    	 ObservableList<TransactionDetail> TransactionDetailObs = FXCollections.observableArrayList(adminTransactionDetails);
	 		adminTransactionDetail.setItems(TransactionDetailObs);
	
	     });
	     
	     GridPane gp = new GridPane();
	     gp.add(allTransaction, 0, 0);
	     gp.add(adminTransactionTable, 0, 1);
	     gp.add(transactionDetaillb, 1, 0);
	     gp.add(adminTransactionDetail, 1, 1);
	     gp.setHgap(10);
	     gp.setVgap(10);
	     
	     bp.setCenter(gp);
	     gp.setAlignment(Pos.CENTER);
	     
	     bp.setAlignment(totalPrice, Pos.CENTER);
	     bp.setBottom(totalPrice);
	     
	     viewHistory = new Scene(bp, 1000, 500);
	     viewAdminHistoryStage = new Stage();
	     viewAdminHistoryStage.setScene(viewHistory);
	     viewAdminHistoryStage.setResizable(false);
	     viewAdminHistoryStage.setTitle("My History");
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.loginStage = primaryStage;
		
		login();
		registerScene();
		homeScene();
		cartScene();
		transactionCard();
		historyScene();
		manageProduct();
		viewHistory();
		primaryStage.setScene(loginScene);
		primaryStage.setTitle("Login");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
		
	}


}
