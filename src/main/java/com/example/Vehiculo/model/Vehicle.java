package com.example.Vehiculo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "vehiculos")
public class Vehicle {
    @Id
    private String id;

    @NotBlank
    @Size(max = 50)
    private String marca;

    @NotBlank
    @Size(max = 50)
    private String modelo;

    @NotNull
    private Integer anio;

    @Indexed(unique = true)
    @NotBlank
    @Size(max = 20)
    private String placa;

    @Size(max = 30)
    private String color;

    public Vehicle() {}

    public Vehicle(String id, String marca, String modelo, Integer anio, String placa, String color) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.placa = placa;
        this.color = color;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}