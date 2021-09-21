package com.candor.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.candor.main.Candor;

public class Money extends MySql{

	private final String AccountsTable = "accounts";
	
	public class Account {
		
		private UUID player;
		private long balance;
		
		public Account(UUID player, long balance) {
			this.player = player;
			this.balance = balance;
		}
		
		public UUID getPlayer() {
			return player;
		}
		public void setPlayer(UUID player) {
			this.player = player;
		}
		public long getBalance() {
			return balance;
		}
		public void setBalance(long balance) {
			this.balance = balance;
		}
		public void addBalance(int amount) {
			this.balance += amount;
		}
		public void takeBalance(int amount) {
			this.balance -= amount;
		}
			
	}
	
	private List<Account> accounts;

	public Money() {
		this.accounts = new ArrayList<>();
	}
	
	@Override
	public Databases config() {
		// TODO Auto-generated method stub
		return Databases.ACCOUNTS;
	}

	@Override
	public void update() throws SQLException {
		
		for(Account account : this.accounts) {
			
			
			PreparedStatement keys = this.getConnection().prepareStatement("SELECT * FROM " + AccountsTable + " WHERE UUID=?");
			keys.setString(1, account.getPlayer().toString());
			
			ResultSet results = keys.executeQuery();
			
			if(!results.next()) {
				
				PreparedStatement insert = this.getConnection().prepareStatement("INSERT INTO " + AccountsTable + " (UUID,BALANCE) VALUES (?,?)");
				
				insert.setString(1, account.getPlayer().toString());
				insert.setDouble(2, account.getBalance());
				
				insert.executeUpdate();
				insert.close();
				
			}else {
				
				PreparedStatement insert = this.getConnection().prepareStatement("UPDATE " + AccountsTable + " SET BALANCE=? WHERE UUID=?");
				
				insert.setDouble(1, account.getBalance());
				insert.setString(2, account.getPlayer().toString());
				
				insert.executeUpdate();
				insert.close();
				
			}
			
			keys.close();
			
		}
		
		
	}

	@Override
	public void setup() {
		this.createDatabase(this.AccountsTable, "(UUID VARCHAR(37), BALANCE BIGINT(255), PRIMARY KEY(UUID))");
		
		//load database
		BukkitRunnable runnable = new BukkitRunnable() {	
			@Override
			public void run() {
				
				PreparedStatement keys;
				try {
					
					keys = getConnection().prepareStatement("SELECT UUID FROM " + AccountsTable);
					ResultSet results = keys.executeQuery();
			
					while(results.next()) {
						
						UUID result = UUID.fromString(results.getString(results.getRow()));
						
						PreparedStatement balanceQuery = getConnection().prepareStatement("SELECT BALANCE FROM " + AccountsTable + " WHERE UUID=?");
						balanceQuery.setString(1, result.toString());
						
						ResultSet balanceResult = balanceQuery.executeQuery();
						
						if(!balanceResult.next()) {
							Candor.inform("&cSomething went wrong!");
							Bukkit.getServer().shutdown();
						}
						
						long balance = balanceResult.getLong(balanceResult.getRow());
						
						Account account = new Account(result, balance);
						accounts.add(account);
						
						balanceQuery.close();
						balanceResult.close();
						
					}
					
					results.close();
					keys.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
			}
		};
		
		runnable.runTaskAsynchronously(Candor.getInstance());
		
	}

	public Account getUser(UUID uuid) {
		
		for(Account account : this.accounts) {
			if(account.getPlayer().compareTo(uuid) == 0) return account;
		}
		
		Account account = new Account(uuid, 0);
		this.accounts.add(account);
		
		return account;
	}
	
	public boolean userExists(UUID uuid) {
		for(Account account : this.accounts) {
			if(account.getPlayer().compareTo(uuid) == 0) return true;
		}
		return false;
	}
	

}
