package itch.tecnm.proyecto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itch.tecnm.proyecto.dto.ProductoDto;
import itch.tecnm.proyecto.service.ProductoService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
//@CrossOrigin("*")
@RequestMapping("/api/producto")
public class ProductoController {
	//Inyección de dependencia
		private ProductoService productoService;
		
		//Construir el REST API para agregar un producto
		@PostMapping
		public ResponseEntity<ProductoDto> crearProducto(@RequestBody ProductoDto productoDto){
			ProductoDto guardarProducto = productoService.createProducto(productoDto);
			//Respose entity permite utilicar información a traves de http
			return new ResponseEntity<>(guardarProducto, HttpStatus.CREATED);
		}
		
		//Construir el get del producto REST API
				@GetMapping("{id}")
				public ResponseEntity<ProductoDto> getProductoById(@PathVariable("id") Integer productoId){
					
					ProductoDto productoDto = productoService.getProductoById(productoId);
					return ResponseEntity.ok(productoDto);
				}

			//REST API. Construir el get para todos los productos
			@GetMapping 
			public ResponseEntity<List<ProductoDto>> getAllProductos(){
				List<ProductoDto> productos = productoService.getAllProductos();
				return ResponseEntity.ok(productos);
			}
			
			// Construir REST API Update Producto.
				// Exponer un endpoint HTTP PUT para actualizar un producto
				@PutMapping("{id}")
				public ResponseEntity<ProductoDto> updateProducto(@PathVariable("id") Integer id,
						@RequestBody ProductoDto updateProducto) {
					ProductoDto productoDto = productoService.updateProducto(id, updateProducto);
					return ResponseEntity.ok(productoDto);
				}
			
			//Construir delete producto REST API
				@DeleteMapping("{id}")
				public ResponseEntity<String> deleteProducto(@PathVariable("id") Integer productoId){
					productoService.deleteProducto(productoId);
					return ResponseEntity.ok("Registro de producto eliminado");
				}
				
				
				// 2. ENDPOINT DE GESTIÓN: Devuelve TODOS los productos (activos e inactivos)
			    @GetMapping("/todos")
			    public ResponseEntity<List<ProductoDto>> getAllProductosForAdmin(){
			        List<ProductoDto> productos = productoService.getAllProductosForAdmin();
			        return ResponseEntity.ok(productos);
			    }
}
