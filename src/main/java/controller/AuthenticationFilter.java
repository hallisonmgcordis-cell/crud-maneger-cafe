package controller;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Bloqueia o acesso à aplicação para quem não fez login. Somente a página de
 * login e os recursos estáticos (css, js, imagens, fontes) são públicos.
 */
@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

	static final String LOGGED_USER_KEY = "loggedUser";
	static final String REDIRECT_AFTER_LOGIN_KEY = "redirectAfterLogin";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, 
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse resp = (HttpServletResponse) servletResponse;
		
		String path = req.getServletPath();
		
		if (isPublic(path) || isLogged(req)) {
			chain.doFilter(req, resp);
			return;
		}
		
		// Guarda a página pedida para voltar a ela depois do login
		if ("GET".equals(req.getMethod())) {
			String target = req.getRequestURI();
			if (req.getQueryString() != null)
				target += "?" + req.getQueryString();
			req.getSession().setAttribute(REDIRECT_AFTER_LOGIN_KEY, target);
		}
		
		resp.sendRedirect(req.getContextPath() + "/login");
	}

	private boolean isLogged(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		return session != null && session.getAttribute(LOGGED_USER_KEY) != null;
	}

	private boolean isPublic(String path) {
		return path.equals("/login") 
				|| path.startsWith("/css/") 
				|| path.startsWith("/js/")
				|| path.startsWith("/images/") 
				|| path.startsWith("/fonts/");
	}
}
