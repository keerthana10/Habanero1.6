import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Mac2 {

  public static void main(String[] args) {

    System.out.println(calculateMacIdentifier());
    String value = "00-26-6C-D2-6A-36|00-26-6C-D2-6A-37|00-26-6C-D2-6A-38"; // calculateMacIdentifier();
    StringTokenizer st = new StringTokenizer(value, "|");
    while (st.hasMoreTokens()) {
      String macCaptured = st.nextToken();
      System.out.println(macCaptured);
      if (macCaptured.equals("00-26-6C-D2-6A-37")) {
        break;

      }
    }

  }

  private final static String calculateMacIdentifier() {
    StringBuilder sb = new StringBuilder();
    List<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();
    try {
      interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

      Collections.sort(interfaces, new Comparator<NetworkInterface>() {
        @Override
        public int compare(NetworkInterface o1, NetworkInterface o2) {
          try {
            if (o1.isLoopback() && !o2.isLoopback()) {
              return 1;
            }
            if (!o1.isLoopback() && o2.isLoopback()) {
              return -1;
            }
          } catch (SocketException e) {
            // System.out.println("Error sorting network interfaces");
            return 0;
          }
          return o1.getName().compareTo(o2.getName());
        }
      });

      for (NetworkInterface iface : interfaces) {
        if (iface.getHardwareAddress() != null) {
          // get the first not null hw address and CRC it
          byte[] mac = iface.getHardwareAddress();

          // System.out.print("Current MAC address : ");

          for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
          }
          sb.append("|");

          // System.out.println(sb.toString());

          // return Long.toHexString(crc.getValue());
        }
      }

      if (interfaces.isEmpty()) {
        System.out.println("Not found mac adress");
      }
      return sb.toString();
      // return "";
    } catch (SocketException e) {
      System.out.println("Error getting mac address");
      return "";
    }

  }

}