package Marmoleria.Roma.demo.Service;

import Marmoleria.Roma.demo.Modelos.Enumeradores.Moneda;
import Marmoleria.Roma.demo.Modelos.Enumeradores.TipoCalculo;
import Marmoleria.Roma.demo.Modelos.Extras.ItemAdicional;
import Marmoleria.Roma.demo.Repository.RepositoryItemAdicional;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceItemAdicional {
    @Autowired
    private RepositoryItemAdicional repositoryItemAdicional;

    public List<ItemAdicional> todosLosItems() {
        return repositoryItemAdicional.findAll();
    }

    public List<ItemAdicional> itemsActivos() {
        return repositoryItemAdicional.findByActivoTrue();
    }

    public ItemAdicional buscarPorId(long id) {
        return repositoryItemAdicional.findById(id).orElse(null);
    }

    public ItemAdicional guardarItem(ItemAdicional item) {
        return repositoryItemAdicional.save(item);
    }

    @Transactional
    public ItemAdicional modificarItem(long id, String nombre, TipoCalculo tipoCalculo, Float precio, Moneda moneda) {
        ItemAdicional item = repositoryItemAdicional.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ítem no encontrado"));
        item.setNombre(nombre);
        item.setTipoCalculo(tipoCalculo);
        item.setPrecio(precio);
        item.setMoneda(moneda);
        return repositoryItemAdicional.saveAndFlush(item);
    }

    @Transactional
    public ItemAdicional cambiarEstado(long id, boolean activo) {
        ItemAdicional item = repositoryItemAdicional.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ítem no encontrado"));
        item.setActivo(activo);
        return repositoryItemAdicional.saveAndFlush(item);
    }
}
