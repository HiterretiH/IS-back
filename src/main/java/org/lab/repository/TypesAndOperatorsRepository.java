package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.TypesAndOperators;

@Stateless
public class TypesAndOperatorsRepository extends GenericRepository<TypesAndOperators, Integer> {
    public TypesAndOperatorsRepository() {
        super(TypesAndOperators.class);
    }
}
