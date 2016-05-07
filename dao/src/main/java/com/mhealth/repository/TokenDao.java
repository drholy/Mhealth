package com.mhealth.repository;

import com.mhealth.common.base.BaseDao;
import com.mhealth.model.Token;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by pengt on 2016.5.5.0005.
 */
@Repository("tokenDao")
public class TokenDao extends BaseDao {
    /**
     * 新增token
     *
     * @param token
     */
    public void addToken(Token token) {
        mongoTemplate.insert(token);
    }

    /**
     * upsert token
     *
     * @param token
     * @return
     */
    public boolean upsertToken(Token token) {
        WriteResult wr = mongoTemplate.upsert(new Query(Criteria.where("_id").is(new ObjectId(token.getId())))
                , new Update().set("access_token", token.getAccess_token()).set("expire", token.getExpire()), Token.class);
        return wr.getN() == 1;
    }

    /**
     * 根据access_token查询token
     *
     * @param access_token
     * @return
     */
    public Token getTokenByAcc(String access_token) {
        return mongoTemplate.findOne(new Query(Criteria.where("access_token").is(access_token)), Token.class);
    }

    /**
     * 根据userId获取token
     *
     * @param userId
     * @return
     */
    public Token getTokenByUser(String userId) {
        return mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), Token.class);
    }

    /**
     * 根据userId删除token
     *
     * @param userId
     * @return
     */
    public boolean delTokenByUser(String userId) {
        WriteResult wr = mongoTemplate.remove(new Query(Criteria.where("userId").is(userId)), Token.class);
        return wr.getN() == 1;
    }
}
