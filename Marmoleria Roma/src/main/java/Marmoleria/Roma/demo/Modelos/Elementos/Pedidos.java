package Marmoleria.Roma.demo.Modelos.Elementos;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_pedido")
    @SequenceGenerator(name = "id_pedido", sequenceName = "id_pedido", allocationSize = 1)
    private Long id;

    public Pedidos(){}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
