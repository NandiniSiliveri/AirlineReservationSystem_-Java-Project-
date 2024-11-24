import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class FlightBookingApp {
    // Data structures for storing user and flight information
    private static Map<String, String> users = new HashMap<>();
    private static Map<String, Flight> flights = new HashMap<>();
    private static JFrame frame = new JFrame("Flight Booking System");
    private static String loggedInUser = null;

    public static void main(String[] args) {
        // Preloaded flight data
        loadFlights();

        // Show main menu
        showMainMenu();
    }

    private static void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Welcome to the Flight Booking System!", JLabel.CENTER);
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        frame.add(label);
        frame.add(registerButton);
        frame.add(loginButton);
        frame.add(exitButton);

        registerButton.addActionListener(e -> showRegistrationScreen());
        loginButton.addActionListener(e -> showLoginScreen());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void showRegistrationScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(registerButton);
        frame.add(backButton);

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(frame, "Username already exists. Try another.");
            } else {
                users.put(username, password);
                JOptionPane.showMessageDialog(frame, "Registration successful!");
                showMainMenu();
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.setVisible(true);
    }

    private static void showLoginScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(loginButton);
        frame.add(backButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (users.containsKey(username) && users.get(username).equals(password)) {
                loggedInUser = username;
                JOptionPane.showMessageDialog(frame, "Login successful!");
                showUserMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        frame.setVisible(true);
    }

    private static void showUserMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Welcome, " + loggedInUser + "!", JLabel.CENTER);
        JButton searchButton = new JButton("Search Flights");
        JButton viewButton = new JButton("View Flights");
        JButton logoutButton = new JButton("Logout");

        frame.add(label);
        frame.add(searchButton);
        frame.add(viewButton);
        frame.add(logoutButton);

        searchButton.addActionListener(e -> showSearchScreen());
        viewButton.addActionListener(e -> showFlightDetailsScreen());
        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            showMainMenu();
        });

        frame.setVisible(true);
    }

    private static void showSearchScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));

        JLabel departureLabel = new JLabel("Departure:");
        JTextField departureField = new JTextField();
        JLabel destinationLabel = new JLabel("Destination:");
        JTextField destinationField = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        frame.add(departureLabel);
        frame.add(departureField);
        frame.add(destinationLabel);
        frame.add(destinationField);
        frame.add(searchButton);
        frame.add(backButton);

        searchButton.addActionListener(e -> {
            String departure = departureField.getText();
            String destination = destinationField.getText();

            StringBuilder result = new StringBuilder("Available Flights:\n");
            boolean found = false;
            for (Flight flight : flights.values()) {
                if (flight.getDepartureCity().equalsIgnoreCase(departure) &&
                        flight.getDestinationCity().equalsIgnoreCase(destination)) {
                    result.append(flight).append("\n");
                    found = true;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame, "No flights found.");
            } else {
                JOptionPane.showMessageDialog(frame, result.toString());
            }
        });

        backButton.addActionListener(e -> showUserMenu());

        frame.setVisible(true);
    }

    private static void showFlightDetailsScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(2, 1));

        JTextField flightIdField = new JTextField();
        JButton viewButton = new JButton("View Flight Details");
        JButton backButton = new JButton("Back");

        frame.add(flightIdField);
        frame.add(viewButton);
        frame.add(backButton);

        viewButton.addActionListener(e -> {
            String flightId = flightIdField.getText();

            if (flights.containsKey(flightId)) {
                Flight flight = flights.get(flightId);
                JOptionPane.showMessageDialog(frame, flight.toString());
                int seat = Integer.parseInt(JOptionPane.showInputDialog("Enter seat number to book:"));
                if (flight.reserveSeat(seat)) {
                    JOptionPane.showMessageDialog(frame, "Seat booked successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid or already reserved seat.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Flight not found.");
            }
        });

        backButton.addActionListener(e -> showUserMenu());

        frame.setVisible(true);
    }

    private static void loadFlights() {
        flights.put("FL123", new Flight("FL123", "New York", "Los Angeles", 10));
        flights.put("FL124", new Flight("FL124", "San Francisco", "Chicago", 8));
        flights.put("FL125", new Flight("FL125", "Houston", "Miami", 12));
    }
}

class Flight {
    private String flightId;
    private String departureCity;
    private String destinationCity;
    private boolean[] seats;

    public Flight(String flightId, String departureCity, String destinationCity, int seatCount) {
        this.flightId = flightId;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.seats = new boolean[seatCount];
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public boolean reserveSeat(int seatNumber) {
        if (seatNumber > 0 && seatNumber <= seats.length && !seats[seatNumber - 1]) {
            seats[seatNumber - 1] = true;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Flight ID: " + flightId +
                ", From: " + departureCity +
                ", To: " + destinationCity +
                ", Total Seats: " + seats.length;
    }
}
