package com.hspedu.qqclient;

import com.hspedu.qqclient.service.FileClientService;
import com.hspedu.qqclient.service.MessageClientService;
import com.hspedu.qqclient.service.UserClientService;
import com.hspedu.qqclient.utils.Utility;
import com.hspedu.qqcommon.Message;

import java.io.IOException;
import java.util.Scanner;

public class QQView {

    private Boolean loop  = true;//メニューの表示を制御します
    private String key = "";

    private UserClientService userClientService = new UserClientService();
    //このオブジェクトはサーバーにログインとアカウント登録に使います。
    private MessageClientService messageClientService = new MessageClientService();
    //プライベートメッセージとグループメッセージ
    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new QQView().mainMenu();
        System.out.println("アカウントはログアウトしました。");

    }
    private void mainMenu() throws IOException, ClassNotFoundException {
        while (loop) {
            System.out.println("==========ログイン画面==========");
            System.out.println("\t\t1　システムログイン");
            System.out.println("\t\t9　システムログアウト");
            System.out.println("選択肢を入力してください");
            key = Utility.readString(1);

            //入力された内容で、異なるロジックを処理します。
            switch (key) {
                case "1":
                    System.out.print("アカウント番号を入力してください");
                    String userId = Utility.readString(50);
                    System.out.print("アカウントパスワードを入力してください");
                    String pwd = Utility.readString(50);
                    //サーバー側でアカウントの有効性を検証します。
                    //UserClientService「ユーザー管理／ユーザーログイン」クラスを新規します。
                    if (userClientService.checkUser(userId,pwd)/* 検証用のメソッドの戻った結果 */){
                            System.out.println("==========ログイン成功〜！" + userId + "　さん　ようこそ〜！==========");
                            while (loop) {
                                System.out.println("\n==========アカウント　"+userId+" さんのネットワークメニュー");
                                System.out.println("\t\t1　オンライアカウトリスト");
                                System.out.println("\t\t2　グループメッセージ");
                                System.out.println("\t\t3　プライベートメッセージ");
                                System.out.println("\t\t4　ファイル送信");
                                System.out.println("\t\t9　システムログアウト");
                                System.out.println("選択肢を入力してください");
                                key = Utility.readString(1);
                                switch (key){
                                    case"1":
                                        System.out.println("オンライアカウトリスト");
                                        userClientService.onelineFriendList();
                                       break;
                                    case"2":
                                        System.out.println("グループメッセージを入力してください。");
                                        String  s = Utility.readString(100);
                                        messageClientService.sendMessageToAll(s,userId);
                                        break;
                                    case"3":
                                        //System.out.println("プライベートメッセージ");
                                        System.out.println("相手のアカウトを入力してください。");
                                        String  getterId = Utility.readString(50);
                                        System.out.println("メッセージ内容を入力してください。");
                                        String  content = Utility.readString(100);
                                        messageClientService.sendMessageToOne(content,userId,getterId);
                                        break;
                                    case"4":
                                        System.out.println("ファイル送信相手のアカウントを入力してください");
                                        getterId = Utility.readString(50);
                                        System.out.println("送信ファイルのアドレスを入力してください");
                                        String src = Utility.readString(100);
                                        System.out.println("相手の受信アドレスを入力してください");
                                        String dest = Utility.readString(100);
                                        fileClientService.sendFileToOne(src,dest,userId,getterId);
                                        break;
                                    case "9":

                                        userClientService.SystemExit();

                                        loop = false;
                                        break;
                                }

                            }
                    }else {
                        System.out.println("==========サーバーにログインする事ができませんでした！==========");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }




        }
    }

}
