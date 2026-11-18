package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CoffeeLot;
import model.ModelException;
import model.User;
import model.dao.CoffeeLotDAO;
import model.dao.DAOFactory;

@WebServlet(urlPatterns = {"/lots", "/lot/form", "/lot/insert", "/lot/update", "/lot/delete"})
public class CoffeeLotsController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		// getServletPath() não depende do nome do contexto da aplicação
		String action = req.getServletPath();
		
		switch (action) {
		case "/lot/form": {
			CommonsController.listUsers(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-lot.jsp");
			break;
		}
		case "/lot/update": {
			CoffeeLot lot = loadLot(req);
			
			if (lot == null) {
				ControllerUtil.errorMessage(req, "Lote não encontrado para alteração.");
				ControllerUtil.redirect(resp, req.getContextPath() + "/lots");
				return;
			}
			
			CommonsController.listUsers(req);
			req.setAttribute("action", "update");
			req.setAttribute("lot", lot);
			ControllerUtil.forward(req, resp, "/form-lot.jsp");
			break;
		}
		default:
			listLots(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
			
			ControllerUtil.forward(req, resp, "/lots.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getServletPath();
		
		switch (action) {
		case "/lot/insert":
			insertLot(req);
			break;
		case "/lot/update":
			updateLot(req);
			break;
		case "/lot/delete":
			deleteLot(req);
			break;
		default:
			System.out.println("URL inválida " + action);
		}
		
		ControllerUtil.redirect(resp, req.getContextPath() + "/lots");
	}

	private void listLots(HttpServletRequest req) {
		CoffeeLotDAO dao = DAOFactory.createDAO(CoffeeLotDAO.class);
		
		List<CoffeeLot> lots = null;
		try {
			int page = ControllerUtil.paginate(req, dao.count(), "/lots");
			lots = dao.listPage(page, ControllerUtil.PAGE_SIZE);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (lots != null)
			req.setAttribute("lots", lots);
	}

	private CoffeeLot loadLot(HttpServletRequest req) {
		try {
			int lotId = Integer.parseInt(req.getParameter("lotId"));
			
			CoffeeLotDAO dao = DAOFactory.createDAO(CoffeeLotDAO.class);
			return dao.findById(lotId);
		} catch (NumberFormatException | ModelException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Monta o lote a partir do formulário. Retorna null (e registra a mensagem de erro) se inválido.
	private CoffeeLot buildLot(HttpServletRequest req, int id) {
		String name = req.getParameter("name");
		String variety = req.getParameter("variety");
		String areaStr = req.getParameter("area");
		String plantingDate = req.getParameter("plantingDate");
		String userIdStr = req.getParameter("user");
		
		if (name == null || name.trim().isEmpty() 
				|| variety == null || variety.isEmpty()) {
			ControllerUtil.errorMessage(req, "Informe o nome e a variedade do lote.");
			return null;
		}
		
		double area;
		int userId;
		try {
			area = Double.parseDouble(areaStr.replace(',', '.'));
			userId = Integer.parseInt(userIdStr);
		} catch (NumberFormatException | NullPointerException e) {
			ControllerUtil.errorMessage(req, "Área ou proprietário inválido.");
			return null;
		}
		
		if (area <= 0) {
			ControllerUtil.errorMessage(req, "A área deve ser maior que zero.");
			return null;
		}
		
		java.util.Date date = ControllerUtil.formatDate(plantingDate);
		if (date == null) {
			ControllerUtil.errorMessage(req, "Informe uma data de plantio válida.");
			return null;
		}
		
		CoffeeLot lot = new CoffeeLot(id);
		lot.setName(name.trim());
		lot.setVariety(variety);
		lot.setArea(area);
		lot.setPlantingDate(date);
		// checkbox: o parâmetro só é enviado quando marcado
		lot.setIrrigated(req.getParameter("irrigated") != null);
		lot.setUser(new User(userId));
		
		return lot;
	}

	private void insertLot(HttpServletRequest req) {
		CoffeeLot lot = buildLot(req, 0);
		if (lot == null)
			return;
		
		CoffeeLotDAO dao = DAOFactory.createDAO(CoffeeLotDAO.class);
		
		try {
			if (dao.save(lot)) {
				ControllerUtil.sucessMessage(req, "Lote '" + lot.getName() + "' salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Lote '" + lot.getName() + "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateLot(HttpServletRequest req) {
		int id;
		try {
			id = Integer.parseInt(req.getParameter("lotId"));
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Lote inválido para alteração.");
			return;
		}
		
		CoffeeLot lot = buildLot(req, id);
		if (lot == null)
			return;
		
		CoffeeLotDAO dao = DAOFactory.createDAO(CoffeeLotDAO.class);
		
		try {
			if (dao.update(lot)) {
				ControllerUtil.sucessMessage(req, "Lote '" + lot.getName() + "' atualizado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Lote '" + lot.getName() + "' não pode ser atualizado.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteLot(HttpServletRequest req) {
		CoffeeLotDAO dao = DAOFactory.createDAO(CoffeeLotDAO.class);
		
		try {
			int lotId = Integer.parseInt(req.getParameter("id"));
			
			CoffeeLot lot = dao.findById(lotId);
			
			if (lot == null)
				throw new ModelException("Lote não encontrado para deleção.");
			
			if (dao.delete(lot)) {
				ControllerUtil.sucessMessage(req, "Lote '" + lot.getName() + "' deletado com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Lote '" + lot.getName() 
						+ "' não pode ser deletado. Há colheitas registradas para ele.");
			}
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Lote inválido para deleção.");
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}
