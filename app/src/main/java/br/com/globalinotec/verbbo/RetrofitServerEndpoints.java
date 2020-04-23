package br.com.globalinotec.verbbo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RetrofitServerEndpoints {

    //Generico

    @PUT
    Call<Void> enviarImagem(
            @Url String url,
            @Body RequestBody body
    );

    //Usuários

    @Headers({"Cache-Control: no-cache"})
    @FormUrlEncoded
    @POST("usuarios/cadastrar")
    Call<ModelRequest> usuariosCadastrar(
            @Field("publicante_nome") String publicante_nome,
            @Field("publicante_resumo") String publicante_resumo,
            @Field("publicante_nome_completo") String publicante_nome_completo,
            @Field("publicante_cpf") String publicante_cpf,
            @Field("publicante_data_nascimento") String data_nascimento,
            @Field("publicante_telefone") String publicante_telefone,
            @Field("email") String email,
            @Field("senha_hash") String senha
    );

    @Headers({"Cache-Control: no-cache"})
    @FormUrlEncoded
    @POST("usuarios/login")
    Call<ModelRequest> usuariosLogin(
            @Field("email") String email,
            @Field("senha") String password
    );

    @Headers({"Cache-Control: no-cache"})
    @FormUrlEncoded
    @POST("usuarios/atualizarperfil")
    Call<ModelRequest> usuariosAtualizarPerfil(
            @Header("verbbo-token") String token,
            @Field("path_image") boolean novaImagem,
            @Field("publicante_nome") String nome,
            @Field("publicante_resumo") String resumo,
            @Field("publicante_telefone") String telefone
    );

    @Headers({"Cache-Control: no-cache"})
    @FormUrlEncoded
    @POST("usuarios/alterarsenha")
    Call<ModelRequest> usuariosAlterarSenha(
            @Header("verbbo-token") String token,
            @Field("senha_antiga") String senha_antiga,
            @Field("senha_nova") String senha
    );

    //Portais

    @Headers({"Cache-Control: no-cache"})
    @FormUrlEncoded
    @POST("portais/criar")
    Call<ModelRequest> portaisCriar(
            @Header("verbbo-token") String token,
            @Field("id") String slug,
            @Field("portal_nome") String nome,
            @Field("portal_resumo") String resumo,
            @Field("portal_tema_cores") int tema
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("portais/meusportais")
    Call<ModelRequest> portaisMeusPortais(
            @Header("verbbo-token") String token
    );

    @Headers({"Cache-Control: no-cache"})
    @Multipart
    @POST("usuarios/upload")
    Call<ModelRequest> usuariosUpload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part object
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("usuarios/estatisticas")
    Call<ModelRequest> usuariosEstatisticas(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken
    );

    //Pessoas

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/ultimos/{ultimaData}")
    Call<ModelRequest> pessoasUltimos(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("ultimaData") String ultimaData
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/meuscadastros/{ultimaData}")
    Call<ModelRequest> pessoasMeusCadastros(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("ultimaData") String ultimaData
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/busca/{pagina}/{parametros}")
    Call<ModelRequest> pessoasBuscaSimples(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("pagina") int pagina,
            @Path("parametros") String parametros
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/buscaautocomplete/{parametros}")
    Call<ModelRequest> pessoasBuscaAutoComplete(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("parametros") String parametros
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/buscaautocompletevinculo/{id}/{parametros}")
    Call<ModelRequest> pessoasBuscaAutoCompleteVinculo(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id_pessoa,
            @Path("parametros") String parametros
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/perfil/{id}")
    Call<ModelRequest> pessoasPerfil(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @DELETE("pessoas/excluir/{id}")
    Call<ModelRequest> pessoasExcluir(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @DELETE("pessoas/excluir/{id}/{item}/{id_item}")
    Call<ModelRequest> pessoasExcluirItem(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id,
            @Path("item") String item,
            @Path("id_item") String id_tem
    );

    @Headers({"Cache-Control: no-cache"})
    @Multipart
    @POST("pessoas/adicionar/{id}/imagens/")
    Call<ModelRequest> pessoasAdicionarImagens(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Part("description") RequestBody description,
            @Part List<MultipartBody.Part> objects
    );

    @Headers({"Cache-Control: no-cache"})
    @POST("pessoas/adicionar/{id}/{item}")
    Call<ModelRequest> pessoasAdicionarItem(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id,
            @Path("item") String item,
            @Body RequestBody body
    );

    @Headers({"Cache-Control: no-cache"})
    @POST("pessoas/buscaavancada/{pagina}")
    Call<ModelRequest> pessoasBuscaAvancada(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("pagina") int pagina,
            @Body RequestBody body
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/relatorio/sai/{id}")
    Call<ModelRequest> pessoasRelatorioSAI(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/relatorio/sasp/{id}")
    Call<ModelRequest> pessoasRelatorioSASP(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/relatorio/sccd/{id}")
    Call<ModelRequest> pessoasRelatorioSCCD(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/relatorio/mif/{id}")
    Call<ModelRequest> pessoasRelatorioMIF(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/relatorio/scrap/{id}")
    Call<ModelRequest> pessoasRelatorioSCRAP(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("pessoas/relatorio/sac/{id}")
    Call<ModelRequest> pessoasRelatorioSAC(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    //Veículos

    @Headers({"Cache-Control: no-cache"})
    @GET("veiculos/ultimos/{ultimaData}")
    Call<ModelRequest> veiculosUltimos(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("ultimaData") String ultimaData
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("veiculos/perfil/{placa}")
    Call<ModelRequest> veiculosPerfil(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("placa") String placa
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("veiculos/busca/{pagina}/{parametros}")
    Call<ModelRequest> veiculosBuscaSimples(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("pagina") int pagina,
            @Path("parametros") String parametros
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("veiculos/buscaautocomplete/{parametros}")
    Call<ModelRequest> veiculosBuscaAutoComplete(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("parametros") String parametros
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("veiculos/meuscadastros/{ultimaData}")
    Call<ModelRequest> veiculosMeusCadastros(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("ultimaData") String ultimaData
    );

    @Headers({"Cache-Control: no-cache"})
    @DELETE("veiculos/excluir/{placa}")
    Call<ModelRequest> veiculosExcluir(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("placa") String placa
    );

    @Headers({"Cache-Control: no-cache"})
    @POST("veiculos/adicionar/{placa}/{item}")
    Call<ModelRequest> veiculosAdicionarItem(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("placa") String placa,
            @Path("item") String item,
            @Body RequestBody body
    );

    @Headers({"Cache-Control: no-cache"})
    @DELETE("veiculos/excluir/{placa}/{item}/{id_item}")
    Call<ModelRequest> veiculosExcluirItem(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("placa") String placa,
            @Path("item") String item,
            @Path("id_item") String id_tem
    );

    @Headers({"Cache-Control: no-cache"})
    @Multipart
    @POST("veiculos/adicionar/{placa}/imagens/")
    Call<ModelRequest> veiculosAdicionarImagens(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Part("description") RequestBody description,
            @Part List<MultipartBody.Part> objects
    );

    //Abordagens

    @Headers({"Cache-Control: no-cache"})
    @GET("abordagens/ultimos/{ultimaData}")
    Call<ModelRequest> abordagensUltimos(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("ultimaData") String ultimaData
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("abordagens/meuscadastros/{ultimaData}")
    Call<ModelRequest> abordagensMeusCadastros(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("ultimaData") String ultimaData
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("abordagens/perfil/{id}")
    Call<ModelRequest> abordagensPerfil(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("abordagens/busca/{pagina}/{parametros}")
    Call<ModelRequest> abordagensBuscaSimples(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("pagina") int pagina,
            @Path("parametros") String parametros
    );

    @Headers({"Cache-Control: no-cache"})
    @DELETE("abordagens/excluir/{id}")
    Call<ModelRequest> abordagensExcluir(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @POST("abordagens/buscaavancada/{pagina}")
    Call<ModelRequest> abordagensBuscaAvancada(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("pagina") int pagina,
            @Body RequestBody body
    );

    //Informes

    @Headers({"Cache-Control: no-cache"})
    @GET("informes/meuscadastros/{senha}/{ultimaData}")
    Call<ModelRequest> informesMeusCadastros(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("senha") String senha,
            @Path("ultimaData") String ultimaData
    );

    @Headers({"Cache-Control: no-cache"})
    @GET("informes/perfil/{id}")
    Call<ModelRequest> informesPerfil(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );

    @Headers({"Cache-Control: no-cache"})
    @DELETE("informes/excluir/{id}")
    Call<ModelRequest> informesExcluir(
            @Header("sasp-token") String saspToken,
            @Header("device-token") String deviceToken,
            @Path("id") String id
    );
}
