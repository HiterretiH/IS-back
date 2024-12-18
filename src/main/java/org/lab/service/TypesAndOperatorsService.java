package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.ProductType;
import org.lab.model.TypesAndOperators;
import org.lab.model.User;
import org.lab.repository.TypesAndOperatorsRepository;

import java.util.List;

@Stateless
public class TypesAndOperatorsService {
    @Inject
    private TypesAndOperatorsRepository typesAndOperatorsRepository;

    public List<TypesAndOperators> findAll() {
        return typesAndOperatorsRepository.findAll();
    }

    public TypesAndOperators findById(int id) {
        return typesAndOperatorsRepository.findById(id);
    }

    public void save(TypesAndOperators typesAndOperators) {
        typesAndOperatorsRepository.save(typesAndOperators);
    }

    public void delete(TypesAndOperators typesAndOperators) {
        typesAndOperatorsRepository.delete(typesAndOperators.getId());
    }

    public List<TypesAndOperators> findByUser(User user) {
        return typesAndOperatorsRepository.findAllByUser(user);
    }

    public List<TypesAndOperators> findByUserAndType(User user, ProductType type) {
        return typesAndOperatorsRepository.findByUserAndType(user, type);
    }
}
