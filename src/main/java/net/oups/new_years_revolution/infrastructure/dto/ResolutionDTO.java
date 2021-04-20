package net.oups.new_years_revolution.infrastructure.dto;

import com.sun.istack.NotNull;

public class ResolutionDTO {

    @NotNull
    private String contenu;

    @NotNull
    private Integer occurence;

    @NotNull
    private Boolean periode;


    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }

    public Boolean getPeriode() {
        return periode;
    }

    public void setPeriode(Boolean periode) {
        this.periode = periode;
    }
}
