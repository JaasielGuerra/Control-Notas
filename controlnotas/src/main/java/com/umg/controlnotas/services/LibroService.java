package com.umg.controlnotas.services;

import com.umg.controlnotas.model.dto.LibroDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LibroService {

    @Transactional
    ResponseDataDto registrarNuevoLibro(LibroDto libroDto);

    List<LibroDto> consultarLibros();

    @Transactional
    ResponseDataDto eliminarLibro(Long id);

    List<LibroDto> consultarLibrosDisponibles();

    LibroDto editarLibro(Long id);

    @Transactional
    ResponseDataDto actualizarLibro(LibroDto libro, Long id);
}
