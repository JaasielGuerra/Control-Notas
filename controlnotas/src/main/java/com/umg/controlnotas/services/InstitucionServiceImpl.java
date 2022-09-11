package com.umg.controlnotas.services;

import com.umg.controlnotas.model.Institucion;
import com.umg.controlnotas.model.dto.InstitucionDto;
import com.umg.controlnotas.model.dto.ResponseDataDto;
import com.umg.controlnotas.repository.InstitucionRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalTime;

@Log
@Service
public class InstitucionServiceImpl implements InstitucionService {

    @Autowired
    private InstitucionRepository institucionRepository;

    /**
     * @param institucionDto para guardar los datos de la institucion
     * @param id
     * @return ResponseDataDto con el codigo de respuesta y el mensaje
     */
    @Override
    @Transactional
    public ResponseDataDto actualizarDatosInstitucion(InstitucionDto institucionDto, int id) {

        log.info("actualizando datos de la institucion");

        Institucion institucion = new Institucion();
        institucion.setId(id);
        institucion.setNombre(institucionDto.getNombre());
        institucion.setDireccion(institucionDto.getDireccion());
        institucion.setCodigo(institucionDto.getCodigo());
        institucion.setEstado(Institucion.ESTADO_ACTIVO);
        institucion.setFechaCommit(LocalDate.now());
        institucion.setHoraCommit(LocalTime.now());
        institucion.setNombreDirector(institucionDto.getNombreDirector());
        institucion.setCorreo(institucionDto.getCorreo());
        institucion.setTelefono(institucionDto.getTelefono());
        institucion.setNivel(institucionDto.getNivel());
        institucion.setSector(institucionDto.getSector());
        institucion.setJornada(institucionDto.getJornada());

        institucionRepository.save(institucion);

        institucionDto.setId(id);

        return ResponseDataDto.builder()
                .code(1)
                .data(institucionDto)
                .message("Datos de la institución actualizados correctamente!")
                .build();
    }

    @Override
    public InstitucionDto getInstitucion(int id) {

        log.info("consultando datos de la institucion");

        Institucion institucion = institucionRepository.findById(id).orElseThrow(() -> new NoResultException("No se encontró institución con id: " + id));

        InstitucionDto institucionDto = new InstitucionDto();
        institucionDto.setId(institucion.getId());
        institucionDto.setNombre(institucion.getNombre());
        institucionDto.setDireccion(institucion.getDireccion());
        institucionDto.setCodigo(institucion.getCodigo());
        institucionDto.setNombreDirector(institucion.getNombreDirector());
        institucionDto.setCorreo(institucion.getCorreo());
        institucionDto.setTelefono(institucion.getTelefono());
        institucionDto.setNivel(institucion.getNivel());
        institucionDto.setSector(institucion.getSector());
        institucionDto.setJornada(institucion.getJornada());
        return institucionDto;
    }
}
