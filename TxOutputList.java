import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

/**
 * TxOutputList defines a list of outputs of a transaction
 * 
 */

public class TxOutputList {

    /**
     * list of txOutputs for a transaction
     */

    private ArrayList<TxOutput> txOutputList;

    /**
     * add entry given by sender and amount to the list
     */

    public void addEntry(PublicKey sender, int amount) {
        txOutputList.add(new TxOutput(sender, amount));
    }

    /**
     * constructor constructing the empty TxOutputList
     */

    public TxOutputList() {
        txOutputList = new ArrayList<TxOutput>();
    }

    /**
     * constructor constructing a list containing one entry consisting of a sender
     * and an amount
     */

    public TxOutputList(PublicKey sender, int amount) {
        txOutputList = new ArrayList<TxOutput>();
        addEntry(sender, amount);
    }

    /**
     * constructor constructing a list containing two entries each consisting of a
     * sender and an amount
     */

    public TxOutputList(PublicKey sender1, int amount1, PublicKey sender2, int amount2) {
        txOutputList = new ArrayList<TxOutput>();
        addEntry(sender1, amount1);
        addEntry(sender2, amount2);
    }

    /**
     * constructor constructing a list containing three entries each consisting of a
     * sender and an amount
     */

    public TxOutputList(PublicKey sender1, int amount1, PublicKey sender2, int amount2, PublicKey sender3,
            int amount3) {
        txOutputList = new ArrayList<TxOutput>();
        addEntry(sender1, amount1);
        addEntry(sender2, amount2);
        addEntry(sender3, amount3);
    }

    /**
     * obtain the underlying list
     */

    public ArrayList<TxOutput> toList() {
        return (txOutputList);
    };

    /**
     * compute the sum of entries in the list
     */

    public int toSum() {
        int result = 0;
        for (TxOutput entry : toList()) {
            result += entry.getAmount();
        }
        ;
        return result;
    };

    /**
     *
     * the operation toAccountBalance is defined similarly as for TxInputList.
     * 
     * We don't need it really since it is only used for checking in case of
     * txInputs that they can be deducted
     *
     * We still keep it since it may be of use in the future.
     *
     */

    public AccountBalance toAccountBalance() {
        AccountBalance result = new AccountBalance();
        for (TxOutput entry : toList()) {
            result.addToBalance(entry.getRecipient(), entry.getAmount());
        }
        ;
        return result;

    }

    /**
     * Create the message to be signed, if the outpupt is the current TxOutputList
     * and the sender and amount are the inputs
     *
     * Note that the sender signs his input and all outputs so other inputs of the
     * transaction are not included in the message to sign
     *
     */

    public byte[] getMessageToSign(PublicKey sender, int amount) {
        SigData sigData = new SigData();
        sigData.addPublicKey(sender);
        sigData.addInteger(amount);
        for (TxOutput txOutput : toList()) {
            sigData.addPublicKey(txOutput.getRecipient());
            sigData.addInteger(txOutput.getAmount());
        }
        return sigData.toArray();
    }

    /**
     * function to print all items in the TxOutputList in the form word1 <recipient>
     * word2 <amount> % we use the pubKeyMap in order to look up the names of the
     * recipients
     */

    public void print(String word1, String word2, PublicKeyMap pubKeyMap) {
        for (TxOutput entry : txOutputList) {
            entry.print(word1, word2, pubKeyMap);
        }
    }

    /**
     * Default way of printing out the TxOutputList
     */

    public void print(PublicKeyMap pubKeyMap) {
        print("Recipient: ", " value:  ", pubKeyMap);
    }

    /**
     * Generic Test cases, providing a headline printing out the TxOutputList and
     * printing out the sum of amounts in the TxOutputList
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
        PublicKey pubKeyA = pubKeyMap.getPublicKey("Alice");
        PublicKey pubKeyB = pubKeyMap.getPublicKey("Bob");
        (new TxOutputList(pubKeyA, 10)).testCase("Test Alice 10", pubKeyMap);

        (new TxOutputList(pubKeyB, 20)).testCase("Test Bob 20", pubKeyMap);

        (new TxOutputList(pubKeyA, 10, pubKeyA, 10)).testCase("Alice twice 10", pubKeyMap);

        TxOutputList l = new TxOutputList(pubKeyA, 10, pubKeyB, 20);
        l.testCase("Test Alice 10 and Bob  20", pubKeyMap);

        System.out.println("Same List but with words User and spends");
        l.print("User ", " spends ", pubKeyMap);

    }

    /**
     * main function running test cases
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        TxOutputList.test();
    }

};