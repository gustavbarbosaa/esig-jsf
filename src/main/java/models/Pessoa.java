package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="pessoa")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false)
    private String cidade;

    @Column(nullable=false, unique = true)
    private String email;

    @Column(nullable=false)
    private String cep;

    @Column(nullable=false)
    private String endereco;

    @Column(nullable=false)
    private String pais;

    @Column(nullable=false)
    private String usuario;

    @Column(nullable=false)
    private String telefone;

    @Column(nullable=false)
    private LocalDate dataNascimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargo cargo;
}
