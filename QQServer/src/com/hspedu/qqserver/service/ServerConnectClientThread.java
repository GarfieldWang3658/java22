package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectClientThread extends Thread {
    //オブジェクトをクライアントとの通信を維持するクラスです
    private Socket socket;
    private String userId;

    public Socket getSocket() {
        return socket;
    }

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {//スレッドをランニング状態にし、送受信を行います
        while (true){
            try {
            System.out.println(userId+"　さんは現在通信中、データーをロードしています。");
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message)ois.readObject();
            //メッセージのタイプによって、そうな業務処理を行います。
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONELINE_FRIEND)){
                    System.out.println(message.getSender()+"オンラインアカウントリストを要求します。");
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    //messageを戻ります
                    //Messageオブジェクトを構築し、クライアントに戻ります。
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONELINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                }else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender()+"ログアウトします。");
                    //該当クライアントのスレットをコレクションから削除します。
                     ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    socket.close();
                    break;


                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //messageでgetterIdを取得し、相応なスレットを取得します。
                    ServerConnectClientThread serverConnectClientThread =
                            ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream
                            (serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                    //転送、クライアントはオフラインの場合はメッセージをデーターべーすに保存する事で
                    // 留守伝言の機能を実現することができます。

                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){
                   // スレットを管理するコレクションを走査し、全てのソケットを取得します。
                    //メッセージを転送します。
                    HashMap<String,ServerConnectClientThread>hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()){
                        String oneLineUserId= iterator.next().toString();
                    if (!oneLineUserId.equals(message.getSender())){
                        ObjectOutputStream oos = new ObjectOutputStream
                                (hm.get(oneLineUserId).getSocket().getOutputStream());
                        oos.writeObject(message);
                    }
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    //getterIdでスレッドを取得し,messageを対象に転送します
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageClientThreads.getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);





                }
                else{System.out.println("他の処置はしばらくできません。");}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
