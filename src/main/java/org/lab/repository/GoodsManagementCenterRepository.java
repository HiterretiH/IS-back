package org.lab.repository;

import jakarta.ejb.Stateless;
import org.lab.model.GoodsManagementCenter;

@Stateless
public class GoodsManagementCenterRepository extends GenericRepository<GoodsManagementCenter, Integer> {
    public GoodsManagementCenterRepository() {
        super(GoodsManagementCenter.class);
    }
}
