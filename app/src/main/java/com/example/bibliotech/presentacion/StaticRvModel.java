package com.example.bibliotech.presentacion;

public class StaticRvModel {

    private String bk_name, dia_dev, mes_res, dia_res, año_res;
    private boolean invisible;

    public StaticRvModel(String bk_name, String dia_dev, String mes_res, String dia_res, String año_res, boolean invisible) {
        this.bk_name = bk_name;
        this.dia_dev = dia_dev;
        this.mes_res = mes_res;
        this.dia_res = dia_res;
        this.año_res = año_res;
        this.invisible = invisible;
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
}
