import java.net.*;
import java.io.*;

public class Test {
public static void main(String [] args) {
try {
InetAddress thisIp =InetAddress.getLocalHost();
System.out.println("getHostAddress:"+thisIp.getHostAddress());
System.out.println("getHostName:"+thisIp.getHostName());
}
catch(Exception e) {
e.printStackTrace();
}
}
}