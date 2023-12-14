package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread{
    //該当スレッドはソケットを所持しています。
    private Socket socket;

    //該当コンストラクタはソケットのオブジェクトを受け入れます。

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }
    //socketを効率的に取得するために
    @Override
    public void run() {
        //このスレッドはベースでサーバーと通信するので、whileループを新規します、
        while (true) {
            try {
                System.out.println("クライアントのスレッドはサーバーから送られてきた情報の読取りを待機しています。");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //サーバーはメッセージのオブジェクトを送られていなかった場合は、
                //スレッドは渋滞になります。
                //messageTypeを判断します、相応な処置を行います
                //読み取ったのは「MESSAGE_RET_ONELINE_FRIEND」の場合
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONELINE_FRIEND)){
                    //オンラインリストを引き出します、表示します
                    String[] onelineUsers = message.getContent().split(" ");
                    System.out.println("==========　オンラインアカウントリスト　==========");
                    for (int i = 0; i < onelineUsers.length; i++) {
                        System.out.println("ユーザー："+onelineUsers[i]);
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //サーバーから転送されてきたメッセージをモニターに表示します。
                    System.out.println("\n"+" "+message.getSender()+" は " +
                            message.getGetter()+" にメッセージ："+message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL)) {
                    //サーバーから転送されてきたメッセージをモニターに表示します。
                    System.out.println("\n"+" "+message.getSender()+"からのグループメッセージ："+message.getContent());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println("\n"+message.getSender()+" は "+message.getSrc()+" をファイルメッセージで "
                            +message.getGetter()+ "　の　"+message.getDest()+" に送信しました");
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\nファイル送信完了しました");

                }
                else{
                    System.out.println("他のメッセージタイプは現時点処理する事ができません");
                }
            } catch (Exception e) {
e.printStackTrace();            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
