import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;   
import java.io.IOException;

public class Theater {
    private int theaterRows;
    private int theaterSeatsPerRow;
    private int seatSafetyBuffer; // buffer of how many empty seats in between groups in same row
    private int totalSeatsLeft; // total number of seats left in theater
    private int[] seatsLeft; // seats left per row
    private int[][] seats; // track status of all seats
    private HashMap<Integer, HashMap<Integer, String>> seatCodes = new HashMap<Integer, HashMap<Integer, String>>(); // unique codes for each seat
    private ArrayList<Reservation> reservations = new ArrayList<Reservation>(); // all registered reservations that were processed

    // constructor
    public Theater() {
        theaterRows = 10;
        theaterSeatsPerRow = 20;
        seatSafetyBuffer = 3;

        GenerateTheater();
    }

    /**
     * setup structures for holder theater data
     * fill dual HashMap with row, col coords as keys and generate unique seatCode for value
     */
    private void GenerateTheater() {
        totalSeatsLeft = theaterRows * theaterSeatsPerRow;
        seatsLeft = new int[theaterRows];

        for (int i = 0; i < seatsLeft.length; i++) {
            seatsLeft[i] = theaterSeatsPerRow;
        }
        
        seats = new int[theaterRows][theaterSeatsPerRow]; // initialized to 0s (empty), will be set to 1s when assigning seats (full), 2 to buffer

        char rowLetter = 'A';

        for (int row = 0; row < theaterRows; row++) {
            HashMap<Integer, String> innerMap = new HashMap<Integer, String>();  

            for (int col = 0; col < theaterSeatsPerRow; col++) {
                StringBuilder seatCode = new StringBuilder();
                seatCode.append(rowLetter);
                seatCode.append((col + 1));
                innerMap.put(col, seatCode.toString());
            }
            seatCodes.put(row, innerMap); // row, <col, code>

            rowLetter++;
        }
    }

    /**
     * parse fileline provided, create reservation object, assign seats to reservation
     */
    public void ProcessReservation(String fileLine) { 
        String[] tokens = fileLine.split(" "); // [0] == code, [1] == assigned number of seats
        String code = tokens[0];
        int assignedNoOfSeats = 0;

        try {
            assignedNoOfSeats = Integer.parseInt(tokens[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Reservation reservation = new Reservation(code, assignedNoOfSeats);
        AssignSeats(reservation);
    }

    /**
     * main algorithm that decides which seats to allocate for reservation request
     */
    private void AssignSeats(Reservation reservation) {
        if (reservation.GetAssignedSeats() > totalSeatsLeft) { // check if there are enough seats in theater for reservation
            return;
        }

        int backRowPointer = theaterRows / 2; // moves towards the back rows
        int frontRowPointer = backRowPointer - 1; // moves towards the front rows

        int seatsLeftToAssign = reservation.GetAssignedSeats();
        ArrayList<String> reservationCodes = new ArrayList<String>();
        
        // Loop through from center rows and see if entire reservation request fits in a single row
        while (true) {
            if (seatsLeftToAssign <= 0) {
                break;
            }
            if (backRowPointer >= theaterRows && frontRowPointer < 0) { // No row large enough to accomodate group found
                break;
            }

            seatsLeftToAssign = CheckRoomForFullGroup(backRowPointer, seatsLeftToAssign, reservationCodes);
            seatsLeftToAssign = CheckRoomForFullGroup(frontRowPointer, seatsLeftToAssign, reservationCodes);

            frontRowPointer--;
            backRowPointer++;
        }

        backRowPointer = theaterRows / 2;
        frontRowPointer = backRowPointer - 1;

        // Loop through from center rows and fill any open seats until there are no seats left to asign
        while (seatsLeftToAssign > 0) {
            if (backRowPointer >= theaterRows && frontRowPointer < 0) { // No room found to accomodate group
                break;
            }

            seatsLeftToAssign = FillAnyEmptySeats(backRowPointer, seatsLeftToAssign, reservationCodes);
            seatsLeftToAssign = FillAnyEmptySeats(frontRowPointer, seatsLeftToAssign, reservationCodes);

            frontRowPointer--;
            backRowPointer++;
        }

        reservation.AssignSeats(reservationCodes);
        reservations.add(reservation);
    }

    /**
     * see if row can fit entire group
     */
    private int CheckRoomForFullGroup(int rowPointer, int seatsLeftToAssign, ArrayList<String> reservationCodes) { 
        int firstSeatInlineup = -1;
        int currentSeatInlineup = 0; // points to current seat in line of empty seats

            if (!(rowPointer >= theaterRows || rowPointer < 0)) { // see if pointer is in range of rows
                for (int i = 0; i < theaterSeatsPerRow; i++) {
                    if (seatsLeftToAssign <= 0) {
                        break;
                    } else if (seatsLeft[rowPointer] < seatsLeftToAssign) {
                        break;
                    }

                    if (seats[rowPointer][i] == 0 && firstSeatInlineup == -1) {
                        firstSeatInlineup = i;
                        currentSeatInlineup = i;
                    } else if (seats[rowPointer][i] == 0) {
                        currentSeatInlineup = i;
                        if ((currentSeatInlineup + 1) - firstSeatInlineup == seatsLeftToAssign) {
                            for (int j = firstSeatInlineup; j <= currentSeatInlineup; j++) {
                                seats[rowPointer][j] = 1;
                                totalSeatsLeft--;
                                seatsLeftToAssign--;
                                seatsLeft[rowPointer] = seatsLeft[rowPointer] - 1;
                                reservationCodes.add(seatCodes.get(rowPointer).get(j));
                            }
                            int buffer = seatSafetyBuffer;
                            currentSeatInlineup++;
                            while (buffer > 0 && currentSeatInlineup < theaterSeatsPerRow) {
                                seats[rowPointer][currentSeatInlineup] = 2;
                                buffer--;
                                currentSeatInlineup++;
                            }
                        }
                    } else {
                        firstSeatInlineup = -1;
                    }
                }
            }
        return seatsLeftToAssign;
    }

    /**
     * fill any seats in row if there are seats left to assign
     */
    private int FillAnyEmptySeats(int rowPointer, int seatsLeftToAssign, ArrayList<String> reservationCodes) {
        
        if (!(rowPointer >= theaterRows || rowPointer < 0)) { // fill in any empty seats in row
            for (int i = 0; i < theaterSeatsPerRow; i++) {
                if (seatsLeftToAssign <= 0) {
                    break;
                } else if (seatsLeft[rowPointer] <= 0) {
                    break;
                }

                if (seats[rowPointer][i] == 0) {
                    seats[rowPointer][i] = 1;
                    totalSeatsLeft--;
                    seatsLeftToAssign--;
                    seatsLeft[rowPointer] = seatsLeft[rowPointer] - 1;
                    reservationCodes.add(seatCodes.get(rowPointer).get(i));

                    if (seatsLeftToAssign <= 0) {
                        int buffer = seatSafetyBuffer;
                        int index = i + 1;
                        while (buffer > 0 && index < theaterSeatsPerRow) {
                            seats[rowPointer][index] = 2;
                            buffer--;
                            index++;
                        }
                    }
                }
            }
        }
        return seatsLeftToAssign;
    }

    /**
     * write each assigned reservation to output file in the following string format: R### A#,B#,C##
     */
    public void PrintReservations() { 
        try {
            FileWriter myWriter = new FileWriter("output.txt");

            if (reservations != null) {
                for (Reservation reservation : reservations) {
                    myWriter.write(reservation.PrintSeats());
                }
            }
            
            myWriter.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
    }

    /**
     * prints entire theater seating reservations in a visual manner, with x = taken, / = blocked, and _ = available
     */
    public void PrintTheaterLayout() {
        // Screen Line
        StringBuilder screen = new StringBuilder();
        screen.append("  [[");
        for (int i = 0; i < ((theaterSeatsPerRow / 2) - 5); i++) {
            screen.append(" ");
        }
        screen.append("SCREEN");
        for (int i = 0; i < ((theaterSeatsPerRow / 2) - 5); i++) {
            screen.append(" ");
        }
        screen.append("]]  ");
        System.out.println(screen);

        // Dashed Line
        StringBuilder dashes = new StringBuilder();
        dashes.append("  ");
        for (int i = 0; i < theaterSeatsPerRow; i++) {
            dashes.append("-");
        }
        System.out.println(dashes);

        // Rows
        char rowLetter = 'A';
        StringBuilder currentRow;
        for (int row = 0; row < theaterRows; row++) {
            currentRow = new StringBuilder();
            currentRow.append(rowLetter).append(" ");
            for (int col = 0; col < theaterSeatsPerRow; col++) {
                if (seats[row][col] == 0) {
                    currentRow.append("_");
                } else if (seats[row][col] == 1){
                    currentRow.append("x");
                } else {
                    currentRow.append("/");
                }
            }
            System.out.println(currentRow);
            rowLetter++;
        }
    }
}
