import java.util.ArrayList;

/** TxEntryList
 *  defines a list of TxEntrys
 *  can be used as the list of inputs or list of outputs of a 
 *  transaction
 */

public class TxEntryList{


    /** 
      * the underlying list of user amounts
      */
    
    private ArrayList<TxEntry> txEntryList;

    /** 
      * add a user and an amount to the list
      */
    
    public void addEntry(String user, int amount){
	txEntryList.add(new TxEntry(user,amount));
    }

    /** 
      * constructor for the empty TxEntryList
      */
    
    public TxEntryList(){
	txEntryList =  new ArrayList<TxEntry>();
    }

    /** 
      * constructor for the TxEntryList containing one entry
        consisting of a user and an amount
      */    
    
    public TxEntryList(String user,int amount){
	txEntryList = new ArrayList<TxEntry>();
	addEntry(user,amount);
    }


    /** 
      * constructor for the TxEntryList containing two entries
        each consisting of a user and an amount
      */        

    public TxEntryList(String user1,int amount1,String user2,int amount2)
    {
	txEntryList = new ArrayList<TxEntry>();
	addEntry(user1,amount1);
	addEntry(user2,amount2);	
    }


    /** 
      * obtain the underlying list
      */
    
    public ArrayList<TxEntry> toList(){
	return(txEntryList);
    };


    /** 
      * compute the sum of entries in the list
      */

    public int toSum(){
	int result = 0;
	for (TxEntry  entry : toList()){
	    result += entry.getAmount();
		};
	return result;
    };	


    /** 
      * when checking that a TxEntryList can be deducted
      *   from  an AccountBalance
      *   it is not enough to check that each single item can be deducted
      *   since for the same user several items might occur
      *   
      *   in order to check that the user amount list can be deducted
      *    we first create an AccountBalance which determines for each user the
      *    sum of amounts to be deducted
      *
      *   then we can check whether each entry in the original accountBalance is
      *     greater the sum of items for each user to be deducted
      */
    
    public AccountBalance toAccountBalance(){
	AccountBalance result = new AccountBalance();
	for (TxEntry  entry : toList()){
	    result.addBalance(entry.getUser(),entry.getAmount());
	};
	return result;
	
    }


    /**   function  to print all items in the User Mmaount List
     *    in the form 
     *      word1  <user> word2 <amount>  
     */
    
    public void print(String word1, String word2) {
	for (TxEntry entry : txEntryList) {
	    entry.print(word1,word2);
	}
    }

    /** 
     * Default way of printing out the useramount
     */

    public void print() {
	print("User: "," value:  ");
    }

    /** 
     * Generic Test cases, providing a headline
     *    printing out the user amount list
     *    and printing out the sum of amounts in the user amount list.
     */            
    

    public void testCase(String header){
	System.out.println(header);
	print();
	System.out.println("transformed to AccountBalance:");
	toAccountBalance().print();
	System.out.println("Sum of Amounts = " + toSum());	
	System.out.println();	
    };

    /** 
     * Test cases
     */            
    
    public static void test() {
	TxEntryList l;
	(new TxEntryList("Alice",10)).testCase("Test Alice 10");

	(new TxEntryList("Bob",20)).testCase("Test Bob 20");
	
	(new TxEntryList("Alice",10,"Alice",10)).testCase("Alice twice 10");

	l = new TxEntryList("Alice",10,"Bob",20);
	l.testCase("Test Alice 10 and Bob  20");
	
	System.out.println("Same List but with words User and spends");	
	l.print("User "," spends ");		
	
    }
    

    /** 
     * main function running test cases
     */            
    
    public static void main(String[] args) {
	TxEntryList.test();
    }    

};    