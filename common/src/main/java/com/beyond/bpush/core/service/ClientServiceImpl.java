package com.beyond.bpush.core.service;

import com.beyond.bpush.core.EpochTime;
import com.beyond.bpush.core.TxMain;
import com.beyond.bpush.core.entity.Client;
import com.beyond.bpush.core.entity.ClientStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * 客户端管理类.
 * Created by admin on 14-8-11.
 */
@Service("clientServiceImpl")
public class ClientServiceImpl extends BaseService implements ClientService {

    public static final String SQL_findByUserId = "select * from client where userId = ?";
    public static final String SQL_addClient = "insert into client(productId, userId, deviceToken, createAt, statusId, typeId, deviceId)values(?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_updateOnlineTs = "update client set lastOnline=?, statusId=?, typeId=?, deviceToken=? where id = ?";
    public static final String SQL_findOfflineByType = "select * from client where productId = ? and typeId = ? and lastOnline >= ? and deviceToken is not null order by id limit ?, ?";
    public static final String SQL_countOfflineByType = "select count(1) from client where productId = ? and typeId = ? and lastOnline >= ?";
    public static final String SQL_updateStatus = "update client set lastOnline=?, statusId=? where id = ?";
    public static final String SQL_updateStatusByUser = "update client set lastOnline=?, statusId=? where userId = ?";
    public static final String SQL_updateBadge = "update client set badge = badge + ? where userId = ?";
    public static final String SQL_updateBadge2 = "update client set badge = IF(badge + ? > 0, badge + ?, 0) where userId = ?";
    public static final String SQL_updateOfflineTs = "update client set lastSendAt=?, statusId=? where userId = ?";

    public static ClientService instance;

    protected static final RowMapper<Client> Client_ROWMAPPER = new BeanPropertyRowMapper<Client>(
            Client.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    @Override
    @TxMain
    public void add(final Client client){
        if (client == null){
            return;
        }

        KeyHolder holder = new GeneratedKeyHolder();
        this.mainJdbc.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {

                PreparedStatement ps = connection.prepareStatement(SQL_addClient,
                        Statement.RETURN_GENERATED_KEYS);

                ps.setObject(1, client.getProductId());
                ps.setObject(2, client.getUserId());
                ps.setObject(3, client.getDeviceToken().equalsIgnoreCase("null") ? null : client.getDeviceToken());
                ps.setObject(4, new Date());
                ps.setObject(5, ClientStatus.Online);
                ps.setObject(6, client.getTypeId());
                ps.setObject(7, client.getDeviceId());

                return ps;
            }
        }, holder);

        client.setId(holder.getKey().longValue());
    }

    @Override
    public Client findByUserId(String userId){
        String cacheKey = formatCacheKey(userId);
        Client client = getItemFromRedis(cacheKey, Client.class);
        if (null == client) {
            List<Client> list = this.mainJdbc.query(SQL_findByUserId, Client_ROWMAPPER, userId);
            if (list.size() > 0) {
                client = list.get(0);
                putItemToRedis(cacheKey, client);
            }
        }
        return client;
    }

    private String formatCacheKey(String userId) {
        return "qp:c:u:" + userId;
    }

    @Override
    public List<Client> findOfflineByType(Integer productId, Integer typeId, Integer page, Integer limit){
        long now = EpochTime.now() - 86400;
        int offset = (page - 1) * limit;
        List<Client> list = this.mainJdbc.query(SQL_findOfflineByType, Client_ROWMAPPER, productId, typeId, now, offset, limit);
        return list;
    }

    @Override
    @TxMain
    public int countOfflineByType(Integer productId, Integer typeId){
        long now = EpochTime.now() - 86400;
        int count = this.mainJdbc.queryForObject(SQL_countOfflineByType, Integer.class, productId, typeId, now);
        return count;
    }

    @Override
    @TxMain
    public void updateOnlineTs(Client client){
        long ts = EpochTime.now();
        int ret = this.mainJdbc.update(SQL_updateOnlineTs, ts, ClientStatus.Online, client.getTypeId(), client.getDeviceToken(), client.getId());
        if (ret > 0){
            String cacheKey = formatCacheKey(client.getUserId());
            delCache(cacheKey);
        }
    }

    @Override
    @TxMain
    public void updateStatus(String userId, int statusId) {
        long ts = EpochTime.now();
        int ret = this.mainJdbc.update(SQL_updateStatusByUser, ts, statusId, userId);
        if (ret > 0) {
            String cacheKey = formatCacheKey(userId);
            delCache(cacheKey);
        }
    }

    @Override
    public void updateBadge(final String userId, final int count) {
        jdbcExecutor.submit(new Runnable() {
            @Override
            public void run() {

                try {
                    postUpdateBadge(count, userId);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            }
        });

    }

    @TxMain
    private void postUpdateBadge(int count, String userId) {
        int ret = 0;
        if (count > 0) {
            ret = mainJdbc.update(SQL_updateBadge, count, userId);
        }else{
            ret = mainJdbc.update(SQL_updateBadge2, count, count, userId);
        }

        if (ret > 0){
            String cacheKey = formatCacheKey(userId);
            delCache(cacheKey);
        }
    }

    @Override
    @TxMain
    public void updateOfflineTs(String userId, int lastSendTs) {
        int ret = this.mainJdbc.update(SQL_updateOfflineTs, lastSendTs, ClientStatus.Offline, userId);
        if (ret > 0) {
            String cacheKey = formatCacheKey(userId);
            delCache(cacheKey);
        }
    }
}
