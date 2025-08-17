package security;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"*.xhtml", "/pages/*"})
public class ControleAutenticacao implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String loginURI = req.getContextPath() + "/pages/login.xhtml";
        String cadastrarSenhaURI = req.getContextPath() + "/pages/cadastrarSenha.xhtml";

        String requestURI = req.getRequestURI();

        boolean loggedIn = (session != null && session.getAttribute("usuarioLogado") != null);
        boolean loginRequest = requestURI.equals(loginURI);
        boolean cadastrarSenhaRequest = requestURI.equals(cadastrarSenhaURI);

        boolean resourceRequest = requestURI.contains("javax.faces.resource")
                || requestURI.startsWith(req.getContextPath() + "/resources/");

        if (loggedIn || loginRequest || cadastrarSenhaRequest || resourceRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }
}
