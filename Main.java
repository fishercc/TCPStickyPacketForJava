
/*
 * @Author: ywc
 * @Date: 2020-03-15 20:07:00
 * @LastEditTime: 2020-03-16 12:30:16
 * @LastEditors: Please set LastEditors
 * @Description: test
 * @FilePath: \test\Main.java
 */
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.lang.Character;


public class Main {
    byte[] rawBuffer = new byte[1024];
    ByteBuffer buffer = ByteBuffer.wrap(rawBuffer);
    InputStream inFromServer;

    public boolean readSocket()throws IOException{
        buffer.compact();
        int head = buffer.position();
        //采用包头+包体，标识符 报头:7E len1 len2+ [报文体]
        int r = inFromServer.read(rawBuffer, head,rawBuffer.length - head);
        if (r == -1) {
            
           return false;
          // throw new EOFException("Controller socket closed");
        }
        buffer.position(head + r);
        buffer.flip();
        return true;
    }
    public String Next(){

        int packLen =processPacket();
        if( packLen==-1) return null;

        int savelimit = buffer.limit();
        int curpos = buffer.position();
        buffer.limit(packLen+curpos);

            Charset cs = Charset.forName ("UTF-8");
            CharBuffer cb = cs.decode (buffer);
            String str =  cb.toString();
           // System.out.println(str);
           // System.out.println(str.length());

        buffer.limit(savelimit);
         //   buffer.position(str.length());
         return str;
    }

    public int processPacket() {
        int res = -1;
        while(true){
            if (!buffer.hasRemaining() ||(buffer.remaining() <= 3)) {
                res = -1;
                break;
            }
            int savePosition = buffer.position();
            byte firstBy = buffer.get();
            if(0x7e == firstBy){
                byte bh = buffer.get();
                byte bl = buffer.get();
                res = (((int)bh)<<8)|bl;
                if(res>buffer.remaining())
                {
                    res =-1;
                    buffer.position(savePosition);
                }
                break;
            }
        }
        return  res;
    }
    public void run()throws IOException{

        System.out.println("--------------");
        String serverName ="127.0.0.1";
        int port = 8080;
        try
        {
            System.out.println("连接到主机：" + serverName + " ，端口号：" + port);
            Socket client = new Socket(serverName, port);
            System.out.println("远程主机地址：" + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
    
            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            inFromServer = client.getInputStream();
          
            buffer.limit(0);
          
            while(true)
            {
                

              boolean  res  = readSocket();
              if(!res)break;
            
               while(true) {
                  String   msg = Next();
                    if(msg==null){
                        break; 
                    }
                    System.out.println(msg);
                    System.out.println(msg.length());
               }

                    
                
                
            }
            
            client.close();
       }
       catch(IOException e)
       {
            e.printStackTrace();
       }

    }
    public static void main(String[] args) throws IOException {
       
        Main obj=new Main();
        obj.run();;
       /*
        InputStream input = new FileInputStream("D:/my1.txt");
        byte[] rawBuffer = new byte[1024];
        ByteBuffer buffer = ByteBuffer.wrap(rawBuffer);
      


        buffer.limit(0);
    while(true)
    {
        int head = buffer.position();
            buffer.compact();
            head = buffer.position();
            int r = input.read(rawBuffer, head,rawBuffer.length - head);
            if (r == -1) {
                
                break;
               // throw new EOFException("Controller socket closed");
            }
       
            buffer.position(head + r);
            buffer.flip();



            int savedPosition = buffer.position();

             int type = buffer.get();
             int a = buffer.get();
             int b = buffer.get();
             boolean fla = buffer.hasRemaining();
       
             String str;
             str = (char)type+""+(char)a+""+(char)b;
             System.out.println(str);
    }
       
    input.close();
*/
    }
}
