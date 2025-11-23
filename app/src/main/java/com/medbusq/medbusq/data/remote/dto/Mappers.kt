package com.medbusq.medbusq.data.remote.dto

import com.medbusq.medbusq.data.Usuario
import com.medbusq.medbusq.data.model.Medicamento
import com.medbusq.medbusq.data.model.UsuarioUIState

// -------- USUARIO --------

fun UsuarioDto.toEntity(): Usuario =
    Usuario(
        rut = rut,
        dv_rut = dv_rut,
        pnombre = pnombre,
        snombre = snombre,
        apaterno = apaterno,
        amaterno = amaterno,
        correo = correo,
        clave = clave
    )

fun Usuario.toDto(): UsuarioDto =
    UsuarioDto(
        rut = rut,
        dv_rut = dv_rut,
        pnombre = pnombre,
        snombre = snombre,
        apaterno = apaterno,
        amaterno = amaterno,
        correo = correo,
        clave = clave
    )

fun Usuario.toUIState(): UsuarioUIState =
    UsuarioUIState(
        rut = rut,
        dv_rut = dv_rut,
        pnombre = pnombre,
        snombre = snombre ?: "",
        apaterno = apaterno,
        amaterno = amaterno,
        correo = correo,
        clave = clave,
        confirmClave = ""
    )

fun UsuarioUIState.toDto(): UsuarioDto =
    UsuarioDto(
        rut = rut,
        dv_rut = dv_rut,
        pnombre = pnombre,
        snombre = snombre,
        apaterno = apaterno,
        amaterno = amaterno,
        correo = correo,
        clave = clave
    )

// -------- MEDICAMENTO --------

fun MedicamentoDto.toModel(): Medicamento =
    Medicamento(
        id = id,
        nombre = nombre,
        laboratorio = laboratorio,
        presentacion = presentacion,
        formaFarmaceutica = formaFarmaceutica
    )
