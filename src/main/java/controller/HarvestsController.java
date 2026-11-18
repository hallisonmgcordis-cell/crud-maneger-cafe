package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CoffeeLot;
import model.Harvest;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.HarvestDAO;

@WebServlet(urlPatterns = {"/harvests", "/harvest/form", "/harvest/insert", 
		"/harvest/update", "/harvest/delete"})
public class HarvestsController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final List<String> QUALITIES = 
			Arrays.asList("ESPECIAL", "SUPERIOR", "TRADICIONAL");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getServletPath();
		
		switch (action) {
		case "/harvest/form": {
			CommonsController.listUsers(req);
			CommonsController.listLots(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-harvest.jsp");
			break;
		}
		case "/harvest/update": {
			Harvest harvest = loadHarvest(req);
			
			if (harvest == null) {
				ControllerUtil.errorMessage(req, "Colheita não encontrada para alteração.");
				ControllerUtil.redirect(resp, req.getContextPath() + "/harvests");
				return;
			}
			
			CommonsController.listUsers(req);
			CommonsController.listLots(req);
			req.setAttribute("action", "update");
			req.setAttribute("harvest", harvest);
			ControllerUtil.forward(req, resp, "/form-harvest.jsp");
			break;
		}
		default:
			listHarvests(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
			
			ControllerUtil.forward(req, resp, "/harvests.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getServletPath();
		
		switch (action) {
		case "/harvest/insert":
			insertHarvest(req);
			break;
		case "/harvest/update":
			updateHarvest(req);
			break;
		case "/harvest/delete":
			deleteHarvest(req);
			break;
		default:
			System.out.println("URL inválida " + action);
		}
		
		ControllerUtil.redirect(resp, req.getContextPath() + "/harvests");
	}

	private void listHarvests(HttpServletRequest req) {
		HarvestDAO dao = DAOFactory.createDAO(HarvestDAO.class);
		
		List<Harvest> harvests = null;
		try {
			int page = ControllerUtil.paginate(req, dao.count(), "/harvests");
			harvests = dao.listPage(page, ControllerUtil.PAGE_SIZE);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (harvests != null)
			req.setAttribute("harvests", harvests);
	}

	private Harvest loadHarvest(HttpServletRequest req) {
		try {
			int harvestId = Integer.parseInt(req.getParameter("harvestId"));
			
			HarvestDAO dao = DAOFactory.createDAO(HarvestDAO.class);
			return dao.findById(harvestId);
		} catch (NumberFormatException | ModelException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Monta a colheita a partir do formulário. Retorna null (e registra o erro) se inválida.
	private Harvest buildHarvest(HttpServletRequest req, int id) {
		String quality = req.getParameter("quality");
		String notes = req.getParameter("notes");
		
		int lotId;
		int bags;
		int userId;
		try {
			lotId = Integer.parseInt(req.getParameter("lot"));
			bags = Integer.parseInt(req.getParameter("bags"));
			userId = Integer.parseInt(req.getParameter("user"));
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Lote, sacas ou responsável inválido.");
			return null;
		}
		
		if (bags <= 0) {
			ControllerUtil.errorMessage(req, "A quantidade de sacas deve ser maior que zero.");
			return null;
		}
		
		if (quality == null || !QUALITIES.contains(quality)) {
			ControllerUtil.errorMessage(req, "Informe a qualidade do café.");
			return null;
		}
		
		java.util.Date date = ControllerUtil.formatDate(req.getParameter("harvestDate"));
		if (date == null) {
			ControllerUtil.errorMessage(req, "Informe uma data de colheita válida.");
			return null;
		}
		
		Harvest harvest = new Harvest(id);
		harvest.setCoffeeLot(new CoffeeLot(lotId));
		harvest.setHarvestDate(date);
		harvest.setBags(bags);
		harvest.setQuality(quality);
		harvest.setNotes(notes == null ? "" : notes.trim());
		harvest.setUser(new User(userId));
		
		return harvest;
	}

	private void insertHarvest(HttpServletRequest req) {
		Harvest harvest = buildHarvest(req, 0);
		if (harvest == null)
			return;
		
		HarvestDAO dao = DAOFactory.createDAO(HarvestDAO.class);
		
		try {
			if (dao.save(harvest)) {
				ControllerUtil.sucessMessage(req, "Colheita salva com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Colheita não pode ser salva.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateHarvest(HttpServletRequest req) {
		int id;
		try {
			id = Integer.parseInt(req.getParameter("harvestId"));
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Colheita inválida para alteração.");
			return;
		}
		
		Harvest harvest = buildHarvest(req, id);
		if (harvest == null)
			return;
		
		HarvestDAO dao = DAOFactory.createDAO(HarvestDAO.class);
		
		try {
			if (dao.update(harvest)) {
				ControllerUtil.sucessMessage(req, "Colheita atualizada com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Colheita não pode ser atualizada.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void deleteHarvest(HttpServletRequest req) {
		HarvestDAO dao = DAOFactory.createDAO(HarvestDAO.class);
		
		try {
			int harvestId = Integer.parseInt(req.getParameter("id"));
			
			Harvest harvest = dao.findById(harvestId);
			
			if (harvest == null)
				throw new ModelException("Colheita não encontrada para deleção.");
			
			if (dao.delete(harvest)) {
				ControllerUtil.sucessMessage(req, "Colheita deletada com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, "Colheita não pode ser deletada.");
			}
		} catch (NumberFormatException e) {
			ControllerUtil.errorMessage(req, "Colheita inválida para deleção.");
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}
