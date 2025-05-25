package com.reservas.vehiculos.institucionales.dto;

import java.math.BigDecimal;

public class ReparacionDTO {
    private Long idAuto;
    private BigDecimal costo;
    private String descripcion;
    private String docFactura;

    public Long getIdAuto() {
        return idAuto;
    }

    public void setIdAuto(Long idAuto) {
        this.idAuto = idAuto;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDocFactura() {
        return docFactura;
    }

    public void setDocFactura(String docFactura) {
        this.docFactura = docFactura;
    }
}
