package com.umg.controlnotas.model.custom;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class AlumnoJSON {

    public AlumnoJSON() {
    }

    private String codigo;
    private String nombre;
    private String apellido;
    private String direccion;
    private LocalDate nacimiento;
    private long grado;
    private long seccion;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(LocalDate nacimiento) {
        this.nacimiento = nacimiento;
    }

    public long getGrado() {
        return grado;
    }

    public void setGrado(long grado) {
        this.grado = grado;
    }

    public long getSeccion() {
        return seccion;
    }

    public void setSeccion(long seccion) {
        this.seccion = seccion;
    }
}
