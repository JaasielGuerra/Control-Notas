package com.umg.controlnotas.services;


import com.umg.controlnotas.model.Libro;
import com.umg.controlnotas.model.dto.LibroDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.model.query.ConsultaEditarLibro;
import com.umg.controlnotas.repository.LibroRepository;
import com.umg.controlnotas.repository.UsuarioRepository;
import com.umg.controlnotas.web.UserFacade;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


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
        l.setCondicionLibro(libroDto.getCondicion());

        libroRepository.save(l);
        log.info("Libro registrado correctamente" + l);
        return ResponseDataDto.builder()
                .code(1)
                .data(libroDto)
                .message("Libro " + libroDto.getNombre() + " Registrado correctamente! ")
                .build();
    }

    @Override
    public List<LibroDto> consultarLibros(){
            return libroRepository.findByEstado(
                    Libro.ESTADO_ACTIVO
                    )

                    .stream()
                    .map(libros -> LibroDto.builder()
                            .id(libros.getId())
                            .nombre(libros.getNombre())
                            .descripcion(libros.getDescripcion())
                            .disponibilidad(libros.getDisponibilidad())
                            .condicion(libros.getCondicionLibro())
                            .descripcionCondicion(obtenerDescripcionCondicion(libros.getCondicionLibro()))
                            .build()
                    )
                    .collect(Collectors.toList());


    }

    /** Obtener libros disponibles para prestamo
     * @return
     */
    @Override
    public List<LibroDto> consultarLibrosDisponibles() {
        return libroRepository.findByEstadoAndDisponibilidad(
                        Libro.ESTADO_ACTIVO,
                        Libro.DISPONIBLE
                )

                .stream()
                .map(libros -> LibroDto.builder()
                        .id(libros.getId())
                        .nombre(libros.getNombre())
                        .descripcion(libros.getDescripcion())
                        .disponibilidad(libros.getDisponibilidad())
                        .condicion(libros.getCondicionLibro())
                        .descripcionCondicion(obtenerDescripcionCondicion(libros.getCondicionLibro()))
                        .build()
                )
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ResponseDataDto eliminarLibro(Long id){
        libroRepository.eliminarLibroById(id);
        log.info("Libro eliminado con exito");

        return ResponseDataDto.builder()
                .message("Libro eliminado correctamente!")
                .code(ResponseDataDto.SUCCESS)
                .build();
    }

    /**
     * obtener datos de un libro para editar
     * @param id  id del libro
     * @return libroDto
     */
    @Override
    public LibroDto editarLibro(Long id) {

        ConsultaEditarLibro libro = libroRepository.obtenerLibroEditar(id);

        log.info("Libro encontrado: " + libro);

        if(libro == null){
            throw new NoSuchElementException("No se encontro el libro");
        }

        return LibroDto.from(libro);
    }

    /**
     * Actualizar datos de un libro
     * @param libro  libro a actualizar
     * @return ResponseDataDto
     */
    @Override
    @Transactional
    public ResponseDataDto actualizarLibro(LibroDto libro, Long id) {

        Libro libroDb = libroRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No se encontro el libro a actualizar"));
        libroDb.setNombre(libro.getNombre());
        libroDb.setDescripcion(libro.getDescripcion());
        libroDb.setCondicionLibro(libro.getCondicion());

        libroRepository.save(libroDb);

        return ResponseDataDto.builder()
                .code(1)
                .data(libro)
                .message("Libro " + libro.getNombre() + " actualizado correctamente! ")
                .build();
    }

    private String obtenerDescripcionCondicion(Integer condicion){
        switch (condicion){
            case Libro.CONDICION_BUEN_ESTADO:
                return "BUEN ESTADO";
            case Libro.CONDICION_PERDIDO:
                return "PERDIDO";
            case Libro.CONDICION_DETERIORADO:
                return "DETERIORADO";
            default:
                return "DESCONOCIDO";
        }
    }
}
