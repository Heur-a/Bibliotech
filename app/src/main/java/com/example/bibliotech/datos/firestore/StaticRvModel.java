package com.example.bibliotech.datos.firestore;

import androidx.annotation.Nullable;

public class StaticRvModel {

    private String bk_name, dia_dev, mes_res, dia_res, año_res;
    private boolean invisible;
    private String bookId,reservaId;

    public StaticRvModel(String bk_name, String bookId, String dia_dev, String mes_res, String dia_res, String año_res, boolean invisible,  @Nullable String reservaId) {
        this.bk_name = bk_name;
        this.dia_dev = dia_dev;
        this.mes_res = mes_res;
        this.dia_res = dia_res;
        this.año_res = año_res;
        this.bookId = bookId;
        this.invisible = invisible;
        this.reservaId = reservaId;
    }

    public String getBk_name() {
        return bk_name;
    }

    public String getDia_dev() {
        return dia_dev;
    }

    public String getMes_res() {
        return mes_res;
    }

    public String getDia_res() {
        return dia_res;
    }

    public String getAño_res() {
        return año_res;
    }

    public boolean isInvisible() {
        return invisible;
    }
    public String getBookId() {
        return bookId;
    }

    public String getReservaId() {
        return reservaId;
    }

    public void setReservaId(String reservaId) {
        this.reservaId = reservaId;
    }
}
