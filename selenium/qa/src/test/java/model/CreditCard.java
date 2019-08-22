package model;

public class CreditCard {
	
	private String cardNumber;
	private String expirationDate;
	private String cvc;
	private String zipCode;
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getCvc() {
		return cvc;
	}
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
	public static CreditCard generateCreditCard() {
		CreditCard card = new CreditCard();
		card.setCardNumber("4242424242424242");
		card.setExpirationDate("0442");
		card.setCvc("424");
		card.setZipCode("24242");
		return card;
	}
	
}
