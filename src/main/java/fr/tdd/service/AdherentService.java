package fr.tdd.service;

import org.springframework.stereotype.Service;

import fr.tdd.exception.AdherentNotFoundException;
import fr.tdd.model.Adherent;
import fr.tdd.repository.AdherentRepository;

@Service
public class AdherentService {
    private final AdherentRepository adherentRepository;

    public AdherentService(AdherentRepository adherentRepository) {
        this.adherentRepository = adherentRepository;
    }

    public Adherent getAdherentById(String id) {
        try {
            return adherentRepository.findById(id).get();
        } catch (Exception e) {
            throw new AdherentNotFoundException("Adherent not found");
        }
    }

    public Adherent createAdherent(Adherent adherent) {
        try {
            return adherentRepository.save(adherent);
        } catch (Exception e) {
            throw new AdherentNotFoundException("Adherent already exists");
        }
    }

    public Adherent updateAdherent(String id, Adherent adherentDetails) {
        try {
            Adherent adherent = adherentRepository.findById(id).get();
            adherent.setNom(adherentDetails.getNom());
            adherent.setPrenom(adherentDetails.getPrenom());
            adherent.setDateNaissance(adherentDetails.getDateNaissance());
            adherent.setCivilite(adherentDetails.getCivilite());
            return adherentRepository.save(adherent);
        } catch (Exception e) {
            throw new AdherentNotFoundException("Adherent not found");
        }
    }

    public void deleteAdherent(Adherent adherent) {
        try {
            adherentRepository.delete(adherent);
        } catch (Exception e) {
            throw new AdherentNotFoundException("Adherent not found");
        }
    }
}
