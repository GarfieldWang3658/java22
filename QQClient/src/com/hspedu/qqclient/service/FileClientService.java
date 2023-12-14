package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.*;

public class FileClientService {
    //ファイル送信に使用するメソッド
    public void sendFileToOne(String src,String dest,String senderId,String getterId){
        //srcを読み取り
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        //ファイルを読み取る
        FileInputStream fileInputStream =null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        try {
            FileInputStream fileInputStream1 = new FileInputStream(src);
            fileInputStream1.read(fileBytes);
            //srcにあるファイルをバイト配列で読み取ります。
            //ファイルのバイト配列をメッセージに設置します。
            message.setFileBytes(fileBytes);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //
        System.out.println("\n"+senderId+" は "+src+" をファイルメッセージで "
                +getterId+ "　の　"+dest+" に送信しました");

        try {
            ObjectOutputStream oos = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread(senderId)
                            .getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
