import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

/**
 * TxInputList defines a list of TxInputs of a transaction
 *
 */

public class TxInputList {

    /**
     * underlying list of txInputs
     */

    private ArrayList<TxInput> txInputList;

    /**
     * add an TxInput to the list
     */

    public void addEntry(TxInput txInput) {
        txInputList.add(txInput);
    }

    /**
     * add a txInput given by sender amount and signature
     */

    public void addEntry(PublicKey sender, int amount, byte[] signature) {
        txInputList.add(new TxInput(sender, amount, signature));
    }

    /**
     * add an entry given by sender as a string, amount, with signature created
     * using txOutputList, wallet
     */

    public void addEntry(String sender, int amount, TxOutputList txOutputList, Wallet wallet)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        txInputList.add(new TxInput(sender, amount, txOutputList, wallet));
    }

    /**
     * add an entry given by sender as a publicKey, amount, with signature created
     * using txOutputList, wallet
     */

    public void addEntry(PublicKey sender, int amount, TxOutputList txOutputList, Wallet wallet)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        txInputList.add(new TxInput(sender, amount, txOutputList, wallet));
    }

    /**
     * constructor constructing the empty TxInputList
     */

    public TxInputList() {
        txInputList = new ArrayList<TxInput>();
    }

    /**
     * constructor constructing a list containing one entry consisting of a sender,
     * an amount, and a signature
     */

    public TxInputList(PublicKey sender, int amount, byte[] signature) {
        txInputList = new ArrayList<TxInput>();
        addEntry(sender, amount, signature);
    }

    /**
     * If we have a Wallet covering the sender and an txOutputList then we can
     * compute the signature by signing the transaction to be signed consisting of
     * the public key and input amount and the txOutputList using the private key of
     * the sender
     */

    public TxInputList(String sender, int amount, TxOutputList txOutputList, Wallet wallet)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        txInputList = new ArrayList<TxInput>();
        addEntry(sender, amount, txOutputList, wallet);
    }

    /*
     * as before but referring to the sender by the public key rather than the
     * string
     */

    public TxInputList(PublicKey sender, int amount, TxOutputList txOutputList, Wallet wallet)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        txInputList = new ArrayList<TxInput>();
        addEntry(sender, amount, txOutputList, wallet);
    }

    /**
     * constructor constructing a list containing two entries each consisting of a
     * sender, an amount, and a signature note that it is not verifiedthat the
     * signatures are correct
     */

    public TxInputList(PublicKey sender1, int amount1, byte[] signature1, PublicKey sender2, int amount2,
            byte[] signature2) {
        txInputList = new ArrayList<TxInput>();
        addEntry(sender1, amount1, signature1);
        addEntry(sender2, amount2, signature2);
    }

    /** as before but for 2 users, using txOutputList and wallet **/

    public TxInputList(String sender1, int amount1, String sender2, int amount2, TxOutputList txOutputList,
            Wallet wallet) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        txInputList = new ArrayList<TxInput>();
        addEntry(new TxInput(sender1, amount1, txOutputList, wallet));
        addEntry(new TxInput(sender2, amount2, txOutputList, wallet));
    }

    /**
     * as before but for 2 users using txOutputList and wallet, this time senders
     * are given by public keys
     **/

    public TxInputList(PublicKey sender1, int amount1, PublicKey sender2, int amount2, TxOutputList txOutputList,
            Wallet wallet) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        txInputList = new ArrayList<TxInput>();
        addEntry(new TxInput(sender1, amount1, txOutputList, wallet));
        addEntry(new TxInput(sender2, amount2, txOutputList, wallet));
    }

    /**
     * obtain the underlying list
     */

    public ArrayList<TxInput> toList() {
        return (txInputList);
    };

    /* obtain the number of entries */

    public int size() {
        return (toList().size());
    }

    /* get one entry by its index */

    public TxInput get(int index) {
        return (toList()).get(index);
    }

    /**
     * compute the sum of entries in the list
     */

    public int toSum() {
        int result = 0;
        for (TxInput entry : toList()) {
            result += entry.getAmount();
        }
        ;
        return result;
    };

    /**
     * when checking that TxInputList can be deducted from an accountBalance it is
     * not enough to check that each single item can be deducted since for the same
     * sender several items might occur
     * 
     * in order to check that the TxInputList can be deducted we first create an
     * accountBalance containing for each user the sum of amounts to be deducted
     *
     * then we can check whether each entry in the original accountBalance is
     * greater the sum of items for each user to be deducted
     */

    public AccountBalance toAccountBalance() {
        AccountBalance result = new AccountBalance();
        for (TxInput entry : toList()) {
            result.addToBalance(entry.getSender(), entry.getAmount());
        }
        ;
        return result;
    }

    /**
     * function to print all items in the TxInputList in the form word1 <sender>
     * word2 <amount>
     *
     * we use the pubKeyMap in order to look up the names for each public key
     */

    public void print(String word1, String word2, PublicKeyMap pubKeyMap) {
        for (TxInput entry : txInputList) {
            entry.print(word1, word2, pubKeyMap);
        }
    }

    /**
     * Task 2 Check the signatures of each element of the TxInputList is correct
     * w.r.t. the TxOutputList given You can make use of the checkSignature method
     * of individual txinputs (elements of TxInput). In order for the code to
     * compile it has been defined as True but that should be adapted.
     **/

    public boolean checkSignature(TxOutputList txol) {
        return true;
        /*
         * this is not the correct value, only used here so that the code compiles
         */
    }

    /**
     * Default way of printing out the TxInputList
     */

    public void print(PublicKeyMap pubKeyMap) {
        print("Sender: ", " value:  ", pubKeyMap);
    }

    /**
     * Generic Test cases, providing a headline printing out the TxInputList and
     * printing out the sum of amounts in the TxInputList
     */

    public void testCase(String header, PublicKeyMap pubKeyMap) {
        System.out.println(header);
        print(pubKeyMap);
        System.out.println("Sum of Amounts = " + toSum());
        System.out.println();
    };

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
        (new TxInputList(pubKeyA, 10, signedMessage1)).testCase("Test Alice 10", pubKeyMap);

        (new TxInputList(pubKeyB, 20, signedMessage1)).testCase("Test Bob 20", pubKeyMap);

        (new TxInputList(pubKeyA, 10, signedMessage1, pubKeyA, 10, signedMessage1)).testCase("Alice twice 10",
                pubKeyMap);

        TxInputList l = new TxInputList(pubKeyA, 10, signedMessage1, pubKeyB, 20, signedMessage1);
        l.testCase("Test Alice 10 and Bob  20", pubKeyMap);

        System.out.println("Same List but with words Sender and sends");
        l.print("Sender ", " sends ", pubKeyMap);

    }

    /**
     * main function running test cases
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        TxInputList.test();
    }

};