package Objects;

public class MyTransaction {
	private String id, Email;
	private String date;
	
	public MyTransaction(String id, String email, String date) {
		super();
		this.id = id;
		Email = email;
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	
	
}
