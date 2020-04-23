package br.com.globalinotec.verbbo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelSignedUrls {

    @SerializedName("signedUrls")
    private List<String> signedUrls;

    public ModelSignedUrls(List<String> signedUrls) {

        this.signedUrls = signedUrls;
    }

    public List<String> getSignedContent() {

        return this.signedUrls;
    }
}
