package br.com.globalinotec.verbbo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelUsuario {

    @SerializedName("publicante_nome")
    private String publicante_nome;
    @SerializedName("publicante_resumo")
    private String publicante_resumo;
    @SerializedName("publicante_nome_completo")
    private String publicante_nome_completo;
    @SerializedName("publicante_cpf")
    private String publicante_cpf;
    @SerializedName("publicante_data_nascimento")
    private String publicante_data_nascimento;
    @SerializedName("publicante_telefone")
    private String publicante_telefone;
    @SerializedName("email")
    private String email;
    @SerializedName("path_image")
    private String path_image;
    @SerializedName("token")
    private String token;

    public ModelUsuario(String publicante_nome, String publicante_resumo, String publicante_nome_completo, String publicante_cpf, String publicante_data_nascimento, String publicante_telefone, String email, String path_image, String token) {

        this.publicante_nome = publicante_nome;
        this.publicante_resumo = publicante_resumo;
        this.publicante_nome_completo = publicante_nome_completo;
        this.publicante_cpf = publicante_cpf;
        this.publicante_data_nascimento = publicante_data_nascimento;
        this.publicante_telefone = publicante_telefone;
        this.email = email;
        this.path_image = path_image;
        this.token = token;
    }

    public String getPublicanteNome() {

        return publicante_nome;
    }

    public String getPublicanteResumo() {

        return publicante_resumo;
    }

    public String getPublicanteNomeCompleto() {

        return publicante_nome_completo;
    }

    public String getPublicanteCPF() {

        return publicante_cpf;
    }

    public String getPublicanteDataNascimento() {

        return publicante_data_nascimento;
    }

    public String getPublicanteTelefone() {

        return publicante_telefone;
    }

    public String getEmail() {

        return email;
    }

    public String getPathImage() {

        return path_image;
    }

    public void setPathImage(String path) {

        path_image = path;
    }

    public String getToken() {

        return token;
    }
}
