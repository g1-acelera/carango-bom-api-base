package br.com.caelum.carangobom.model.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="veiculo")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String nome;
    
    @NotNull
    private double valor;
    
    @NotNull
    private int ano;
    
    @ManyToOne
    @JoinColumn(name="marca_id", nullable=false)
    private Marca marca;
    
    public Veiculo(String nome, double valor, int ano, Marca marca) {
    	this.ano = ano;
        this.nome = nome;
        this.valor = valor;
        this.marca = marca;
    }
}