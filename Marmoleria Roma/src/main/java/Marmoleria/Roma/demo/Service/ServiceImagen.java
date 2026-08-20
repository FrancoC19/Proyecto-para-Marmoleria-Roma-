package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Elementos.Pedidos;
import Marmoleria.Roma.demo.Modelos.Extras.Imagen;
import Marmoleria.Roma.demo.Modelos.dtos.ImagenDTO;
import Marmoleria.Roma.demo.Repository.RepositoryImagen;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceImagen {

    @Autowired
    private RepositoryImagen repositoryImagen;

    public void guardarImagen(String base64, Pedidos pedido) {

        byte[] imagenBytes = convertirBase64(base64);

        for (int i = 0; i <= 3; i++) {

            try {
                Imagen imagen = new Imagen();

                imagen.setPedido(pedido);
                imagen.setImagen(imagenBytes);

                int numero = repositoryImagen.countByPedido(pedido) + 1;

                imagen.setNumeroDeImagenDelPedido(numero);

                repositoryImagen.saveAndFlush(imagen);

                return;

            } catch (DataIntegrityViolationException e) {

                System.out.println(
                        "⚠️ CONFLICTO EN INTENTO " + (i + 1)
                );

                System.out.println(
                        "⚠️ MENSAJE: " + e.getMessage()
                );
            }
        }

        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "No se pudo asignar un número a la imagen debido a una modificación concurrente"
        );
    }

    @Transactional
    public void actualizarImagen(Imagen imagen, String base64) {
        try {
            imagen.setImagen(convertirBase64(base64));

            repositoryImagen.saveAndFlush(imagen);
        } catch (OptimisticLockingFailureException e) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La imagen fue modificada por otro usuario"
            );
        }
    }

    public List<ImagenDTO> obtenerImagenesDePedido(Pedidos pedido){
        List<Imagen> imagenes=repositoryImagen.findByPedidoOrderByNumeroDeImagenDelPedidoAsc(pedido);
        return  imagenes.stream().map(this::convertirADTO).toList();
    }

    @Transactional
    public void eliminarImagen(Imagen imagen) {
        repositoryImagen.delete(imagen);
        List<Imagen> imagenes= repositoryImagen.findByPedido(imagen.getPedido());
        if (!imagenes.isEmpty()) {
            for (int i=0; i<imagenes.size(); i++) {
                imagenes.get(i).setNumeroDeImagenDelPedido(i+1);
            }
        }
    }

    public ImagenDTO obtenerImagenDePedido(Pedidos pedido, int numero) {
        Imagen imagen=repositoryImagen
                .findByPedidoAndNumeroDeImagenDelPedido(pedido, numero)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No se encontró la imagen"
                        ));

        return convertirADTO(imagen);
    }

    public Imagen obtenerImagenEntidad(Pedidos pedido, int numero) {
        return repositoryImagen
                .findByPedidoAndNumeroDeImagenDelPedido(pedido, numero)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No se encontró la imagen"
                        )
                );
    }

    private ImagenDTO convertirADTO(Imagen imagen) {

        ImagenDTO dto = new ImagenDTO();

        dto.setIdImagen(imagen.getIdImagen());
        dto.setVersion(imagen.getVersion());
        dto.setNumeroDeImagenDelPedido(
                imagen.getNumeroDeImagenDelPedido()
        );
        dto.setIdPedido(
                imagen.getPedido().getIdPedido()
        );
        dto.setImagen(imagen.getImagen());

        return dto;
    }

    private byte[] convertirBase64(String base64) {

        if (base64 == null || base64.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La imagen no puede estar vacía"
            );
        }

        try {

            if (base64.contains(",")) {
                base64 = base64.substring(base64.indexOf(",") + 1);
            }

            // IMPORTANTE: eliminar comillas si vienen desde Postman
            base64 = base64.replace("\"", "").trim();

            return Base64.getDecoder().decode(base64);

        } catch (IllegalArgumentException e) {

            System.out.println("ERROR DECODIFICANDO BASE64: " + e.getMessage());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La imagen enviada no es un Base64 válido"
            );
        }
    }
}
