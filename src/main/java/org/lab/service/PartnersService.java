package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Partners;
import org.lab.repository.PartnersRepository;

import java.util.List;

@Stateless
public class PartnersService {
    @Inject
    private PartnersRepository partnersRepository;

    public List<Partners> findAll() {
        return partnersRepository.findAll();
    }

    public Partners findById(int id) {
        return partnersRepository.findById(id);
    }

    public void save(Partners partner) {
        partnersRepository.save(partner);
    }

    public void delete(Partners partner) {
        partnersRepository.delete(partner.getId());
    }
}
