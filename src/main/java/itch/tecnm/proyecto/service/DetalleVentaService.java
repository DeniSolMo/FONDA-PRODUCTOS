package itch.tecnm.proyecto.service;

import java.util.List;

import itch.tecnm.proyecto.dto.DetalleVentaDto;

public interface DetalleVentaService {
	// Consulta un detalle por id
    DetalleVentaDto getDetalleById(Integer detalleId);

    // Lista todos los detalles
    List<DetalleVentaDto> getAllDetallesVenta();

    // Lista los detalles de una venta específica
    List<DetalleVentaDto> getDetallesByVenta(Integer idVenta);
}
