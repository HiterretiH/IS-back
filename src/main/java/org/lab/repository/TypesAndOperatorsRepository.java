package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.ProductType;
import org.lab.model.TypesAndOperators;
import org.lab.model.User;

import java.util.List;

@Stateless
public class TypesAndOperatorsRepository extends GenericRepository<TypesAndOperators, Integer> {
    public TypesAndOperatorsRepository() {
        super(TypesAndOperators.class);
    }

    public List<TypesAndOperators> findAllByUser(User user) {
        return entityManager.createQuery(
                        "SELECT t FROM TypesAndOperators t WHERE t.operator.id = :user_id", TypesAndOperators.class)
                .setParameter("user_id", user.getId())
                .getResultList();
    }

    public List<TypesAndOperators> findByUserAndType(User user, ProductType type) {
        return entityManager.createQuery(
                "SELECT t FROM TypesAndOperators t WHERE t.operator.id = :user_id AND t.productType = :type", TypesAndOperators.class)
                .setParameter("user_id", user.getId())
                .setParameter("type", type)
                .getResultList();
    }
}
