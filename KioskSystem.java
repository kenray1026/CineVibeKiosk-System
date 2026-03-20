/*
   Members Name: Ray Kenzhe Macaya
                 Kirk Ramirez Burgos
                 Rylle Abellanosa
                 Ashainna Ramas
   Group Name: The syntax errors squad
   Project Name: Enhanced Movie House Kiosk System
   Date: December 27, 2025
   Description: Final Project - Exploring Java OOP with Asynchronous Programming 
*/

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File; 
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.swing.plaf.basic.BasicTabbedPaneUI; // Needed to hide the tabs

/**
 * Main class to encapsulate all Data Models, Controllers, and GUI Panels
 * for the Enhanced Movie House Kiosk System.
 * * Includes: Dark Blue UI, Sidebar, 4x4 Grids, Correct Room Occupancy Logic, 
 * Room Cancellation, and Detailed Receipt Generation.
 */
class KioskSystem {

    // --- 0. UI CONSTANTS ---
    private static final Color DARK_BLUE = new Color(20, 30, 60);
    private static final Color LIGHT_BLUE = new Color(40, 60, 100);
    private static final Color TEXT_LIGHT = Color.WHITE;
    private static final Color TEXT_ACCENT = new Color(255, 165, 0); // Orange
    
    // --- 0. RECEIPT CONSTANTS ---
    private static final String SYSTEM_NAME = "CineVibe Movie House";
    private static final String ADDRESS = "62-C Maria Christina, Capitol Site Cebu City";

    // =================================================================
    // 1. DATA MODEL CLASSES (The Blueprints)
    // =================================================================

    public static class Movie {
        private String title;
        private String genre;
        private int durationMinutes;
        private double basePrice;

        public Movie(String title, String genre, int durationMinutes) {
            this.title = title;
            this.genre = genre;
            this.durationMinutes = durationMinutes;
            this.basePrice = 100.00 + (durationMinutes * 0.50);
        }

        // Getters
        public String getTitle() { return title; }
        public String getGenre() { return genre; }
        public int getDurationMinutes() { return durationMinutes; }
        public double getBasePrice() { return basePrice; }

        // Setter for Admin
        public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
        
        // Setters for Admin editing
        public void setTitle(String title) { this.title = title; }
        public void setGenre(String genre) { this.genre = genre; }
    }

    public static class FoodItem {
        private String name;
        private double price;
        private String category;

        public FoodItem(String name, double price, String category) {
            this.name = name;
            this.price = price;
            this.category = category;
        }

        // Getters
        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }

        // Setter for Admin
        public void setPrice(double price) { this.price = price; }
        
        // Setters for Admin editing
        public void setName(String name) { this.name = name; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class Room {
        private int roomNumber;
        private String capacity;
        private boolean isOccupied;
        private JButton roomButton; 

        public Room(int roomNumber, String capacity) {
            this.roomNumber = roomNumber;
            this.capacity = capacity;
            this.isOccupied = false;
        }

        // Getters
        public int getRoomNumber() { return roomNumber; }
        public String getCapacity() { return capacity; }
        public boolean isOccupied() { return isOccupied; }

        // Setter
        public void setOccupied(boolean occupied) { 
            this.isOccupied = occupied; 
            updateRoomButton(this);
        }
        
        // Link GUI button
        public void setRoomButton(JButton button) { this.roomButton = button; }
        public JButton getRoomButton() { return roomButton; }

        // Helper to update room button appearance
        private void updateRoomButton(Room room) {
            if (room.getRoomButton() != null) {
                if (room.isOccupied()) {
                    room.getRoomButton().setBackground(Color.RED);
                    room.getRoomButton().setText("OCCUPIED");
                } else {
                    room.getRoomButton().setBackground(new Color(60, 179, 113)); // Green
                    room.getRoomButton().setText("Room " + room.getRoomNumber() + " (" + room.getCapacity() + ")");
                }
            }
        }
    }

    public static class Transaction {
        private static int nextId = 1000;
        private int transactionId;
        private Movie selectedMovie;
        private List<FoodItem> selectedFood;
        private Room selectedRoom;
        private double totalPrice;
        private Date transactionDate;

        public Transaction(Movie movie, List<FoodItem> food, Room room, double total) {
            this.transactionId = nextId++;
            this.selectedMovie = movie;
            this.selectedFood = food;
            this.selectedRoom = room;
            this.totalPrice = total;
            this.transactionDate = new Date();
        }

        // Getters
        public int getTransactionId() { return transactionId; }
        public Movie getSelectedMovie() { return selectedMovie; }
        public List<FoodItem> getSelectedFood() { return selectedFood; }
        public Room getSelectedRoom() { return selectedRoom; }
        public double getTotalPrice() { return totalPrice; }
        public Date getTransactionDate() { return transactionDate; }
    }

    // =================================================================
    // 2. CORE LOGIC/SYSTEM CLASSES (The Brain)
    // =================================================================

    public static class DataController {
        private static DataController instance;
        private List<Movie> movies;
        private List<FoodItem> foodMenu;
        private List<Room> rooms;
        private List<Transaction> transactions;

        private DataController() {
            this.movies = new ArrayList<>();
            this.foodMenu = new ArrayList<>();
            this.rooms = new ArrayList<>();
            this.transactions = new ArrayList<>();
            initializeData();
        }
        
        public static DataController getInstance() {
            if (instance == null) {
                instance = new DataController();
            }
            return instance;
        }

        private void initializeData() {
            // --- 1. Movies ---
            movies.add(new Movie("Diablo", "Action", 250));
            movies.add(new Movie("Extraction 2", "Action", 350));
            movies.add(new Movie("Mission Impossible - Dead Reckoning", "Action", 350));
            
            movies.add(new Movie("Bridget Jones: Mad About the Boy", "Romance", 250));
            movies.add(new Movie("50 Shades Darker", "Romance", 250));
            movies.add(new Movie("Sosyal Climbers", "Romance", 250));
            
            movies.add(new Movie("Conjuring - The Last Rites", "Horror", 250));
            movies.add(new Movie("Wrong Turn", "Horror", 250));
            movies.add(new Movie("Sinners", "Horror", 250));

            movies.add(new Movie("A Nice Indian Boy", "Comedy", 250));
            movies.add(new Movie("Strays", "Comedy", 250));
            movies.add(new Movie("Back in Action", "Comedy", 250));
            
            movies.add(new Movie("Transformers - Revenge of the Fallen", "Sci-Fi", 350));
            movies.add(new Movie("Avatar - The Way of the Water", "Sci-Fi", 350));
            movies.add(new Movie("Avengers - Endgame", "Sci-Fi", 350));
            
            movies.add(new Movie("KPOP Demon Hunter", "Cartoon", 250));
            movies.add(new Movie("Ne Zha 2", "Cartoon", 250));
            movies.add(new Movie("Moana 2", "Cartoon", 250));
            
            movies.add(new Movie("Uncharted", "Adventure", 250));
            movies.add(new Movie("Strange World", "Adventure", 250));
            movies.add(new Movie("Jurassic World: Rebirth", "Adventure", 350));
            
            movies.add(new Movie("Chainsaw Man: The Movie", "Anime Movies", 350));
            movies.add(new Movie("Suzume", "Anime Movies", 250));
            movies.add(new Movie("Demon Slayers: Infinity Castle", "Anime Movies", 350));
            
            // The trending movies 
            movies.add(new Movie("Avatar: Fire and Ash", "Trending", 450)); 
            movies.add(new Movie("Together", "Trending", 450)); 
            movies.add(new Movie("Zootopia 2", "Trending", 450)); 
            

            // --- 2. Food & Drinks Menu ---
            foodMenu.add(new FoodItem("Large Fries", 150.00, "Snacks"));
            foodMenu.add(new FoodItem("Chicken Poppers", 99.00, "Snacks"));
            foodMenu.add(new FoodItem("Popcorn", 120.00, "Snacks"));
            foodMenu.add(new FoodItem("Nachos & Cheese Dip", 100.00, "Snacks"));
            foodMenu.add(new FoodItem("Pizza Slices", 115.00, "Snacks"));
            foodMenu.add(new FoodItem("Takoyaki (6pcs)", 70.00, "Snacks"));
            foodMenu.add(new FoodItem("Brownies (3pcs)", 35.00, "Snacks"));
            foodMenu.add(new FoodItem("Burger", 130.00, "Snacks"));
            foodMenu.add(new FoodItem("Hotdogs with a bun", 45.00, "Snacks"));
            foodMenu.add(new FoodItem("Pineapple Juice", 45.00, "Drinks"));
            foodMenu.add(new FoodItem("Water bottle 20ml", 25.00, "Drinks"));
            foodMenu.add(new FoodItem("Coke can", 60.00, "Drinks"));
            foodMenu.add(new FoodItem("Ice tea", 35.00, "Drinks"));
            
            // --- 3. Rooms ---
            rooms.add(new Room(101, "2-3 persons"));
            rooms.add(new Room(102, "2-3 persons"));
            rooms.add(new Room(201, "8-10 persons"));
            rooms.add(new Room(301, "5 persons"));
            rooms.add(new Room(302, "5 persons"));
        }

        // Getters
        public List<String> getAllGenres() {
            return movies.stream()
                         .map(Movie::getGenre)
                         .distinct()
                         .filter(g -> !g.equals("Trending"))
                         .collect(Collectors.toList());
        }
        public List<Movie> getMoviesByGenre(String genre) {
            return movies.stream()
                         .filter(m -> m.getGenre().equals(genre))
                         .collect(Collectors.toList());
        }
        // MODIFIED: Only return the three requested movies for the non-scrolling trending panel
        public List<Movie> getTrendingMovies() {
             return movies.stream()
                         .filter(m -> m.getGenre().equals("Trending"))
                         .filter(m -> m.getTitle().equals("Avatar: Fire and Ash") || 
                                      m.getTitle().equals("Together") || 
                                      m.getTitle().equals("Zootopia 2"))
                         .collect(Collectors.toList());
        }
        
        public List<FoodItem> getFoodMenu() { return foodMenu; }
        public List<Room> getRooms() { return rooms; }
        public List<Transaction> getTransactions() { return transactions; }

        public void addTransaction(Transaction t) {
            this.transactions.add(t);
        }
    }

    public static class OrderManager {
        private Movie selectedMovie;
        private List<FoodItem> foodOrder;
        private Room selectedRoom;
        private JLabel priceLabel;

        public OrderManager(JLabel priceLabel) {
            this.foodOrder = new ArrayList<>();
            this.priceLabel = priceLabel;
            updateTotalPrice();
        }

        public void resetOrder() {
            // Room occupied status is NOT reset here, only the current order selection.
            this.selectedMovie = null;
            this.foodOrder.clear();
            this.selectedRoom = null; 
            updateTotalPrice();
        }

        public void selectMovie(Movie movie) {
            this.selectedMovie = movie;
            updateTotalPrice();
        }
        
        public void addFoodItem(FoodItem item) {
            this.foodOrder.add(item);
            updateTotalPrice();
        }

        public boolean selectRoom(Room room) {
            // Check if the room is occupied by someone else's *finalized* order
            if (room.isOccupied()) {
                return false; 
            }
            
            // Allow selection, even if another customer is currently "holding" it before finalizing
            this.selectedRoom = room;
            updateTotalPrice();
            return true;
        }
        
        public void cancelSelectedRoom() {
            this.selectedRoom = null;
            updateTotalPrice();
        }

        public void updateTotalPrice() {
            double total = 0.0;
            if (selectedMovie != null) {
                total += selectedMovie.getBasePrice();
            }
            for (FoodItem item : foodOrder) {
                total += item.getPrice();
            }
            
            priceLabel.setText(String.format("Current Total: PHP %.2f", total));
        }
        
        public void finalizeTransaction() {
            if (selectedMovie == null) {
                JOptionPane.showMessageDialog(null, "Please select a movie first.", "Order Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selectedRoom == null) {
                JOptionPane.showMessageDialog(null, "Please select a room.", "Order Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 1. Mark Room as OCCUPIED (CRITICAL FIX: This stays occupied!)
            selectedRoom.setOccupied(true); 

            // 2. Calculate Total
            double total = selectedMovie.getBasePrice() + foodOrder.stream().mapToDouble(FoodItem::getPrice).sum();
            
            // 3. Create and Save Transaction
            Transaction t = new Transaction(selectedMovie, new ArrayList<>(foodOrder), selectedRoom, total);
            DataController.getInstance().addTransaction(t);
            
            // 4. Generate and Display Receipt
            displayReceipt(t);

            // 5. Reset Order for Next Customer (Room remains occupied)
            this.selectedMovie = null;
            this.foodOrder.clear();
            this.selectedRoom = null; 
            updateTotalPrice(); 
        }
        
        private void displayReceipt(Transaction t) {
            StringBuilder receipt = new StringBuilder();
            
            // Header
            receipt.append("=========================================\n");
            receipt.append(String.format("%-40s\n", SYSTEM_NAME));
            receipt.append(String.format("%-40s\n", ADDRESS));
            receipt.append("=========================================\n");
            receipt.append(String.format("ID: %d\n", t.getTransactionId()));
            receipt.append(String.format("Date: %s\n", t.getTransactionDate()));
            receipt.append("-----------------------------------------\n");
            
            // Movie/Room Details
            receipt.append(String.format("Movie Name: %s\n", t.getSelectedMovie().getTitle()));
            receipt.append(String.format("Room: %d\n", t.getSelectedRoom().getRoomNumber()));
            receipt.append("-----------------------------------------\n");
            
            // Itemized Details
            receipt.append(String.format("%-25s %15s\n", "ITEM", "PRICE"));
            
            // Movie price item
            receipt.append(String.format("%-25s PHP %10.2f\n", 
                                         "Movie Ticket", 
                                         t.getSelectedMovie().getBasePrice()));
            
            // Food items (Grouping by name and counting for cleaner receipt)
            Map<String, List<FoodItem>> foodGroup = t.getSelectedFood().stream()
                .collect(Collectors.groupingBy(FoodItem::getName));
                
            for (Map.Entry<String, List<FoodItem>> entry : foodGroup.entrySet()) {
                double totalItemPrice = entry.getValue().stream().mapToDouble(FoodItem::getPrice).sum();
                int count = entry.getValue().size();
                String itemDisplay = count > 1 ? String.format("%s (x%d)", entry.getKey(), count) : entry.getKey();
                 receipt.append(String.format("%-25s PHP %10.2f\n", 
                                         itemDisplay, 
                                         totalItemPrice));
            }

            receipt.append("-----------------------------------------\n");
            
            // Total
            receipt.append(String.format("%-25s PHP %10.2f\n", 
                                         "TOTAL AMOUNT DUE", 
                                         t.getTotalPrice()));
            receipt.append("=========================================\n");
            receipt.append(String.format("%-40s\n", "Thank You for choosing CineVibe!"));

            JTextArea receiptArea = new JTextArea(receipt.toString());
            receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            receiptArea.setEditable(false);
            
            JScrollPane scrollPane = new JScrollPane(receiptArea);
            scrollPane.setPreferredSize(new Dimension(400, 450));
            
            JOptionPane.showMessageDialog(null, 
                                          scrollPane, 
                                          "Order Receipt - ID " + t.getTransactionId(), 
                                          JOptionPane.PLAIN_MESSAGE);
        }
    }


    // =================================================================
    // 3. GUI/VIEW CLASSES (The Interface)
    // =================================================================
    
    public static class GridPanel extends JPanel {
        private final OrderManager manager;

        public GridPanel(OrderManager manager) {
            this.manager = manager;
            setBackground(DARK_BLUE);
            setLayout(new GridLayout(4, 4, 15, 15));
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }
        
        public void loadMovies(List<Movie> movies) {
            removeAll();
            for (Movie movie : movies) {
                // Default movie card size (standard 4x4 grid size)
                add(createMovieCard(movie, new Dimension(180, 350))); 
            }
            revalidate();
            repaint();
        }
        
        public void loadFood(List<FoodItem> foodList) {
            removeAll();
            for (FoodItem item : foodList) {
                add(createFoodCard(item));
            }
            revalidate();
            repaint();
        }

        public void loadRooms(List<Room> rooms) {
            removeAll();
            for (Room room : rooms) {
                add(createRoomCard(room));
            }
            revalidate();
            repaint();
        }
        
        // --- Card Creation Methods ---
        
        // ** MODIFIED METHOD to accept a preferred size **
        public JPanel createMovieCard(Movie movie, Dimension preferredSize) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(LIGHT_BLUE);
            card.setBorder(new LineBorder(DARK_BLUE.brighter(), 2));
            card.setPreferredSize(preferredSize);
            card.setMaximumSize(preferredSize); // Important for BoxLayout!
            
            // Image Placeholder Label
            JLabel imgLabel = new JLabel("[Movie Poster]", SwingConstants.CENTER);
            imgLabel.setForeground(TEXT_LIGHT.darker());
            imgLabel.setPreferredSize(new Dimension(50, 50)); 
            
            // ----------------------------------------------------------------------------------
            // VVVVVV IMAGE LOADING IMPLEMENTATION VVVVVV
            // ----------------------------------------------------------------------------------
            
            int targetWidth = preferredSize.width - 18; // 20px padding
            int targetHeight = preferredSize.height - 110; // Account for text/button space
            
            if (targetHeight <= 0) targetHeight = 150; // Fallback height if math fails
            if (targetWidth <= 0) targetWidth = 120;
            
            String imageFileName = null;
            if (movie.getTitle().equals("Avatar: Fire and Ash")) {
                // COMMENT: Place the 'avatar_fire_and_ash.jpg' file in the root directory.
                imageFileName = "avatar_fire_and_ash.jpg";
            } else if (movie.getTitle().equals("Together")) {
                // COMMENT: Place the 'Together.jpg' file in the root directory.
                imageFileName = "2Together.jpg";
            } else if (movie.getTitle().equals("Zootopia 2")) {
                // COMMENT: Place the 'Zootopia.jpg' file (or Zootopia 2 poster) in the root directory.
                imageFileName = "3Zootopia.jpg";
            
               //Action
            } else if (movie.getTitle().equals("Diablo")) {
                imageFileName = "Diablo.jpg";
            } else if (movie.getTitle().equals("Extraction 2")) {
                imageFileName = "Extraction2.jpg";
            } else if (movie.getTitle().equals("Mission Impossible - Dead Reckoning")) {
                imageFileName = "MissionImpossible.jpg";
                
                //Romance
            } else if (movie.getTitle().equals("Bridget Jones: Mad About the Boy")) {
                imageFileName = "bridgetjones.jpg";
            } else if (movie.getTitle().equals("50 Shades Darker")) {
                imageFileName = "50shades.jpg";
            } else if (movie.getTitle().equals("Sosyal Climbers")) {
                imageFileName = "sosyalclimbers.jpg";
            
               //Horror
            } else if (movie.getTitle().equals("Conjuring - The Last Rites")) {
                imageFileName = "conjuring.jpg";
            } else if (movie.getTitle().equals("Wrong Turn")) {
                imageFileName = "wrongturn.jpg";
            } else if (movie.getTitle().equals("Sinners")) {
                imageFileName = "sinners.jpg";
            
               //Comedy
            } else if (movie.getTitle().equals("A Nice Indian Boy")) {
                imageFileName = "aniceindianboy.jpg";
            } else if (movie.getTitle().equals("Strays")) {
                imageFileName = "stray.jpg";
            } else if (movie.getTitle().equals("Back in Action")) {
                imageFileName = "backinaction.jpg";
            
               //Sci-Fi
            } else if (movie.getTitle().equals("Avatar - The Way of the Water")) {
                imageFileName = "Avatarwayofwater.jpg";
            } else if (movie.getTitle().equals("Transformers - Revenge of the Fallen")) {
                imageFileName = "tf2.jpg";
            } else if (movie.getTitle().equals("Avengers - Endgame")) {
                imageFileName = "avengersendgame.jpg";
            
               //Cartoon
            } else if (movie.getTitle().equals("KPOP Demon Hunter")) {
                imageFileName = "Kpop.jpg";
            } else if (movie.getTitle().equals("Ne Zha 2")) {
                imageFileName = "nezha2.jpg";
            } else if (movie.getTitle().equals("Moana 2")) {
                imageFileName = "Moana2.jpg";
            
               //Adventure
            } else if (movie.getTitle().equals("Uncharted")) {
                imageFileName = "uncharted.jpg";
            } else if (movie.getTitle().equals("Strange World")) {
                imageFileName = "strangeworld.jpg";
            } else if (movie.getTitle().equals("Jurassic World: Rebirth")) {
                imageFileName = "jurassicworld.jpg";
            
               //Anime Movies
            } else if (movie.getTitle().equals("Chainsaw Man: The Movie")) {
                imageFileName = "chainsawman.jpg";
            } else if (movie.getTitle().equals("Suzume")) {
                imageFileName = "suzume.jpg";
            } else if (movie.getTitle().equals("Demon Slayers: Infinity Castle")) {
                imageFileName = "demoncastle.jpg";
            }

            
                     
            if (imageFileName != null) {
                try {
                    File imageFile = new File(imageFileName);
                    
                    if (imageFile.exists()) {
                        ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath()); 
                        
                        // Set specific image size for trending cards to maximize display
                        if (movie.getGenre().equals("Trending")) {
                            targetWidth = preferredSize.width - 20; 
                            targetHeight = preferredSize.height - 80;
                        }
                        
                        Image scaledImage = icon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH); 
                        imgLabel.setIcon(new ImageIcon(scaledImage));
                        imgLabel.setText(""); // Clear placeholder text
                        imgLabel.setPreferredSize(new Dimension(targetWidth, targetHeight)); 
                    } else {
                        imgLabel.setText(imageFileName + " Missing");
                        imgLabel.setPreferredSize(new Dimension(targetWidth, targetHeight)); 
                    }
                } catch (Exception e) {
                    System.err.println("Could not load image for: " + movie.getTitle() + " Error: " + e.getMessage());
                }
            } else {
                // Fallback for non-trending or generic trending movies
                if (targetHeight < 50) targetHeight = 50; 
                if (targetWidth < 50) targetWidth = 50;
                imgLabel.setText("[Movie Poster]");
                imgLabel.setPreferredSize(new Dimension(targetWidth, targetHeight)); 
            }
            
            // ----------------------------------------------------------------------------------
            // ^^^^^^ END IMAGE LOADING IMPLEMENTATION ^^^^^^
            // ----------------------------------------------------------------------------------
            
            JLabel titleLabel = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            titleLabel.setForeground(TEXT_ACCENT);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JLabel priceLabel = new JLabel("PHP " + String.format("%.2f", movie.getBasePrice()), SwingConstants.CENTER);
            priceLabel.setForeground(TEXT_LIGHT);
            priceLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            
            JButton selectButton = new JButton("Select");
            selectButton.setBackground(new Color(60, 179, 113)); 
            selectButton.setForeground(TEXT_LIGHT);
            selectButton.addActionListener(e -> {
                manager.selectMovie(movie);
                JOptionPane.showMessageDialog(this, "Selected Movie: " + movie.getTitle());
            });

            card.add(Box.createVerticalGlue());
            card.add(imgLabel);
            card.add(Box.createVerticalStrut(5));
            card.add(titleLabel);
            card.add(Box.createVerticalStrut(2));
            card.add(priceLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(selectButton);
            card.add(Box.createVerticalGlue());
            
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            return card;
        }

        private JPanel createFoodCard(FoodItem item) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(LIGHT_BLUE);
            card.setBorder(new LineBorder(DARK_BLUE.brighter(), 2));
    
            // Default placeholder
            JLabel imgLabel = new JLabel("[Food Image]", SwingConstants.CENTER);
            imgLabel.setForeground(TEXT_LIGHT.darker());
            imgLabel.setPreferredSize(new Dimension(160, 130)); // Increased size for visibility

            // --- IMAGE LOADING LOGIC ---
            String foodImageFile = null;
    
            // Match the image to the FoodItem name
            if (item.getName().equalsIgnoreCase("Large Fries")) { //IMMMAAAGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
               foodImageFile = "LargeFries.png";
               } else if (item.getName().equalsIgnoreCase("Chicken Poppers")) {
                  foodImageFile = "chickenpoppers.png";
               } else if (item.getName().contains("Popcorn")) {
            // Example for themed items
                  foodImageFile = "popcorn.png";
               } else if (item.getName().contains("Nachos & Cheese Dip")) {
                  foodImageFile = "nachocheese.png";
               } else if (item.getName().contains("Pizza Slices")) {
                  foodImageFile = "pizza.png";
               } else if (item.getName().contains("Takoyaki (6pcs)")) {
                  foodImageFile = "takoyaki.png";
               } else if (item.getName().contains("Brownies (3pcs)")) {
                  foodImageFile = "brownies.png";
               } else if (item.getName().contains("Burger")) {
                  foodImageFile = "burger.png";
               } else if (item.getName().contains("Hotdogs with a bun")) {
                  foodImageFile = "hotdog.png";
               } else if (item.getName().contains("Pineapple Juice")) {
                  foodImageFile = "pineapplejuice.png";
               } else if (item.getName().contains("Water bottle 20ml")) {
                  foodImageFile = "bottlewater.png";
               } else if (item.getName().contains("Coke can")) {
                  foodImageFile = "coke.png";
               } else if (item.getName().contains("Ice tea")) {
                  foodImageFile = "icetea.png";
               }

      if (foodImageFile != null) {
          try {
               File f = new File(foodImageFile);
               if (f.exists()) {
                   ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                   // Scaling the food image to fit nicely in the card
                   Image scaled = icon.getImage().getScaledInstance(160, 130, Image.SCALE_SMOOTH);
                   imgLabel.setIcon(new ImageIcon(scaled));
                   imgLabel.setText(""); // Remove the "[Food Image]" text
            } else {
                imgLabel.setText("Missing: " + foodImageFile);
            }
        } catch (Exception e) {
            System.err.println("Error loading food image: " + e.getMessage());
        }
    }
    // ----------------------------

    JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
    nameLabel.setForeground(TEXT_ACCENT);
    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

    JLabel priceLabel = new JLabel("PHP " + String.format("%.2f", item.getPrice()), SwingConstants.CENTER);
    priceLabel.setForeground(TEXT_LIGHT);
    priceLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    
    JButton addButton = new JButton("Add");
    addButton.setBackground(new Color(60, 179, 113)); 
    addButton.setForeground(TEXT_LIGHT);
    addButton.addActionListener(e -> {
        manager.addFoodItem(item);
        JOptionPane.showMessageDialog(this, item.getName() + " added.");
    });

    // Layout components
    card.add(Box.createVerticalGlue());
    card.add(imgLabel);
    card.add(Box.createVerticalStrut(5));
    card.add(nameLabel);
    card.add(Box.createVerticalStrut(2));
    card.add(priceLabel);
    card.add(Box.createVerticalStrut(10));
    card.add(addButton);
    card.add(Box.createVerticalGlue());
    
    // Center alignments
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    return card;
}
        private JPanel createRoomCard(Room room) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(LIGHT_BLUE);
            card.setBorder(new LineBorder(DARK_BLUE.brighter(), 2));
            
            // Removed: imgLabel (no "Room View")
            
            JLabel numberLabel = new JLabel("Room " + room.getRoomNumber(), SwingConstants.CENTER);
            numberLabel.setForeground(TEXT_ACCENT);
            numberLabel.setFont(new Font("Arial", Font.BOLD, 18));
            
            JLabel capacityLabel = new JLabel(room.getCapacity(), SwingConstants.CENTER);
            capacityLabel.setForeground(TEXT_LIGHT);
            capacityLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JButton selectButton = new JButton("Select");
            selectButton.setForeground(TEXT_LIGHT);

            room.setRoomButton(selectButton); 

            selectButton.addActionListener(e -> {
                if (manager.selectRoom(room)) {
                    JOptionPane.showMessageDialog(this, "Room " + room.getRoomNumber() + " selected! Check View Cart.");
                } else {
                    JOptionPane.showMessageDialog(this, "Room " + room.getRoomNumber() + " is currently OCCUPIED.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Initial button state management
            if (room.isOccupied()) {
                selectButton.setBackground(Color.RED);
                selectButton.setText("OCCUPIED");
            } else {
                selectButton.setBackground(new Color(60, 179, 113)); // Green
            }

            card.add(Box.createVerticalGlue());
            card.add(numberLabel);
            card.add(Box.createVerticalStrut(2));
            card.add(capacityLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(selectButton);
            card.add(Box.createVerticalGlue());
            
            numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            capacityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            return card;
        }
    }


    public static class TrendingMoviesPanel extends JPanel {
         public TrendingMoviesPanel(OrderManager manager) {
            // Use BoxLayout for vertical stacking of Header and Content
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(DARK_BLUE);
            
            JLabel header = new JLabel(" TODAY'S TOP TRENDING MOVIES ", SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 26));
            header.setForeground(TEXT_ACCENT);
            header.setBorder(new EmptyBorder(20, 0, 30, 0));
            header.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(header);
            
            List<Movie> trendingMovies = DataController.getInstance().getTrendingMovies();
            
            if (trendingMovies.isEmpty()) {
                JLabel noMovieLabel = new JLabel("No trending movie available.", SwingConstants.CENTER);
                noMovieLabel.setForeground(TEXT_LIGHT);
                noMovieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                add(noMovieLabel);
                add(Box.createVerticalGlue());
                return;
            }
            
            // Panel to hold the 3 static cards side-by-side
            JPanel contentPanel = new JPanel();
            // Use FlowLayout to center the cards horizontally
            contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0)); 
            contentPanel.setBackground(DARK_BLUE);
            contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); 

            // Card size calculated to fit 3 in the general window area
            Dimension cardSize = new Dimension(280, 500); 
            GridPanel cardCreator = new GridPanel(manager);

            // Add ALL (3) trending movies to the static content panel
            for (Movie movie : trendingMovies) {
                JPanel card = cardCreator.createMovieCard(movie, cardSize);
                contentPanel.add(card);
            }
            
            // Add content panel directly (NO SCROLL PANE)
            add(contentPanel); 
            add(Box.createVerticalGlue()); // Add glue to push content up/center
        }
    }
    
    // Remaining Panels (MoviesPanel, FoodDrinksPanel, RoomsPanel, Admin Panels) 
    // are structured identically to the previous working version.
   
   // This simplifies code because MoviesPanel automatically gets 
   // all the features of a JPanel without you writing them.
    public static class MoviesPanel extends JPanel {
        private final OrderManager manager;
        private final JTabbedPane genreTabs;

        public MoviesPanel(OrderManager manager) {
            this.manager = manager;
            setLayout(new BorderLayout());
            setBackground(DARK_BLUE);
            
            JLabel header = new JLabel(" Select a Movie", SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 22));
            header.setForeground(TEXT_LIGHT);
            add(header, BorderLayout.NORTH);
            
            genreTabs = new JTabbedPane();
            genreTabs.setBackground(DARK_BLUE.darker());
            genreTabs.setForeground(TEXT_ACCENT);
            
            DataController dc = DataController.getInstance();
            for (String genre : dc.getAllGenres()) {
                List<Movie> movies = dc.getMoviesByGenre(genre);
                if (!movies.isEmpty()) {
                    GridPanel listing = new GridPanel(manager);
                    listing.loadMovies(movies);
                    JScrollPane scrollPane = new JScrollPane(listing);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    scrollPane.setBorder(null);
                    genreTabs.addTab(genre, scrollPane);
                }
            }
            
            add(genreTabs, BorderLayout.CENTER);
        }
    }

    public static class FoodDrinksPanel extends JPanel {
        public FoodDrinksPanel(OrderManager manager) {
            setLayout(new BorderLayout(10, 10));
            setBackground(DARK_BLUE);
            
            JLabel header = new JLabel(" Foods & Drinks Menu ", SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 22));
            header.setForeground(TEXT_LIGHT);
            header.setBorder(new EmptyBorder(10, 0, 10, 0));
            add(header, BorderLayout.NORTH);

            GridPanel menuPanel = new GridPanel(manager);
            menuPanel.loadFood(DataController.getInstance().getFoodMenu());
            
            add(new JScrollPane(menuPanel), BorderLayout.CENTER);
        }
    }

    public static class RoomsPanel extends JPanel {
        public RoomsPanel(OrderManager manager) {
            setLayout(new BorderLayout(10, 10));
            setBackground(DARK_BLUE);
            
            JLabel header = new JLabel("Private Room Selection", SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 22));
            header.setForeground(TEXT_LIGHT);
            header.setBorder(new EmptyBorder(10, 0, 10, 0));
            add(header, BorderLayout.NORTH);
            
            GridPanel roomsContainer = new GridPanel(manager);
            roomsContainer.loadRooms(DataController.getInstance().getRooms());
            
            // Removed: Cancel Room Button (moved to ReviewOrderDialog)
            
            add(new JScrollPane(roomsContainer), BorderLayout.CENTER);
        }
    }

    // --- ADMIN TABS ---

    public static class AdminPanelManager extends JPanel {
        private final CardLayout cardLayout = new CardLayout();
        private final JPanel cardPanel = new JPanel(cardLayout);
        private final String LOGIN_CARD = "Login";
        private final String DASHBOARD_CARD = "Dashboard";

        public AdminPanelManager() {
            setLayout(new BorderLayout());
            setBackground(DARK_BLUE);
            
            AdminLoginForm loginForm = new AdminLoginForm(this);
            AdminDashboardPanel dashboard = new AdminDashboardPanel(this);

            cardPanel.add(loginForm, LOGIN_CARD);
            cardPanel.add(dashboard, DASHBOARD_CARD);
            cardPanel.setBackground(DARK_BLUE);
            
            add(cardPanel, BorderLayout.CENTER);
            showLogin();
        }

        public void showLogin() {
            cardLayout.show(cardPanel, LOGIN_CARD);
        }

        public void showDashboard() {
            AdminDashboardPanel dashboard = (AdminDashboardPanel) cardPanel.getComponent(1);
            dashboard.refreshData();
            cardLayout.show(cardPanel, DASHBOARD_CARD);
        }
    }

    public static class AdminLoginForm extends JPanel {
        public AdminLoginForm(AdminPanelManager manager) {
            setLayout(new GridBagLayout());
            setBackground(DARK_BLUE);
            
            JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            loginPanel.setBackground(LIGHT_BLUE);
            loginPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_ACCENT), 
                "Admin Login", 
                javax.swing.border.TitledBorder.CENTER, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 16), 
                TEXT_ACCENT));

            JTextField userField = new JTextField(15);
            JPasswordField passField = new JPasswordField(15);
            JButton loginButton = new JButton("Login");
            loginButton.setBackground(Color.BLUE.darker());
            loginButton.setForeground(TEXT_LIGHT);

            JLabel userLabel = new JLabel("Username:");
            userLabel.setForeground(TEXT_LIGHT);
            JLabel passLabel = new JLabel("Password:");
            passLabel.setForeground(TEXT_LIGHT);

            loginPanel.add(userLabel);
            loginPanel.add(userField);
            loginPanel.add(passLabel);
            loginPanel.add(passField);
            loginPanel.add(new JLabel());
            loginPanel.add(loginButton);

            loginButton.addActionListener(e -> {
                String user = userField.getText();
                String pass = new String(passField.getPassword());
                
                if (user.equals("admin") && pass.equals("123")) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    manager.showDashboard();
                    userField.setText("");
                    passField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            add(loginPanel);
        }
    }
    
    public static class AdminDashboardPanel extends JPanel {
        private final DataController dc = DataController.getInstance();
        private final AdminPanelManager manager;
        private JTabbedPane adminTabs;
        private JPanel salesPanel;
        private JPanel priceEditPanel;

        public AdminDashboardPanel(AdminPanelManager manager) {
            this.manager = manager;
            setLayout(new BorderLayout(10, 10));
            setBackground(DARK_BLUE);
            setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(DARK_BLUE);

            JLabel header = new JLabel(" Admin Dashboard", SwingConstants.CENTER);
            header.setFont(new Font("Arial", Font.BOLD, 24));
            header.setForeground(TEXT_LIGHT);
            topPanel.add(header, BorderLayout.CENTER);
            
            // Removed: Clear Room Status Button (replaced with Room Status tab)
            
            JButton logoutButton = new JButton("Log Out");
            logoutButton.setBackground(new Color(220, 20, 60)); 
            logoutButton.setForeground(TEXT_LIGHT);
            logoutButton.addActionListener(e -> {
                manager.showLogin();
                JOptionPane.showMessageDialog(this, "Logged out successfully.");
            });
            topPanel.add(logoutButton, BorderLayout.EAST);
            
            add(topPanel, BorderLayout.NORTH);

            adminTabs = new JTabbedPane();
            adminTabs.setBackground(DARK_BLUE.darker());
            adminTabs.setForeground(TEXT_ACCENT);
            add(adminTabs, BorderLayout.CENTER);
            
            salesPanel = new JPanel(new BorderLayout());
            priceEditPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            
            refreshData();
        }
        
        public void refreshData() {
            adminTabs.removeAll();
            
            // Room Status Tab
            JPanel roomStatusPanel = createRoomStatusPanel();
            adminTabs.addTab("Room Status", roomStatusPanel);
            
            // Transactions & Sales (unchanged)
            salesPanel.removeAll();
            salesPanel.add(createSalesSummaryPanel(), BorderLayout.NORTH);
            salesPanel.add(createTransactionsTable(), BorderLayout.CENTER);
            salesPanel.setBackground(DARK_BLUE);
            salesPanel.revalidate();
            salesPanel.repaint();
            adminTabs.addTab("Transactions & Sales", salesPanel);
            
            // Edit Prices (updated)
            priceEditPanel.removeAll();
            priceEditPanel.add(createMovieEditPanel());
            priceEditPanel.add(createFoodEditPanel());
            priceEditPanel.setBackground(DARK_BLUE);
            priceEditPanel.revalidate();
            priceEditPanel.repaint();
            adminTabs.addTab("Edit Prices", priceEditPanel);
        }

        private JPanel createRoomStatusPanel() {
            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10)); // Vertical layout for medium boxes
            panel.setBackground(DARK_BLUE);
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            for (Room room : dc.getRooms()) {
                JPanel roomBox = new JPanel(new BorderLayout());
                roomBox.setBackground(LIGHT_BLUE);
                roomBox.setBorder(new LineBorder(TEXT_ACCENT, 2));
                roomBox.setPreferredSize(new Dimension(300, 80)); // Medium size
                
                JLabel infoLabel = new JLabel("Room " + room.getRoomNumber() + " (" + room.getCapacity() + ") - " + (room.isOccupied() ? "OCCUPIED" : "AVAILABLE"));
                infoLabel.setForeground(TEXT_LIGHT);
                
                JButton availableButton = new JButton("Set Available");
                availableButton.setBackground(new Color(60, 179, 113));
                availableButton.setForeground(TEXT_LIGHT);
                availableButton.addActionListener(e -> {
                    room.setOccupied(false);
                    JOptionPane.showMessageDialog(panel, "Room " + room.getRoomNumber() + " set to AVAILABLE.");
                    refreshData(); // Refresh to update labels
                });
                
                roomBox.add(infoLabel, BorderLayout.CENTER);
                roomBox.add(availableButton, BorderLayout.EAST);
                panel.add(roomBox);
            }
            
            return panel;
        }

        private JPanel createSalesSummaryPanel() {
            JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            summaryPanel.setBackground(LIGHT_BLUE);
            summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_ACCENT), 
                "Sales Summary", 
                javax.swing.border.TitledBorder.LEFT, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14), 
                TEXT_ACCENT));
            
            double totalSales = dc.getTransactions().stream().mapToDouble(Transaction::getTotalPrice).sum();
            double foodSales = dc.getTransactions().stream()
                .flatMap(t -> t.getSelectedFood().stream())
                .mapToDouble(FoodItem::getPrice).sum();
            double movieSales = totalSales - foodSales;

            JLabel totalLabel = new JLabel(String.format("Total Sales: PHP %.2f", totalSales), SwingConstants.CENTER);
            totalLabel.setForeground(TEXT_LIGHT);
            JLabel movieLabel = new JLabel(String.format("Movie Sales: PHP %.2f", movieSales), SwingConstants.CENTER);
            movieLabel.setForeground(TEXT_LIGHT);
            JLabel foodLabel = new JLabel(String.format("Food Sales: PHP %.2f", foodSales), SwingConstants.CENTER);
            foodLabel.setForeground(TEXT_LIGHT);

            summaryPanel.add(totalLabel);
                        summaryPanel.add(movieLabel);
            summaryPanel.add(foodLabel);
            return summaryPanel;
        }

        private JScrollPane createTransactionsTable() {
            String[] columnNames = {"ID", "Date", "Movie", "Room", "Food Items", "Total"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            
            for (Transaction t : dc.getTransactions()) {
                String foodList = t.getSelectedFood().stream()
                                   .map(FoodItem::getName)
                                   .collect(Collectors.joining(", "));
                
                model.addRow(new Object[]{
                    t.getTransactionId(),
                    t.getTransactionDate(),
                    t.getSelectedMovie().getTitle(),
                    t.getSelectedRoom().getRoomNumber(),
                    foodList.isEmpty() ? "None" : foodList,
                    String.format("PHP %.2f", t.getTotalPrice())
                });
            }
            
            JTable transactionTable = new JTable(model);
            transactionTable.setBackground(LIGHT_BLUE.darker());
            transactionTable.setForeground(TEXT_LIGHT);
            transactionTable.getTableHeader().setBackground(DARK_BLUE.darker());
            transactionTable.getTableHeader().setForeground(TEXT_ACCENT);
            
            JScrollPane scrollPane = new JScrollPane(transactionTable);
            scrollPane.getViewport().setBackground(LIGHT_BLUE); 
            return scrollPane;
        }
        
        private JPanel createMovieEditPanel() {
            JPanel movieEditPanel = new JPanel(new BorderLayout());
            movieEditPanel.setBackground(LIGHT_BLUE);
            movieEditPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_ACCENT), 
                "Edit Movie Prices", 
                javax.swing.border.TitledBorder.LEFT, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14), 
                TEXT_ACCENT));
            
            String[] movieCols = {"Title", "Genre", "Current Price"};
            DefaultTableModel movieModel = new DefaultTableModel(movieCols, 0);
            
            for (Movie m : dc.movies) {
                movieModel.addRow(new Object[]{m.getTitle(), m.getGenre(), String.format("%.2f", m.getBasePrice())});
            }
            JTable movieTable = new JTable(movieModel);
            movieTable.setBackground(LIGHT_BLUE.darker());
            movieTable.setForeground(TEXT_LIGHT);
            JScrollPane movieScroll = new JScrollPane(movieTable);
            movieScroll.getViewport().setBackground(LIGHT_BLUE);
            
            JPanel movieControl = new JPanel(new FlowLayout());
            movieControl.setBackground(LIGHT_BLUE);
            JTextField moviePriceField = new JTextField(10);
            JButton movieApplyBtn = new JButton("Apply New Price");
            movieApplyBtn.setBackground(new Color(60, 179, 113));
            movieApplyBtn.setForeground(TEXT_LIGHT);
            
            movieApplyBtn.addActionListener(e -> {
                int row = movieTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(movieEditPanel, "Select a movie first.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    double newPrice = Double.parseDouble(moviePriceField.getText());
                    Movie selectedMovie = dc.movies.get(row);
                    selectedMovie.setBasePrice(newPrice);
                    movieModel.setValueAt(String.format("%.2f", newPrice), row, 2);
                    JOptionPane.showMessageDialog(movieEditPanel, selectedMovie.getTitle() + " price updated.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(movieEditPanel, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            // Add button to add new movie
            JButton addMovieButton = new JButton("Add New Movie");
            addMovieButton.setBackground(new Color(60, 179, 113));
            addMovieButton.setForeground(TEXT_LIGHT);
            addMovieButton.addActionListener(e -> {
                String title = JOptionPane.showInputDialog("Enter Movie Title:");
                String genre = JOptionPane.showInputDialog("Enter Genre:");
                String priceStr = JOptionPane.showInputDialog("Enter Price:");
                try {
                    double price = Double.parseDouble(priceStr);
                    Movie newMovie = new Movie(title, genre, 100); // Default duration, adjust as needed
                    newMovie.setBasePrice(price);
                    dc.movies.add(newMovie);
                    refreshData();
                    JOptionPane.showMessageDialog(this, "New movie added to " + genre + " genre.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            movieControl.add(addMovieButton);
            movieControl.add(moviePriceField);
            movieControl.add(movieApplyBtn);
            
            movieEditPanel.add(movieScroll, BorderLayout.CENTER);
            movieEditPanel.add(movieControl, BorderLayout.SOUTH);
            return movieEditPanel;
        }
        
        private JPanel createFoodEditPanel() {
            JPanel foodEditPanel = new JPanel(new BorderLayout());
            foodEditPanel.setBackground(LIGHT_BLUE);
            foodEditPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_ACCENT), 
                "Edit Food Prices", 
                javax.swing.border.TitledBorder.LEFT, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14), 
                TEXT_ACCENT));
            
            String[] foodCols = {"Name", "Category", "Current Price"};
            DefaultTableModel foodModel = new DefaultTableModel(foodCols, 0);
            
            for (FoodItem f : dc.foodMenu) {
                foodModel.addRow(new Object[]{f.getName(), f.getCategory(), String.format("%.2f", f.getPrice())});
            }
            JTable foodTable = new JTable(foodModel);
            foodTable.setBackground(LIGHT_BLUE.darker());
            foodTable.setForeground(TEXT_LIGHT);
            JScrollPane foodScroll = new JScrollPane(foodTable);
            foodScroll.getViewport().setBackground(LIGHT_BLUE);

            JPanel foodControl = new JPanel(new FlowLayout());
            foodControl.setBackground(LIGHT_BLUE);
            JTextField foodPriceField = new JTextField(10);
            JButton foodApplyBtn = new JButton("Apply New Price");
            foodApplyBtn.setBackground(new Color(60, 179, 113));
            foodApplyBtn.setForeground(TEXT_LIGHT);

            foodApplyBtn.addActionListener(e -> {
                int row = foodTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(foodEditPanel, "Select a food item first.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    double newPrice = Double.parseDouble(foodPriceField.getText());
                    FoodItem selectedFood = dc.foodMenu.get(row);
                    selectedFood.setPrice(newPrice);
                    foodModel.setValueAt(String.format("%.2f", newPrice), row, 2);
                    JOptionPane.showMessageDialog(foodEditPanel, selectedFood.getName() + " price updated.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(foodEditPanel, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            // Add button
            JButton addFoodButton = new JButton("Add New Food");
            addFoodButton.setBackground(new Color(60, 179, 113));
            addFoodButton.setForeground(TEXT_LIGHT);
            addFoodButton.addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Enter Food Name:");
                String category = JOptionPane.showInputDialog("Enter Category:");
                String priceStr = JOptionPane.showInputDialog("Enter Price:");
                try {
                    double price = Double.parseDouble(priceStr);
                    FoodItem newFood = new FoodItem(name, price, category);
                    dc.foodMenu.add(newFood);
                    refreshData();
                    JOptionPane.showMessageDialog(this, "New food added.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            foodControl.add(addFoodButton);
            foodControl.add(foodPriceField);
            foodControl.add(foodApplyBtn);
            
            foodEditPanel.add(foodScroll, BorderLayout.CENTER);
            foodEditPanel.add(foodControl, BorderLayout.SOUTH);
            return foodEditPanel;
        }
    }


    // =================================================================
    // 4. MAIN APPLICATION FRAME AND DIALOGS
    // =================================================================

    public static class ViewCartDialog extends JDialog {
        private final OrderManager manager;
        
        public ViewCartDialog(OrderManager manager) {
    super((JFrame) null, "Your Cart", true);
    this.manager = manager;
    setLayout(new BorderLayout());
    getContentPane().setBackground(DARK_BLUE);
    setSize(800, 600); // Full-screen-ish dialog, not stretching main window
    setLocationRelativeTo(null);
    
    // Order Summary Panel
    JPanel summaryPanel = new JPanel();
    summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
    summaryPanel.setBackground(DARK_BLUE);
    summaryPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
    // Movie
    if (manager.selectedMovie != null) {
        JLabel movieLabel = new JLabel("Movie: " + manager.selectedMovie.getTitle() + " - PHP " + String.format("%.2f", manager.selectedMovie.getBasePrice()), SwingConstants.LEFT);
        movieLabel.setForeground(TEXT_LIGHT);  // Make text lighter
        summaryPanel.add(movieLabel);
        summaryPanel.add(Box.createVerticalStrut(10));
    }
    
    // Foods & Drinks
    if (!manager.foodOrder.isEmpty()) {
        JLabel foodHeaderLabel = new JLabel("Foods & Drinks:", SwingConstants.LEFT);
        foodHeaderLabel.setForeground(TEXT_LIGHT);  // Make text lighter
        summaryPanel.add(foodHeaderLabel);
        for (FoodItem item : manager.foodOrder) {
            JLabel itemLabel = new JLabel("  - " + item.getName() + " - PHP " + String.format("%.2f", item.getPrice()), SwingConstants.LEFT);
            itemLabel.setForeground(TEXT_LIGHT);  // Make text lighter
            summaryPanel.add(itemLabel);
        }
        summaryPanel.add(Box.createVerticalStrut(10));
    }
    
    // Room (no price, just type)
    if (manager.selectedRoom != null) {
        JLabel roomLabel = new JLabel("Room: " + manager.selectedRoom.getCapacity() + " (Room " + manager.selectedRoom.getRoomNumber() + ")", SwingConstants.LEFT);
        roomLabel.setForeground(TEXT_LIGHT);  // Make text lighter
        summaryPanel.add(roomLabel);
        summaryPanel.add(Box.createVerticalStrut(10));
    }
    
    add(new JScrollPane(summaryPanel), BorderLayout.CENTER);
    
    // Bottom Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.setBackground(DARK_BLUE);
    
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setBackground(Color.GRAY);
    cancelButton.setForeground(TEXT_LIGHT);
    cancelButton.addActionListener(e -> dispose());
    
    JButton reviewButton = new JButton("Review + Pay for Order");
    reviewButton.setBackground(TEXT_ACCENT);
    reviewButton.setForeground(Color.BLACK);
    reviewButton.addActionListener(e -> {
        dispose();
        new ReviewOrderDialog(manager).setVisible(true);
    });
    
    double total = manager.selectedMovie != null ? manager.selectedMovie.getBasePrice() : 0.0;
    total += manager.foodOrder.stream().mapToDouble(FoodItem::getPrice).sum();
    JLabel orderTotalLabel = new JLabel("Order Total: PHP " + String.format("%.2f", total));
    orderTotalLabel.setForeground(TEXT_LIGHT);  // Make text lighter
    orderTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
    
    buttonPanel.add(cancelButton);
    buttonPanel.add(reviewButton);
    buttonPanel.add(orderTotalLabel);
    
    add(buttonPanel, BorderLayout.SOUTH);
}    }
    
    public static class ReviewOrderDialog extends JDialog {
        private final OrderManager manager;
        
        public ReviewOrderDialog(OrderManager manager) {
    super((JFrame) null, "Review Your Order", true);
    this.manager = manager;
    setLayout(new BorderLayout());
    getContentPane().setBackground(DARK_BLUE);
    setSize(600, 400);
    setLocationRelativeTo(null);
    
    // Custom Title Label (white text for visibility)
    JLabel titleLabel = new JLabel("REVIEW YOUR ORDER", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(TEXT_LIGHT);  // White text
    titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
    
    // Footer: Review Details
    JPanel reviewPanel = new JPanel();
    reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
    reviewPanel.setBackground(DARK_BLUE);
    reviewPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    
    // Movie
    if (manager.selectedMovie != null) {
        JLabel movieLabel = new JLabel("Movie: " + manager.selectedMovie.getTitle() + " - PHP " + String.format("%.2f", manager.selectedMovie.getBasePrice()), SwingConstants.CENTER);
        movieLabel.setForeground(TEXT_LIGHT);  // White text
        reviewPanel.add(movieLabel);
    }
    
    // Foods & Drinks
    if (!manager.foodOrder.isEmpty()) {
        JLabel foodHeaderLabel = new JLabel("Foods & Drinks:", SwingConstants.CENTER);
        foodHeaderLabel.setForeground(TEXT_LIGHT);  // White text
        reviewPanel.add(foodHeaderLabel);
        for (FoodItem item : manager.foodOrder) {
            JLabel itemLabel = new JLabel("  - " + item.getName() + " - PHP " + String.format("%.2f", item.getPrice()), SwingConstants.CENTER);
            itemLabel.setForeground(TEXT_LIGHT);  // White text
            reviewPanel.add(itemLabel);
        }
    }
    
    // Room
    if (manager.selectedRoom != null) {
        JLabel roomLabel = new JLabel("Room: " + manager.selectedRoom.getCapacity() + " (Room " + manager.selectedRoom.getRoomNumber() + ")", SwingConstants.CENTER);
        roomLabel.setForeground(TEXT_LIGHT);  // White text
        reviewPanel.add(roomLabel);
    }
    
    // Add title and review panel to dialog
    add(titleLabel, BorderLayout.NORTH);
    add(reviewPanel, BorderLayout.CENTER);
    
    // Bottom: Proceed, Back, Total
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBackground(DARK_BLUE);
    
    JButton proceedButton = new JButton("PROCEED TO PAYMENT  ✔");
    proceedButton.setBackground(Color.RED);
    proceedButton.setForeground(TEXT_LIGHT);
    proceedButton.setFont(new Font("Arial", Font.BOLD, 16));
    proceedButton.addActionListener(e -> {
        dispose();
        manager.finalizeTransaction(); // Triggers receipt
    });
    
    JButton backButton = new JButton("Back");
    backButton.setBackground(Color.GRAY);
    backButton.setForeground(TEXT_LIGHT);
    backButton.addActionListener(e -> {
        dispose();
        new ViewCartDialog(manager).setVisible(true);
    });
    
    double total = manager.selectedMovie != null ? manager.selectedMovie.getBasePrice() : 0.0;
    total += manager.foodOrder.stream().mapToDouble(FoodItem::getPrice).sum();
    JLabel totalLabel = new JLabel("Order Total: PHP " + String.format("%.2f", total), SwingConstants.RIGHT);
    totalLabel.setForeground(TEXT_LIGHT);  // White text
    totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
    
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftPanel.setBackground(DARK_BLUE);
    leftPanel.add(backButton);
    
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightPanel.setBackground(DARK_BLUE);
    rightPanel.add(totalLabel);
    
    bottomPanel.add(leftPanel, BorderLayout.WEST);
    bottomPanel.add(proceedButton, BorderLayout.CENTER);
    bottomPanel.add(rightPanel, BorderLayout.EAST);
    
    add(bottomPanel, BorderLayout.SOUTH);
}
    }


    public static class KioskFrame extends JFrame {
        private final JLabel currentTotalLabel;
        private final OrderManager orderManager;
        private final JTabbedPane contentTabs;
        private final JPanel sidebarPanel;

        public KioskFrame() {
            super("CineVibe Movie House System");
            
            currentTotalLabel = new JLabel("Current Total: PHP 0.00", SwingConstants.CENTER);
            currentTotalLabel.setFont(new Font("Arial", Font.BOLD, 20));
            currentTotalLabel.setForeground(TEXT_ACCENT);
            
            contentTabs = new JTabbedPane();
            contentTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            contentTabs.setOpaque(false); 
            
            // CRITICAL FIX: Hide the JTabbedPane tabs to rely only on the sidebar navigation
            contentTabs.setUI(new BasicTabbedPaneUI() {
                @Override
                protected int calculateTabAreaHeight(int tabPlacement, int horizTextGap, int maxTabHeight) {
                    return 0; // Set height to zero to hide the tabs
                }
                @Override
                protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
                    // Do nothing to prevent painting the tab area
                }
            });


            orderManager = new OrderManager(currentTotalLabel);
            
            final TrendingMoviesPanel trendingPanel = new TrendingMoviesPanel(orderManager);
            final MoviesPanel moviesPanel = new MoviesPanel(orderManager);
            final FoodDrinksPanel foodPanel = new FoodDrinksPanel(orderManager);
            final RoomsPanel roomsPanel = new RoomsPanel(orderManager);
            final AdminPanelManager adminPanel = new AdminPanelManager();

            contentTabs.addTab("Trending", trendingPanel);
            contentTabs.addTab("Movies", moviesPanel);
            contentTabs.addTab("Food & Drinks", foodPanel);
            contentTabs.addTab("Rooms", roomsPanel);
            contentTabs.addTab("Admin Panel", adminPanel);
            
            sidebarPanel = createSidebar(contentTabs);
            
            JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
            controlPanel.setBackground(DARK_BLUE);
            controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            // Left: "Your order" label
            JLabel yourOrderLabel = new JLabel("Your order", SwingConstants.LEFT);
            yourOrderLabel.setFont(new Font("Arial", Font.BOLD, 16));
            yourOrderLabel.setForeground(TEXT_LIGHT);
            
            // Right: "View Cart" button
            JButton viewCartButton = new JButton("View Cart");
            viewCartButton.setFont(new Font("Arial", Font.BOLD, 16));
            viewCartButton.setBackground(TEXT_ACCENT);
            viewCartButton.setForeground(Color.BLACK);
            viewCartButton.addActionListener(e -> new ViewCartDialog(orderManager).setVisible(true));
            
            JPanel topControl = new JPanel(new BorderLayout());
            topControl.setBackground(DARK_BLUE);
            topControl.add(yourOrderLabel, BorderLayout.WEST);
            topControl.add(viewCartButton, BorderLayout.EAST);
            
            controlPanel.add(topControl, BorderLayout.CENTER);
            
            setLayout(new BorderLayout());
            add(sidebarPanel, BorderLayout.WEST);
            add(contentTabs, BorderLayout.CENTER);
            add(controlPanel, BorderLayout.SOUTH);
            
            // Set the initial view to Trending Movies
            contentTabs.setSelectedIndex(0); 

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 800); 
            getContentPane().setBackground(DARK_BLUE);
            setLocationRelativeTo(null); 
        }

        private JPanel createSidebar(JTabbedPane tabs) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(DARK_BLUE.darker());
            panel.setPreferredSize(new Dimension(200, 800));
            panel.setBorder(new LineBorder(LIGHT_BLUE, 1, true));

            JLabel title = new JLabel("CineVibe Movie House");
            title.setFont(new Font("Arial", Font.BOLD, 16));
            title.setForeground(TEXT_ACCENT);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(20));
            panel.add(title);
            panel.add(Box.createVerticalStrut(30));

            String[] buttonNames = {"Trending Movies", "Movies", "Food & Drinks", "Rooms", "Admin Panel"};
            
            for (int i = 0; i < buttonNames.length; i++) {
                JButton button = new JButton(buttonNames[i]);
                button.setMaximumSize(new Dimension(180, 50));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setBackground(LIGHT_BLUE);
                button.setForeground(TEXT_LIGHT);
                button.setFont(new Font("Arial", Font.BOLD, 14));
                button.setBorder(new EmptyBorder(10, 20, 10, 20));
                
                final int index = i;
                button.addActionListener(e -> tabs.setSelectedIndex(index));
                
                panel.add(button);
                panel.add(Box.createVerticalStrut(10));
            }
            
            panel.add(Box.createVerticalGlue()); 
            
            return panel;
        }
    }


    // =================================================================
    // 5. MAIN METHOD (Entry Point)
    // =================================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new KioskFrame().setVisible(true);
        });
    }
}