package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.Partners;
import org.lab.repository.PartnersRepository;
import org.lab.utils.PaginatedResponse;

import java.util.List;

@Stateless
public class PartnersService {
    @Inject
    private PartnersRepository partnersRepository;

    public PaginatedResponse<String> getAll(int page, int size) {
        List<String> data = partnersRepository.findWithPagination(page, size);
        int count = partnersRepository.count();
        return new PaginatedResponse<>(data, count);
    }

    public Partners getById(int id) {
        return partnersRepository.findById(id);
    }

    public void create(Partners partner) {
        Partners newPartner = new Partners();
        newPartner.setAddress(partner.getAddress());
        newPartner.setName(partner.getName());
        newPartner.setEmail(partner.getEmail());
        newPartner.setPhoneNumber(partner.getPhoneNumber());

        partnersRepository.save(newPartner);
    }

    public void delete(Partners partner) {
        partnersRepository.delete(partner.getId());
    }
}
