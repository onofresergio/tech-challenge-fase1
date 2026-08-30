package br.com.fiap.techchallenge.domain;


public enum TipoUsuario {
    DONO_RESTAURANTE(1,"Dono de Restaurante"),
    CLIENTE(2, "Cliente");

    private final int id;
    private final String descricao;

    TipoUsuario(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }


    public static TipoUsuario buscarPorId(int id) {
        for (TipoUsuario tipoUsuario : TipoUsuario.values()) {
            if (tipoUsuario.name() != null && tipoUsuario.getId() == id) {
                return tipoUsuario;
            }
        }
        throw new IllegalArgumentException("Id de usuário inválido: " + id);
    }
}
