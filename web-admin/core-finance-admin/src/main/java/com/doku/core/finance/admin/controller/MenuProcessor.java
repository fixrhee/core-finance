package com.doku.core.finance.admin.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import core.finance.host.services.ws.menu.LoadMenuByGroupsRequest;
import core.finance.host.services.ws.menu.LoadMenuByGroupsResponse;
import core.finance.host.services.ws.menu.Menu;
import core.finance.host.services.ws.menu.MenuServiceImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.doku.core.finance.admin.model.AdminMenu;

@Component
public class MenuProcessor {

	@Autowired
	private ContextLoader contextLoader;

	public AdminMenu getMenuList(Integer groupID) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl() + "menu?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "MenuServiceImplService");
		AdminMenu adminMenu = new AdminMenu();
		MenuServiceImplService MenuServiceImplService = new MenuServiceImplService(url, qName);
		Menu client = MenuServiceImplService.getMenuServiceImplPort();

		core.finance.host.services.ws.menu.Header headerMenu = new core.finance.host.services.ws.menu.Header();
		headerMenu.setToken(contextLoader.getHeaderToken());
		Holder<core.finance.host.services.ws.menu.Header> menuHeaderAuth = new Holder<core.finance.host.services.ws.menu.Header>();
		menuHeaderAuth.value = headerMenu;

		LoadMenuByGroupsRequest loadMenuByGroupsRequest = new LoadMenuByGroupsRequest();
		loadMenuByGroupsRequest.setGroupID(groupID);
		LoadMenuByGroupsResponse loadMenuByGroupsResponse = client.loadMenuByGroups(menuHeaderAuth,
				loadMenuByGroupsRequest);

		List<String> menu = new LinkedList<String>();
		for (int i = 0; i < loadMenuByGroupsResponse.getMainMenu().size(); i++) {
			String composeMenu = "<h3>" + loadMenuByGroupsResponse.getMainMenu().get(i).getMainMenuName()
					+ "</h3><ul class=\"nav side-menu\">";
			for (int j = 0; j < loadMenuByGroupsResponse.getMainMenu().get(i).getParentMenu().size(); j++) {
				composeMenu += "<li><a><i class=\""
						+ loadMenuByGroupsResponse.getMainMenu().get(i).getParentMenu().get(j).getIcon() + "\"></i>"
						+ loadMenuByGroupsResponse.getMainMenu().get(i).getParentMenu().get(j).getParentMenuName()
						+ "<span class=\"fa fa-chevron-down\"></span></a><ul class=\"nav child_menu\">";
				for (int k = 0; k < loadMenuByGroupsResponse.getMainMenu().get(i).getParentMenu().get(j).getChildMenu()
						.size(); k++) {
					composeMenu += "<li><a href=\""
							+ loadMenuByGroupsResponse.getMainMenu().get(i).getParentMenu().get(j).getChildMenu().get(k)
									.getLink()
							+ "\">" + loadMenuByGroupsResponse.getMainMenu().get(i).getParentMenu().get(j)
									.getChildMenu().get(k).getChildMenuName()
							+ "</a></li>";
				}
				composeMenu += "</ul></li>";
			}
			composeMenu += "</ul>";
			menu.add(composeMenu);
			adminMenu.setMainMenu(menu);
			adminMenu.setWelcomeMenu(loadMenuByGroupsResponse.getWelcomeMenu());
		}
		return adminMenu;
	}

}
