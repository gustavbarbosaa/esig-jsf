package bean.pessoaSalario;

import dao.PessoaDAO;
import lombok.Data;
import models.Pessoa;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named
@ViewScoped
@Data
public class CadastrarSenhaBean implements Serializable {

    @Inject
    private PessoaDAO pessoaDAO;

    private Pessoa pessoa;
    private String novaSenha;
    private String confirmarSenha;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String usuario = params.get("usuario");

        if (usuario != null && !usuario.isEmpty()) {
            this.pessoa = pessoaDAO.getByUsername(usuario);
        }
    }

    public String cadastrarSenha() {
        if (!novaSenha.equals(confirmarSenha)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "As senhas não coincidem.", null));
            return null;
        }

        if (this.pessoa != null) {
            String senhaCodificada = passwordEncoder.encode(novaSenha);

            pessoa.setSenha(senhaCodificada);
            pessoaDAO.saveOrUpdate(this.pessoa);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha cadastrada com sucesso!", null));

            return "login?faces-redirect=true";
        }

        return null;
    }
}

