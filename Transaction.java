import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Transaction consisting of a list of txInputs and a list of txOutputs
 */

public class Transaction {

    /** The list of txInputs */
    private TxInputList txInputs;

    /** The list of txOutputs */
    private TxOutputList txOutputs;

    /**
     * Creates a new transaction
     */
    public Transaction(TxInputList txInputs, TxOutputList txOutputs) {
        this.txInputs = txInputs;
        this.txOutputs = txOutputs;
    }

    /**
     * return the list of txInputs
     */

    public TxInputList toTxInputs() {
        return txInputs;
    }

    /**
     * return the list of txOutputs
     */

    public TxOutputList toTxOutputs() {
        return txOutputs;
    }

    /**
     * check the sum of inputs is >= the sum of outputs
     */

    public boolean checkTransactionAmountsValid() {
        return (toTxInputs().toSum() >= toTxOutputs().toSum());
    }

    /*
     * Task 3 check all signatures are valid In order for the code to compile it has
     * been defined as True but that should be adapted
     */

    public boolean checkSignaturesValid() {
        return true;
        /*
         * this is not the correct value, only used here so that the code compiles
         */
    }

    /**
     * print the transaction
     */

    public void print(PublicKeyMap pubKeyMap) {
        System.out.println("TxInputs:");
        toTxInputs().print("User: ", " spends ", pubKeyMap);
        System.out.println("TxOutputs:");
        toTxOutputs().print("User: ", " receives ", pubKeyMap);
    }

    /**
     * Generic Test cases, providing a headline printing out the transaction and
     * printing out whether it is valid
     */

    public void testCase(String header, PublicKeyMap pubKeyMap) {
        System.out.println(header);
        print(pubKeyMap);
        System.out.println("Is valid regarding sums = " + checkTransactionAmountsValid());
        System.out.println("");
    }

    /**
     * Test cases
     */

    public static void test() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet wallet = SampleWallet.generate(new String[] { "Alice", "Bob", "Carol", "David" });
        PublicKeyMap pubKeyMap = wallet.toPublicKeyMap();
        byte[] sampleMessage1 = KeyUtils.integer2ByteArray(1);
        byte[] sampleMessage2 = KeyUtils.integer2ByteArray(2);
        byte[] sampleMessage3 = KeyUtils.integer2ByteArray(3);
        byte[] signedMessage1 = wallet.signMessage(sampleMessage1, "Alice");
        PublicKey pubKeyA = pubKeyMap.getPublicKey("Alice");
        PublicKey pubKeyB = pubKeyMap.getPublicKey("Bob");
        PublicKey pubKeyC = pubKeyMap.getPublicKey("Carol");
        Transaction tx = new Transaction(new TxInputList(), new TxOutputList());
        tx.testCase("Transaction null to null", pubKeyMap);
        tx = new Transaction(new TxInputList(pubKeyA, 10, signedMessage1), new TxOutputList(pubKeyB, 5));
        tx.testCase("Transaction Alice 10  to Bob 5", pubKeyMap);

        tx = new Transaction(new TxInputList(pubKeyA, 5, signedMessage1), new TxOutputList(pubKeyB, 10));
        tx.testCase("Transaction Alice 5  to Bob 10", pubKeyMap);

        tx = new Transaction(new TxInputList(pubKeyA, 10, signedMessage1, pubKeyB, 5, signedMessage1),
                new TxOutputList(pubKeyA, 7, pubKeyC, 8));
        tx.testCase("Transaction Alice 10  Bob 5 to Alice 7 Carol 8", pubKeyMap);

        tx = new Transaction(new TxInputList(pubKeyA, 10, signedMessage1, pubKeyB, 5, signedMessage1),
                new TxOutputList(pubKeyA, 10, pubKeyC, 8));
        tx.testCase("Transaction Alice 10  Bob 5 to Alice 10 Carol 8", pubKeyMap);

    }

    /**
     * main function running test cases
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Transaction.test();
    }

}
