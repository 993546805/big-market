package com.tuto.domain.activity.service;

import com.tuto.domain.activity.repository.IActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tu
 * @date 2024-10-17 11:20
 */
@Service
public class RaffleOrderImpl extends AbstractRaffleOrder {

    @Autowired
    public RaffleOrderImpl(IActivityRepository repository) {
        super(repository);
    }
}
