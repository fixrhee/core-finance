package com.doku.core.finance.admin.model;

import java.util.List;

public class AdminMenu {

	private List<String> mainMenu;
	private String welcomeMenu;

	public List<String> getMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(List<String> mainMenu) {
		this.mainMenu = mainMenu;
	}

	public String getWelcomeMenu() {
		return welcomeMenu;
	}

	public void setWelcomeMenu(String welcomeMenu) {
		this.welcomeMenu = welcomeMenu;
	}

}
