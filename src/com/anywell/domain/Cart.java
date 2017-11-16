package com.anywell.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
	private Map<String, CartItem> cartItems = new HashMap<String, CartItem>();
	private double totalPrice;

	public Map<String, CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Map<String, CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
