package bean.pessoaSalario;

import dao.PessoaDAO;
import lombok.Data;
import models.Pessoa;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@Data
@RequestScoped
public class LoginBean implements Serializable {
    @Inject
    private PessoaDAO pessoaDAO;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private String usuario;
    private String senha;

    public String autenticar() {
        Pessoa pessoa = this.pessoaDAO.getByUsername(this.usuario);

        if (pessoa == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falhou!", "Usuário ou senha inválidos."));
            return null;
        }

        if (pessoa.getSenha() == null || pessoa.getSenha().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Primeiro acesso!", "Crie sua senha para continuar."));

            return "cadastrarSenha.xhtml?faces-redirect=true&usuario=" + this.usuario;
        }

        if (encoder.matches(this.senha, pessoa.getSenha())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Login bem-sucedido!", "Bem-vindo, " + usuario));

            return "/index.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login falhou!", "Usuário ou senha inválidos."));
            return null;
        }
    }
}
