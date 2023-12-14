package com.hspedu.qqcommon;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 123456789L;

    private String sender;//送信者
    private String getter;//受信者
    private String content;//送信内容
    private String sendTime;//送信日時

    private String mesType;//メッセージタイプ「インターフェースでタイプを決められます」
    private byte[] fileBytes;//
    private int fileLen = 0;//
    private String dest;//保存先のアドレス
    private String src;//元ファイルのアドレス

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

}
