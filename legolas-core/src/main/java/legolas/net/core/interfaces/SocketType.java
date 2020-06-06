package legolas.net.core.interfaces;

import javax.net.ServerSocketFactory;
import java.net.DatagramSocket;
import java.net.InetAddress;

public enum SocketType implements LocalPortAvailability {

  TCP {
    @Override
    public boolean isPortAvailable(Port port) {
      try {
        ServerSocketFactory.getDefault()
          .createServerSocket(port.value(), 1, InetAddress.getByName("localhost"))
          .close();
        return true;
      } catch (Exception ex) {
        return false;
      }
    }
  },

  UDP {
    @Override
    public boolean isPortAvailable(Port port) {
      try {
        DatagramSocket socket = new DatagramSocket(port.value(), InetAddress.getByName("localhost"));
        socket.close();
        return true;
      } catch (Exception ex) {
        return false;
      }
    }
  };
}
