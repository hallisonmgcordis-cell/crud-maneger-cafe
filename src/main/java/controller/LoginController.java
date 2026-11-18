package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ModelException;
import model.PasswordUtil;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		if ("/logout".equals(req.getServletPath())) {
			HttpSession session = req.getSession(false);
			if (session != null)
				session.invalidate();
			
			ControllerUtil.redirect(resp, req.getContextPath() + "/login");
			return;
		}
		
		// Quem já está logado não precisa ver a tela de login
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute(AuthenticationFilter.LOGGED_USER_KEY) != null) {
			ControllerUtil.redirect(resp, req.getContextPath() + "/posts");
			return;
		}
		
		ControllerUtil.transferSessionMessagesToRequest(req);
		ControllerUtil.forward(req, resp, "/login.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		User user = authenticate(email, password);
		
		if (user == null) {
			// Mensagem genérica: não revela se o e-mail existe
			req.setAttribute("message", "E-mail ou senha inválidos.");
			req.setAttribute("alertType", 0);
			req.setAttribute("email", email);
			ControllerUtil.forward(req, resp, "/login.jsp");
			return;
		}
		
		// Nunca guarda o hash da senha na sessão
		user.setPassword(null);
		
		// Novo id de sessão após autenticar (evita session fixation)
		HttpSession session = req.getSession();
		req.changeSessionId();
		session.setAttribute(AuthenticationFilter.LOGGED_USER_KEY, user);
		
		String target = (String) session.getAttribute(AuthenticationFilter.REDIRECT_AFTER_LOGIN_KEY);
		session.removeAttribute(AuthenticationFilter.REDIRECT_AFTER_LOGIN_KEY);
		
		if (target == null || !target.startsWith(req.getContextPath() + "/"))
			target = req.getContextPath() + "/posts";
		
		ControllerUtil.redirect(resp, target);
	}

	private User authenticate(String email, String password) {
		if (email == null || password == null)
			return null;
		
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		
		try {
			User user = dao.findByEmail(email.trim());
			
			if (user != null && PasswordUtil.verify(password, user.getPassword()))
				return user;
		} catch (ModelException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
