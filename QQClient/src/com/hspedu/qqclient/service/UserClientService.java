package com.hspedu.qqclient.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class UserClientService {
    //他のところにユーザーの情報を使用するので、Userをメーバープロパティとして新規します。
    private User u = new User();

    //socketは他のところにも使用するので、メーバープロパティとして新規します。
    private Socket socket;

    //このクラスはユーザーの登録とログイン検証を実行します。
    //サーバーでuserIdとpwdを検証します。
    public boolean checkUser(String userId, String pwd) throws IOException, ClassNotFoundException {
        //Userのオブジェクトを新規します。
        boolean b = false;
        u.setUserId(userId);
        u.setPasswd(pwd);
        //サーバーに接続し、uを送信します。
        socket = new Socket(InetAddress.getLocalHost(), 9999);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(u);

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Message ms = (Message) ois.readObject();

        if (ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
            b = true;
            //ログイン成功
            //サーバーと通信するスレッドを新規します。
            //ClientConnectServerThreadクラスを新規します。
            ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
            //クライアントのスレッドを起動します
            clientConnectServerThread.start();
            //今後はクライアント側の拡張を確保するために、スレッドをコレクションで管理します。
            ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
            b = true;

        } else {
            //ログイン失敗
            socket.close();

        }
        return b;
    }

    public void onelineFriendList() {
        //メッセージを送信、メッセージタイプはMESSAGE_GET_ONELINE_FRIENDになります
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONELINE_FRIEND);
        //サーバーに送信
        //現時点のスレッドのソケット（socket）の相応なObjectOutputStreamを取得し、
        try {
            //userIdでコレクションからスレッドのオブジェクトを取得します。　　　
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId());

            //スレッドでsocketを取得します。
            Socket socket = clientConnectServerThread.getSocket();

            //現時点のスレッドのソケット（socket）の相応なObjectOutputStreamを取得し、

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            //messageオブジェクトを送信し、サーバーからオンラインアカウントリストを要求します。

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void SystemExit() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());//送信者を指定しなければなりません。
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//            ObjectOutputStream oos =
//                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread
//                            (u.getUserId()).getSocket().getOutputStream());
//ManageClientConnectServerThread　=>　スレッドを管理するコレクションから
// getClientConnectServerThread　=>　スレットを取得します
            //該当スレットからユーザーIDを取得します、
            // ユーザーIDでソケットを取得します、
            //ソケットでoutputストリームを取得します。

            oos.writeObject(message);
            System.out.println(u.getUserId()+"はスレッドを終了させました。");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
}

