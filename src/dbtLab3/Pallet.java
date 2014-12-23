package dbtLab3;

public class Pallet {
	private String palletId;
	private String date;
	private boolean isBlocked;
	private String cookieName;
	private String orderNbr;
	
	public Pallet(String palletId, String date, boolean isBlocked, String cookieName, String orderNbr){
		this.palletId = palletId;
		this.date = date;
		this.isBlocked = isBlocked;
		this.cookieName = cookieName;
		this.orderNbr = orderNbr;
	}
	
	public String getPalletId(){
		return palletId;
	}
	
	public String getDate(){
		return date;
	}
	
	public boolean blocked(){
		return isBlocked;
	}
	
	public String getCookieName(){
		return cookieName;
		
	}
	
	public String getOrderNbr(){
		return orderNbr;
	}
}
