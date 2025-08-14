package models;

import enums.TipoVencimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="vencimento")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vencimento {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String descricao;

    @Column(nullable=false)
    private BigDecimal valor;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private TipoVencimento tipoVencimento;

    @OneToMany(mappedBy = "vencimento")
    private List<CargoVencimento> cargos = new ArrayList<>();
}
