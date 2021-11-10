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
	
	//function used to buy an item (for the store) and display the cost of the item the person intends on purchasing
	public static void buyItem(Scanner userin, Connection connection)
	{
		int found = 0;
		int tempstock = 0;
		System.out.println("Please enter the name of the item you would like to buy: ");
		String name = userin.nextLine();
		try {
			PreparedStatement sql = connection.prepareStatement("Select * from items Where itemname= ?");
			sql.setString(1, name);
			ResultSet rs = sql.executeQuery();
			while(rs.next()) {
				int tempint = rs.getInt("itemid");
				String temp = rs.getString("itemname");
				tempstock = rs.getInt("stock");
				double tempprice = rs.getDouble("price");
				System.out.println(tempint + " " + temp + " " + tempstock + " " + tempprice);
				found = 1;
			}
			}catch(Exception e) {
				System.out.println("Couldn't find item, sorry!");
			}
		if(found == 1) {
			System.out.println("How many would you like to buy?");
			int num = userin.nextInt();
			userin.nextLine();
			tempstock = tempstock + num;
			
				try {
					PreparedStatement sql = connection.prepareStatement("Update items Set stock = ? Where itemname= ?");
					sql.setInt(1, tempstock);
					sql.setString(2, name);
					sql.executeUpdate();
				}catch(Exception e) {
					System.out.println("unable to alter stock");
				}
		}	
	}
	
	
	//function used to sell an item (from the stores stock) and display how much we would make.
	public static void sellItem(Scanner userin, myLogger logger, Connection connection)
	{
		int found = 0;
		int tempstock = 0;
		System.out.println("Please enter the name of the item you would like to sell: ");
		String name = userin.nextLine();
		try {
			PreparedStatement sql = connection.prepareStatement("Select * from items Where itemname= ?");
			sql.setString(1, name);
			ResultSet rs = sql.executeQuery();
			while(rs.next()) {
				int tempint = rs.getInt("itemid");
				String temp = rs.getString("itemname");
				tempstock = rs.getInt("stock");
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
			tempstock = tempstock - num;
			if(tempstock >= 0)
			{
				try {
					PreparedStatement sql = connection.prepareStatement("Update items Set stock = ? Where itemname= ?");
					sql.setInt(1, tempstock);
					sql.setString(2, name);
					sql.executeUpdate();
				}catch(Exception e) {
					System.out.println("unable to alter stock");
				}
			}else {
				System.out.println("Not enough in stock to sell that many.");
			}
		}
		
	}
	
	//base function used to add an item to the inventory list
	public static void addItem(Scanner userin, Connection connection)
	{
		int index;
		String itemname;
		int stock;
		double price;
		System.out.println("Please enter the item index, item name, stock, and price:");
		index = userin.nextInt();
		userin.nextLine();
		itemname = userin.nextLine();
		stock = userin.nextInt();
		userin.nextLine();
		price = userin.nextDouble();

		String sql = "insert into items "
				+ " (itemid, itemname, stock, price)" + " values (?, ?, ?, ?)";
		try {
		PreparedStatement myStmt = connection.prepareStatement(sql);
		myStmt.setInt(1, index);
		myStmt.setString(2, itemname);
		myStmt.setInt(3, stock);
		myStmt.setDouble(4, price);
		myStmt.executeUpdate();
		
		
		}catch(Exception e) {
			System.out.println("error");
		}
		
	}
	
	//function used to remove an item from our inventory list
	public static void removeItem(Scanner userin, Connection connection)
	{
		System.out.println("Please enter the name of the item you would like to remove: ");
		String item = userin.nextLine();
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM items WHERE itemname = ?");
			ps.setString(1, item);
			ps.executeUpdate();
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
	//function used to find items with names containing user input
	public static void find(Scanner userin, Connection connection)
	{
		System.out.println("Please enter the name of the item you would like to find: ");
		String name = userin.nextLine();
		String query = "Select * from items Where itemname Like ?";
		try {
		PreparedStatement sql = connection.prepareStatement(query);
		sql.setString(1, "%" + name + "%" );
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