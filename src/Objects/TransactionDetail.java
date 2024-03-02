package Objects;

import javafx.scene.control.Label;

public class TransactionDetail {
	private String id, Product;
	private int price, quantity, totalPrice;
	
	public TransactionDetail(String id, String product, int price, int quantity, int totalPrice) {
		super();
		this.id = id;
		this.Product = product;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProduct() {
		return Product;
	}
	public void setProduct(String product) {
		Product = product;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	

}
