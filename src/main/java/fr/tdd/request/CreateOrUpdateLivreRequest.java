package fr.tdd.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateOrUpdateLivreRequest {

    @NotBlank
    private String isbn;
    @NotBlank
    private String titre;
    @NotBlank
    private String auteur;
    @NotBlank
    private String editeur;
    @NotBlank
    private String format;
    @NotBlank
    private boolean disponible;

}
