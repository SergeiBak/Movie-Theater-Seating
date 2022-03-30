import java.util.ArrayList;

public class Reservation {
    private String reservationID;
    private int assignedSeats;
    private ArrayList<String> seats;

    public Reservation(String reservationIdentifier, int assignedNoOfSeats) {
        reservationID = reservationIdentifier;
        assignedSeats = assignedNoOfSeats;
    }

    public Reservation(Reservation res) {
        reservationID = res.GetReservationID();
        assignedSeats = res.GetAssignedSeats();
    }

    public String GetReservationID() {
        return reservationID;
    }

    public int GetAssignedSeats() {
        return assignedSeats;
    }

    public void AssignSeats(ArrayList<String> seatsForReservation) {
        seats = new ArrayList<>(seatsForReservation);
    }

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
