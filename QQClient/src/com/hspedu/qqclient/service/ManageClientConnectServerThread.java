package com.hspedu.qqclient.service;

import java.util.HashMap;

public class ManageClientConnectServerThread {
    //多数のスレッドをHashMapコレクションに入れます、KEYはユーザーID、VALUEはスレッド
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();

    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId,clientConnectServerThread);
    }
    //userIdで相応のスレッドを取得します。
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);
    }


}
