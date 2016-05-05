package com.mhealth.service;

import com.mhealth.model.Token;
import com.mhealth.repository.TokenDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by pengt on 2016.5.5.0005.
 */
@Service("tokenService")
public class TokenService {
    @Resource(name = "tokenDao")
    private TokenDao tokenDao;

    /**
     * 新增token
     *
     * @param token
     */
    public void addToken(Token token) {
        tokenDao.addToken(token);
    }

    /**
     * upsert token
     *
     * @param token
     * @return
     */
    public boolean upsertToken(Token token) {
        return upsertToken(token);
    }

    /**
     * 根据access_token查询token
     *
     * @param access_token
     * @return
     */
    public Token getTokenByAcc(String access_token) {
        return tokenDao.getTokenByAcc(access_token);
    }

    /**
     * 根据userId获取token
     *
     * @param userId
     * @return
     */
    public Token getTokenByUser(String userId) {
        return tokenDao.getTokenByUser(userId);
    }
}
