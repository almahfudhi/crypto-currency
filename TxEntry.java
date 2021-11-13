  /** TxEntry
     *  specifies a user and the amount to be transferred 
     *  will be used as one input or out output of a transaction 
     */


public class TxEntry{

    /** The amount to be transfered  */
    private int amount;

    /** The user */
    private String user;

    /** 
     * Create new TxEntry
     */

    public TxEntry(String user,int amount){
	this.amount  = amount;
	this.user = user;
    }


    /** 
     * Get the user 
     */

    public String getUser(){
	return user;
    };

    /** 
     * Get the  amount 
     */    

    public int getAmount(){
	return amount;
    }


    /** 
     * Print the entry in the form text1  <user> text2 <amount>
     */
    
    public void print(String text1, String text2) {
	System.out.println(text1 + getUser() + text2 + getAmount());
    }

    /** 
     * Default way of printing out the useramount
     */        

    public void print() {
	print("User: "," value:  ");
    }

    /** 
     * Test cases
     */            

    public static void test() {
	System.out.println();
	System.out.println("Test Alice 10");
	(new TxEntry("Alice",10)).print();
	System.out.println();
	System.out.println("Test Bob 20");
	(new TxEntry("Bob",20)).print();	
    };


    /** 
     * main function running test cases
     */            
    
    public static void main(String[] args) {
	TxEntry.test();
    }        

    
}
    
