import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

public class UserDataViewer {
    public static void main(String[] args) {
        // Load user data
        Map<String, String> userData = loadUserData("userdata.ser");
        if (userData != null) {
            // Display the user data
            System.out.println("User Data:");
            for (Map.Entry<String, String> entry : userData.entrySet()) {
                System.out.println("Username: " + entry.getKey() + "\tPassword: " + entry.getValue());
            }
        }

        // Load reservation data
        Map<String, List<String>> reservationData = loadReservationData("reservationdata.ser");
        if (reservationData != null) {
            // Display the reservation data
            System.out.println("\nReservation Data:");
            for (Map.Entry<String, List<String>> entry : reservationData.entrySet()) {
                String username = entry.getKey();
                List<String> reservations = entry.getValue();
                System.out.println("Username: " + username + "\tReservations: " + reservations);
            }
        }
    }

    private static Map<String, String> loadUserData(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Map<String, String> data = (Map<String, String>) in.readObject();
            in.close();
            fileIn.close();
            return data;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, List<String>> loadReservationData(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Map<String, List<String>> data = (Map<String, List<String>>) in.readObject();
            in.close();
            fileIn.close();
            return data;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
