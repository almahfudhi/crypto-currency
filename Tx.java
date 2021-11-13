import java.util.ArrayList;
import java.util.Hashtable;

/**   Tx (Transaction)
 *    consisting of a list of transction inputs and
 *    a list of transaction outputs
 */    

public class Tx {

    /** The list of inputs  */
    private TxEntryList inputs;

    /** The list of outputs  */
    private TxEntryList outputs;    


    /**
     * Creates a new transaction 
     */ 
    public Tx(TxEntryList inputs, TxEntryList outputs){
	this.inputs = inputs;
	this.outputs= outputs;
    }

    /**
     * return the list of inputs
     */ 
    
    public TxEntryList toInputs(){
	return inputs;
    }


    /**
     * return the list of outputs
     */ 
    
    public TxEntryList toOutputs(){
	return outputs;
    }    


    /**
     * check the sum of inputs is >= the sum of outputs
     */
    
    public boolean checkTxAmountsValid (){
	return (toInputs().toSum() >= toOutputs().toSum());
    }

    /**
     * print the transaction
     */ 
    

    public void print() {
	System.out.println("Inputs:");
        toInputs().print("User: "," spends ");
	System.out.println("Outputs:");
        toOutputs().print("User: "," receives ");	
    }


    /** 
     * Generic Test cases, providing a headline
     *    printing out the transaction
     *    and printing out whether it is valid
     */            

    
    public void testCase(String header){
	System.out.println(header);
	print();
	System.out.println("Is valid regarding sums = " + checkTxAmountsValid());
	System.out.println("");
    }
	

    /** 
     * Test cases
     */            

    public static void test(){
	Tx tx;
	tx = new Tx(new TxEntryList(),
			     new TxEntryList());	
	tx.testCase("Tx null to null");
	tx = new Tx(new TxEntryList("Alice",10),
			     new TxEntryList("Bob",5));
	tx.testCase("Tx Alice 10  to Bob 5");


	tx = new Tx(new TxEntryList("Alice",5),
			     new TxEntryList("Bob",10));
	tx.testCase("Tx Alice 5  to Bob 10");
	
	tx = new Tx(new TxEntryList("Alice",10,"Bob",5),
			     new TxEntryList("Alice",7,"Carol",8));
        tx.testCase("Tx Alice 10  Bob 5 to Alice 7 Carol 8");

	tx = new Tx(new TxEntryList("Alice",10,"Bob",5),
			     new TxEntryList("Alice",10,"Carol",8));
        tx.testCase("Tx Alice 10  Bob 5 to Alice 10 Carol 8");

    }


    /** 
     * main function running test cases
     */            
    
    public static void main(String[] args) {
	Tx.test();
    System.out.print("test");
    }    
    
}