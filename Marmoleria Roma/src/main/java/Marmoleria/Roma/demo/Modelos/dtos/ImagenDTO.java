package Marmoleria.Roma.demo.Modelos.dtos;

public class ImagenDTO {
    private Long idImagen;
    private Long version;
    private int numeroDeImagenDelPedido;
    private Long idPedido;
    private byte[] imagen;

    public Long getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Long idImagen) {
        this.idImagen = idImagen;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public int getNumeroDeImagenDelPedido() {
        return numeroDeImagenDelPedido;
    }

    public void setNumeroDeImagenDelPedido(int numeroDeImagenDelPedido) {
        this.numeroDeImagenDelPedido = numeroDeImagenDelPedido;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
