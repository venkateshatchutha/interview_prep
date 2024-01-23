package org.example.deadlock;

public class BankAccount {
    int account;
    int amount;
    public BankAccount(int account, int amount){
        this.account=account;
        this.amount=amount;
    }

    public void deposit(int amount){
        this.amount+=amount;
    }

    public void withdraw(int amount){
        this.amount-=amount;
    }

    public synchronized void transferFunds(BankAccount toAccount, int amount) throws InterruptedException {
        synchronized (this){
            System.out.println(" deducting money from account "+this.account+" money "+amount);
            this.withdraw(amount);
            //Thread.sleep(1);
            synchronized (toAccount ){
                System.out.println(" adding money from account "+toAccount.account+" money "+amount);
                toAccount.deposit(amount);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        BankAccount a1 = new BankAccount(1,0);
        BankAccount a2 = new BankAccount(2,2000);
        Thread t1 = new Thread(()-> {
            System.out.println("in thread 1");
            try {
                a1.transferFunds(a2,350);
                System.out.println("a1, a2 balances "+a1.amount +" "+a2.amount);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(()-> {
            try {
                a2.transferFunds(a1,600);
                System.out.println("a1, a2 balances "+a1.amount +" "+a2.amount);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        //Thread.sleep(10);
        t2.start();
    }


}
