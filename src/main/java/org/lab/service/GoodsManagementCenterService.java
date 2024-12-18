package org.lab.service;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.lab.model.GoodsManagementCenter;
import org.lab.repository.GoodsManagementCenterRepository;

import java.util.List;

@Stateless
public class GoodsManagementCenterService {
    @Inject
    private GoodsManagementCenterRepository goodsManagementCenterRepository;

    public List<GoodsManagementCenter> findAll() {
        return goodsManagementCenterRepository.findAll();
    }

    public GoodsManagementCenter findById(int id) {
        return goodsManagementCenterRepository.findById(id);
    }

    public void save(GoodsManagementCenter center) {
        goodsManagementCenterRepository.save(center);
    }

    public void delete(GoodsManagementCenter center) {
        goodsManagementCenterRepository.delete(center.getId());
    }
}
