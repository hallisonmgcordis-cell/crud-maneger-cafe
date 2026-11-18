package controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import model.CoffeeLot;
import model.ModelException;
import model.User;
import model.dao.CoffeeLotDAO;
import model.dao.DAOFactory;
import model.dao.UserDAO;

public class CommonsController {
	
	public static void listUsers(HttpServletRequest req) {
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		
		List<User> listUsers = null;
		try {
			listUsers = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (listUsers != null)
			req.setAttribute("users", listUsers);		
	}
	
	public static void listLots(HttpServletRequest req) {
		CoffeeLotDAO dao = DAOFactory.createDAO(CoffeeLotDAO.class);
		
		List<CoffeeLot> lots = null;
		try {
			lots = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (lots != null)
			req.setAttribute("lots", lots);
	}
}
