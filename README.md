# TCPStickyPacketForJava
java socket TCP 处理粘包例子，采用包头+包体，标识符7e 报头:7E len1 len2+ [报文体]
len1 表示报文体的长度的高8位；
len2 表示报文体的长度的低8位；
标识7e,可以换成其他的
