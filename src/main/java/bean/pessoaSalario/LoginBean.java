package bean.pessoaSalario;

import lombok.Data;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Named
@Data
@RequestScoped
public class LoginBean implements Serializable {
    private String usuario;
    private String senha;

    public String autenticar() {
        if ("admin".equals(usuario) && "admin".equals(senha)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login bem-sucedido!", "Bem-vindo, " + usuario));

            return "/index.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falhou!", "Verifique o usuário e a senha."));

            return null;
        }
    }
}
