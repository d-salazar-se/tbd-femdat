package tbd.grupo1.femdat.model;

public class Data {
	String name;
	Long quantity;

	public Data(String name, Long quantity)
	{
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}
