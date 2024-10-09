package project;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Customer{
	
	private int amount;
	
	private final Lock lock =new ReentrantLock();
	private final Condition sufficentFunds=lock.newCondition();
	
	public Customer(int amount) {//constructor
		
		this.amount=amount;
	}
	
	//withdrawal Method==========================================
	
	public void withdrawal(int amount){
		
		lock.lock();
		
		try {
			while(this.amount < amount) {
				System.err.println("Sorry, you don't have sufficient balance."+
			 "Your current balance is :"+this.amount);
				sufficentFunds.await();
			}
			
			this.amount-=amount;
			System.out.println("Withdrawal completed. Remaining balance: " + this.amount);
			
		 } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
		 }
		
		finally {
			lock.unlock();
		}
	}
	
	//Deposite 	Method=====================================
	
	public void deposite(int amount) {
		lock.lock();
		 
		try {
			
			this.amount+=amount;
			
			System.out.println("Amount deposited successfully! Current balance: " + this.amount);
			
          sufficentFunds.signalAll(); // Notify all waiting threads
        } 
		finally {
            lock.unlock();
        }
		
	}
	}

public class AtmMachine {

	public static void main(String[] args) {
		
//		Scanner s=new Scanner(System.in);
		
		Customer customer=new Customer(10000);
		
		new Thread (){public void run() {customer.deposite(20000);} }.start();
		
		new Thread() {public void run() {customer.withdrawal(25000);}}.start();
	
		new Thread (){public void run() {customer.deposite(5000);} }.start();
		
		new Thread() {public void run() {customer.withdrawal(25000);}}.start();
		
		new Thread (){public void run() {customer.deposite(50000);} }.start();
		
		//new way
		 new Thread(() -> customer.deposite(20000)).start();
	}

}
