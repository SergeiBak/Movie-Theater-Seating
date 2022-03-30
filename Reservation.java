import java.util.ArrayList;

public class Reservation {
    private String reservationID;
    private int assignedSeats;
    private ArrayList<String> seats;

    // Constructors
    public Reservation(String reservationIdentifier, int assignedNoOfSeats) {
        reservationID = reservationIdentifier;
        assignedSeats = assignedNoOfSeats;
    }

    public Reservation(Reservation res) {
        reservationID = res.GetReservationID();
        assignedSeats = res.GetAssignedSeats();
    }

    // Getters
    public String GetReservationID() {
        return reservationID;
    }

    public int GetAssignedSeats() {
        return assignedSeats;
    }

    /**
     * copies over reserved seats provided by Theater
     */
    public void AssignSeats(ArrayList<String> seatsForReservation) {
        seats = new ArrayList<>(seatsForReservation);
    }

    /**
     * returns reservation details in the following string format: R### A#,B#,C##
     */
    public String PrintSeats() {
        StringBuilder reservationDetails = new StringBuilder();
        reservationDetails.append(reservationID);
        reservationDetails.append(" ");

        for (String seat : seats) {
            reservationDetails.append(seat).append(",");
        }

        if (seats.size() > 0) { // remove extra delmitter at end of string
            reservationDetails.setLength(reservationDetails.length() - 1);
        } else {
            reservationDetails.append("Not enough seats!");
        }

        reservationDetails.append("\n");

        return reservationDetails.toString();
    }
}
