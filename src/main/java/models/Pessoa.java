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
    private int id;

    @Column(nullable=false)
    private String nome;

    @Column(nullable=false)
    private String cidade;

    @Column(nullable=false)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @Column(nullable=true)
    private String senha;
}
