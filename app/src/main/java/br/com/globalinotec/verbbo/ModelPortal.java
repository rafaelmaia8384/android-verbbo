package br.com.globalinotec.verbbo;

import com.google.gson.annotations.SerializedName;

public class ModelPortal {

    @SerializedName("id")
    private String id;
    @SerializedName("id_usuario")
    private String id_usuario;
    @SerializedName("portal_nome")
    private String portal_nome;
    @SerializedName("portal_resumo")
    private String portal_resumo;
    @SerializedName("portal_tema_cores")
    private int portal_tema_cores;
    @SerializedName("path_image")
    private String path_image;
    @SerializedName("createdAt")
    private String createdAt;

    public ModelPortal(String id, String id_usuario, String portal_nome, String portal_resumo, int portal_tema_cores, String path_image, String createdAt) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.portal_nome = portal_nome;
        this.portal_resumo = portal_resumo;
        this.portal_tema_cores = portal_tema_cores;
        this.path_image = path_image;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return id + ".verbbo.com.br";
    }

    public String getIdUsuario() {
        return id_usuario;
    }

    public String getPortalNome() {
        return portal_nome;
    }

    public String getPortalResumo() {
        return portal_resumo;
    }

    public int getPortalTemaCores() {
        return portal_tema_cores;
    }

    public String getPathImage() {
        return path_image;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
