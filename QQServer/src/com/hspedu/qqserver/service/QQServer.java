package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class QQServer {
    //サーバーはポート9999を監視しています
    private ServerSocket ss = null;
    private static ConcurrentHashMap<String,User> validUsers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<User, ArrayList<Message>> offLineDb = new ConcurrentHashMap<>();

    static {
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("400",new User("400","123456"));
        validUsers.put("500",new User("500","123456"));


    }

    public QQServer() throws IOException, ClassNotFoundException {
        try {
            System.out.println("サーバーはポート9999を監視しています....");
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);

            while (true) {//監視はクライアントに接続後も続きます。
                Socket socket = ss.accept();//クライアントに接続していなければ渋滞になります。
                //socketは関連するオブジェクトoutnputストリームを取得します。

                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());

                User u = (User) ois.readObject();

                //Messageオブジェクトを新規し、クライアント側に返信します。
                Message message = new Message();

                if (u.getUserId().equals("100") && u.getPasswd().equals("123456")) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //socketを所持するスレッドを新規し、クライアントと通信します、
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());
                    //スレッドを起動します。
                    serverConnectClientThread.start();
                    //該当スレッドをコレクションで管理します。
                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);

                } else {//ログイン失敗
                    System.out.println("ユーザーID＝" + u.getUserId() + "パスワード＝" + u.getPasswd() + "〜認証失敗〜");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
                //サーバーはwhileループを退出した場合はサーバの監視がなくなることになります。
                //そのため　ソケットを終了します。
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

