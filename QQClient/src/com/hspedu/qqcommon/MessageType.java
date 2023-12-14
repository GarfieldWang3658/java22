package com.hspedu.qqcommon;

public interface MessageType {

    //1.インターフェイスの中に定数を新規します。
    //2.異なる定数の値、異なるメッセージタイプを表示ます
    
    String MESSAGE_LOGIN_SUCCEED = "1";//ログイン成功
    String MESSAGE_LOGIN_FAIL = "2";//ログイン失敗
    String MESSAGE_COMM_MES = "3";//通常メッセージパック
    String MESSAGE_GET_ONELINE_FRIEND = "4";//オンラインアカウントリストに戻る事を要求します。
    String MESSAGE_RET_ONELINE_FRIEND = "5";//オンラインアカウントリストに戻る
    String MESSAGE_CLIENT_EXIT = "6";//クライアントを退出する事を要求します。
    String MESSAGE_TO_ALL = "7";//グループメッセージ

    String MESSAGE_FILE_MES = "8";//ファイルメッセージ






}
