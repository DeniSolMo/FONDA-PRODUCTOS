package itch.tecnm.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import itch.tecnm.proyecto.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	
	 List<Producto> findByEstadoTrue();

}
