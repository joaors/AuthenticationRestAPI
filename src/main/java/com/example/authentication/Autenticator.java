package com.example.authentication;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.security.auth.login.LoginException;

/**
 *
 * @author joao.rodrigo
 */
public class Autenticator {
    
    /* Simula uma tabela do banco com os usuarios */
    private final Map<String, String> users = new HashMap();
    
    /* Simula uma tabela do banco com as chaves de acesso */
    private final Map<String, String> keys = new HashMap();
    
    /* Guarda os tokens gerados em tempo de execução */
    private final Map<String, String> tokens = new HashMap<>();
    
    private static Autenticator autenticator = null;    
    
    private Autenticator() {
        /**
         * Guarda o usuário e senha dos clientes da API
         */
        users.put("walter", "white");
        users.put("toni", "soprano");
        
        /**
         * keys são geradas antecipadamente e disponibilizadas para os clientes da API
         * Aqui estamos representando as keys para os dois users pré cadastrados.
         */
        keys.put( "ece40d50-b22b-4b33-a728-343be730d85e", "walter" );
        keys.put( "1ee68990-2d56-44a9-ae40-09d0bf074d8d", "toni" );        
    }
    
    public static Autenticator getInstance() {
        if (Objects.isNull(autenticator)) {
            autenticator = new Autenticator();
        }
        return autenticator;
    }
    
    public String login(String user, String password, String key) throws LoginException {
        
        if (keys.containsKey(key)) {
            String userStorage = keys.get(key);
            if (userStorage.equals(user) && users.containsKey(user)) {
                String passwordStorage = users.get(user);
                if (passwordStorage.equals(password)) {
                    String auth = UUID.randomUUID().toString();
                    tokens.put(auth, user);
                    return auth;
                }
            }
        }
        throw new LoginException("Get Out! you are not welcome!!");
    }
    
    public boolean isKeyValid(String key ) {
        return keys.containsKey(key);
    } 
    
    private boolean isTokenValid(String token) {
        return tokens.containsKey(token);
    }
    
    public boolean isTokenValid(String key, String token ) {
        if (isKeyValid(key)) {
            String userKey = keys.get(key);
            if (isTokenValid(token)) {
                String userToken = tokens.get(token);
                if ( userKey.equals(userToken) ) {
                    return true;
                }
            }
        }
        return false;
    }

    public void logout( String key, String token ) throws GeneralSecurityException {
        if ( isKeyValid(key) ) {
            String userKey = keys.get(key);
            if (isTokenValid(token)) {
                String userToken = tokens.get(token);
                if (userToken.equals(userKey)) {
                    tokens.remove(token);
                    return;
                }
            }
        }
        throw new GeneralSecurityException( "Invalid key and token." );
    }    
    
}
