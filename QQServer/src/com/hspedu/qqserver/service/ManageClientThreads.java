package com.hspedu.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

public class ManageClientThreads {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //スレッドオブジェクトをhmコレクションに追加します。
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);

    }

    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }
    public static void  removeServerConnectClientThread(String userId){
        hm.remove(userId);
    }

    public static String getOnlineUser() {
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";

        }
        return onlineUserList;
    }
}
