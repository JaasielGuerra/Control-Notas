package com.umg.controlnotas.services;


import com.umg.controlnotas.model.dto.LibroDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.LibroRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.umg.controlnotas.model.Libro;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Log
public class LibroServicelmpl implements LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UserFacade userFacade;

    @Autowired
  private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ResponseDataDto registrarNuevoLibro(LibroDto libroDto){

        Libro l = new Libro();
        l.setNombre(libroDto.getNombre());
        l.setDescripcion(libroDto.getDescripcion());
        l.setEstado(Libro.ESTADO_ACTIVO);
        l.setDisponibilidad(Libro.DISPONIBLE);
        l.setFechaCommit(LocalDate.now());
        l.setHoraCommit(LocalTime.now());
        l.setIdUsuario(usuarioRepository.getReferenceById(userFacade.getUserSession().getIdUsuario()));

        libroRepository.save(l);
        log.info("Libro registrado correctamente" + l);
        return ResponseDataDto.builder()
                .code(1)
                .data(libroDto)
                .message("Libro " + libroDto.getNombre() + " Registrado correctamente! ")
                .build();
    }
}
