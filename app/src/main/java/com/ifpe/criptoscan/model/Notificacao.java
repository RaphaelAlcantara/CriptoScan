package com.ifpe.criptoscan.model;

public class Notificacao {
    private Alerta alerta;
    private String mensagem;
    private boolean lido;

    public Notificacao(Alerta alerta, String mensagem) {
        this.alerta = alerta;
        this.mensagem = mensagem;
        this.lido = false;
    }

    public Notificacao() {
    }

    public Alerta getAlerta() {
        return alerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }
}
