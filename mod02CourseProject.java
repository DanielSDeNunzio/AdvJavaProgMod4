package mod02CourseProject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.sql.*;
public class mod02CourseProject {
	//Main
	public static void main (String[] args) {

		try {
			String urlConn = "jdbc:mysql://localhost:3306/advjavaprog";
			Connection connection = DriverManager.getConnection(urlConn, "root", "Longbow9!");
			System.out.println("Connection Successful");
			
			PreparedStatement sql = connection.prepareStatement("Select * from items");
			ResultSet rs = sql.executeQuery();
			while(rs.next()) {
				int tempint = rs.getInt("itemid");
				String temp = rs.getString("itemname");
				int tempstock = rs.getInt("stock");
				double tempprice = rs.getDouble("price");
				System.out.println(tempint + " " + temp + " " + tempstock + " " + tempprice);
			}
			
			myLogger logger = new myLogger();
			int I1 = 0;
			Scanner userin = new Scanner(System.in);
			
			
		
			//introductory statement
			do {
				System.out.println("Welcome to our inventory management program, for a list of options, press 1, to exit, press 2: "
						+ "\n");				 
				I1 = userin.nextInt();
				}while(I1 != 1 && I1 != 2);
			//do while loop to loop the menu
			do {
				if(I1 == 1)
				{
					//call menu and get users choice
					printMenu();
					int I2 = userin.nextInt();
					userin.nextLine();
					//input users choice into the choose menu function
					chooseMenu(I2, userin, logger, connection);
				}
			}while(I1 != 2);
			System.exit(I1);
			
		}catch(Exception e) {
			System.out.println("Error Connecting to the db: " + e.getMessage());
		}
		
	}
	
	//function used to display the menu of choices
	public static void printMenu()
	{
		System.out.println("Enter the number corresponding to what you would like to do: \n");
		System.out.println("1.Buy item \n2.Sell item \n3.Add item \n4.Remove item \n5.Display inventory \n6.Search \n7.Exit");
	}
	
	//function used to select from the menu of choices
	public static void chooseMenu(int choice, Scanner userin, myLogger logger, Connection connection)
	{
		if (choice == 1)
		{
			 logger.log(Level.INFO, "Buying item");
			buyItem(userin, connection);
		}
		else if (choice == 2)
		{
			logger.log(Level.INFO, "Selling item");
			sellItem(userin, logger, connection);
		}
		else if (choice == 3)
		{
			logger.log(Level.INFO, "Adding item");
			addItem(userin, connection);
		}
		else if (choice == 4)
		{
			logger.log(Level.INFO, "Removing item");
			removeItem(userin, connection);
		}
		else if (choice == 5)
		{
			logger.log(Level.INFO, "Viewing items");
			fullInventory(connection);
		}
		else if (choice == 6)
		{
			find(userin, connection);
		}
		else if (choice == 7)
		{
			System.exit(choice);
		}
		else 
		{
			System.out.println("Invalid option.");
			printMenu();
		}
	}
	
	//function used to buy an item and display the cost of the item or items the person intends on purchasing
	public static void buyItem(Scanner userin, Connection connection)
	{
		System.out.println("Please enter the name of the item and how many you would like to buy: ");
		String name = "SELECT * FROM items WHERE itemname= ?";
		String trial = userin.nextLine();
		int itemsname = userin.nextInt();
		int temporary1 = 0;
		try {
			PreparedStatement sql = connection.prepareStatement(name);
			sql.setString(1, trial);
			ResultSet rs = sql.executeQuery();
			temporary1 = rs.getInt("stock");
			rs.updateInt("stock", temporary1 + itemsname );
			}catch(Exception e) {
				System.out.println("Couldn't find item, sorry!");
			}
		
		
	}
	
	
	//function used to sell an item and display how much we would make.
	public static void sellItem(Scanner userin, myLogger logger, Connection connection)
	{
		int found = 0;
		System.out.println("Please enter the name of the item you would like to sell: ");
		String name = userin.nextLine();
		try {
			PreparedStatement sql = connection.prepareStatement("Select * from items Where itemname=" + name);
			ResultSet rs = sql.executeQuery();
			while(rs.next()) {
				int tempint = rs.getInt("itemid");
				String temp = rs.getString("itemname");
				int tempstock = rs.getInt("stock");
				double tempprice = rs.getDouble("price");
				System.out.println(tempint + " " + temp + " " + tempstock + " " + tempprice);
				found = 1;
			}
			}catch(Exception e) {
				System.out.println("Couldn't find item, sorry!");
			}
		if(found == 1) {
			System.out.println("How many would you like to sell?");
			int num = userin.nextInt();
			userin.nextLine();
			try {
				PreparedStatement sql = connection.prepareStatement("Update items Set stock = (stock + " + num + " from items Where itemname=" + name);
				ResultSet rs = sql.executeQuery();
			}catch(Exception e) {
				System.out.println("unable to alter stock");
			}
		}
		
	}
	
	//base function used to add an item to the inventory list (uses other functions to add items to the list, will have to be
	//changed to fit database later on).
	public static void addItem(Scanner userin, Connection connection)
	{
		System.out.println("Please enter the item number, item name, number in stock, and the price");
		int temp5 = userin.nextInt();
		userin.nextLine();
		String temp2 = userin.nextLine();
		int temp3 = userin.nextInt();
		userin.nextLine();
		double temp4 = userin.nextDouble();
		try {
			PreparedStatement sql = connection.prepareStatement("Insert into items (itemid, itemname, stock, price) Values (" + temp5 + ", " + temp2 + ", " + temp3 + ", " + temp4);
			ResultSet rs = sql.executeQuery();
			while(rs.next()) {
				int tempint = rs.getInt("itemid");
				String temp = rs.getString("itemname");
				int tempstock = rs.getInt("stock");
				double tempprice = rs.getDouble("price");
				System.out.println(tempint + " " + temp + " " + tempstock + " " + tempprice);

			}
			}catch(Exception e) {
				System.out.println("Couldn't find item, sorry!");
			}
		
	}
	
	//base function used to remove an item from our inventory list (will have to be altered to work with database later on
	public static void removeItem(Scanner userin, Connection connection)
	{
		System.out.println("Please enter the name of the item you would like to remove: ");
		String item = userin.nextLine();
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM items WHERE itemname = ?");
			ps.setString(1, item);
			ps.executeQuery();
		}catch(Exception e) {
			System.out.println("Couldn't remove item");
		}
		
	}
	//function used to display the index, item name, number in stock, and price of all items in inventory
	public static void fullInventory(Connection connection)
	{
		String query = "Select * from items";
		try {
		PreparedStatement sql = connection.prepareStatement(query);
		ResultSet rs = sql.executeQuery();
		while(rs.next()) {
			int tempint = rs.getInt("itemid");
			String temp = rs.getString("itemname");
			int tempstock = rs.getInt("stock");
			double tempprice = rs.getDouble("price");
			System.out.println(tempint + " " + temp + " " + tempstock + " " + tempprice);
		}
		}catch(Exception e) {
			System.out.println("unable to review inventory");
		}
		
	}
	
	public static void find(Scanner userin, Connection connection)
	{
		System.out.println("Please enter the name of the item you would like to find: ");
		String name = userin.nextLine();
		String query = "Select * from items Where itemname = ?";
		try {
		PreparedStatement sql = connection.prepareStatement(query);
		sql.setString(0, name );
		ResultSet rs = sql.executeQuery();
		while(rs.next()) {
			int tempint = rs.getInt("itemid");
			String temp = rs.getString("itemname");
			int tempstock = rs.getInt("stock");
			double tempprice = rs.getDouble("price");
			System.out.println(tempint + " " + temp + " " + tempstock + " " + tempprice);
		}
		}catch(Exception e) {
			System.out.println("unable to find item");
		}
	}
	
	
}