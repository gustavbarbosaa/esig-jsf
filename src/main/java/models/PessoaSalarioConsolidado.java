package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "pessoa_salario_consolidado")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaSalarioConsolidado {
    @Id
    @Column(name = "pessoa_id")
    private int pessoaId;

    private String nome_pessoa;
    private String nome_cargo;
    private BigDecimal salario;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_id", insertable = false, updatable = false)
    private Pessoa pessoa;
}
