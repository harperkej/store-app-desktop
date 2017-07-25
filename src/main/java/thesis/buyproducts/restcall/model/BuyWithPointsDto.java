package thesis.buyproducts.restcall.model;

public class BuyWithPointsDto {

	private String username;

	private String firstName;

	private String lastName;

	private Double pointsLeft;

	private Double hasToPay;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Double getPointsLeft() {
		return pointsLeft;
	}

	public void setPointsLeft(Double pointsLeft) {
		this.pointsLeft = pointsLeft;
	}

	public Double getHasToPay() {
		return hasToPay;
	}

	public void setHasToPay(Double hasToPay) {
		this.hasToPay = hasToPay;
	}

}
