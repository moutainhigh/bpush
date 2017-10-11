package com.beyond.bpush.core.service;

import com.beyond.bpush.core.TxMain;
import com.beyond.bpush.core.entity.Client;

import java.util.List;

/**
 * Created by user on 1/28/15.
 */
public interface ClientService {

    /**
     *
     * @param client
     */
    @TxMain
    void add(Client client);

    /**
     *
     * @param userId
     * @return Client
     */
    Client findByUserId(String userId);

    /**
     *
     * @param productId
     * @param typeId
     * @param page
     * @param limit
     * @return List
     */
    List<Client> findOfflineByType(Integer productId, Integer typeId, Integer page, Integer limit);

    /**
     *
     * @param productId
     * @param typeId
     * @return int
     */
    int countOfflineByType(Integer productId, Integer typeId);

    /**
     *
     * @param client
     */
    @TxMain
    void updateOnlineTs(Client client);

    /**
     *
     * @param userId
     * @param lastSendTs
     */
    @TxMain
    void updateOfflineTs(String userId, int lastSendTs);

    /**
     *
     * @param userId
     * @param statusId
     */
    @TxMain
    void updateStatus(String userId, int statusId);

    /**
     *
     * @param userId
     * @param count
     */
    @TxMain
    void updateBadge(String userId, int count);
}
