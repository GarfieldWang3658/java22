package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {
        boolean loop = true;
        while (loop){
        System.out.println("プッシューしたいニュース内容を入力してください。\n退出は「EXIT」を入力してください。");
        String news = Utility.readString(100);
            if (news.equals("EXIT")){
                loop = false;
                break;
            }
        Message message = new Message();
        message.setSender("サーバー");
        message.setMesType(MessageType.MESSAGE_TO_ALL);
        message.setContent(news);
        message.setSendTime(new Date().toString());
        System.out.println("サーバーからのお知らせ"+news);

        HashMap<String,ServerConnectClientThread>hm = ManageClientThreads.getHm();
        Iterator<String> iterator = hm.keySet().iterator();
        while (iterator.hasNext()){
            String oneLineUserId= iterator.next().toString();
            try {
                ObjectOutputStream oos = new ObjectOutputStream
                        (hm.get(oneLineUserId).getSocket().getOutputStream());
                oos.writeObject(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
}
