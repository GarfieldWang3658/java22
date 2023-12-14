package com.hspedu.qqclient.service;

import com.hspedu.qqclient.utils.Utility;
import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class MessageClientService {
    //メッセージに関連するメソッド
    public void sendMessageToAll(String content,String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL);
        message.setSender(senderId);
        message.setContent(content);
        message.getSendTime(new java.util.Date().toString());
        System.out.println(new Date().toString()+" "+senderId+"　はグループメッセージを送りました。");

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
        public void sendMessageToOne(String content,String senderId,String getterId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.getSendTime(new java.util.Date().toString());
        System.out.println(new Date().toString()+" "+senderId+"　は　"+getterId+"　にメッセージを送りました。");

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
        oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
