import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

/**
 * AccountBalance defines an accountBalance in the ledger model of bitcoins
 */

public class AccountBalance {

    /**
     * The current accountBalance, with each account's public Key mapped to its
     * account balance.
     */

    private Hashtable<PublicKey, Integer> accountBalanceBase;

    /**
     * In order to print out the accountBalance in a good order we maintain a list
     * of public Keys, which will be the set of public keys maped by it in the order
     * they were added
     **/

    private ArrayList<PublicKey> publicKeyList;

    /**
     * Creates a new accountBalance
     */
    public AccountBalance() {
        accountBalanceBase = new Hashtable<PublicKey, Integer>();
        publicKeyList = new ArrayList<PublicKey>();

    }

    /**
     * Creates a new accountBalance from a map from string to integers
     */

    public AccountBalance(Hashtable<PublicKey, Integer> accountBalanceBase) {
        this.accountBalanceBase = accountBalanceBase;
        publicKeyList = new ArrayList<PublicKey>();
        for (PublicKey pbk : accountBalanceBase.keySet()) {
            publicKeyList.add(pbk);
        }
    }

    /**
     * obtain the underlying Hashtable from string to integers
     */

    public Hashtable<PublicKey, Integer> getAccountBalanceBase() {
        return accountBalanceBase;
    };

    /**
     * obtain the list of publicKeys in the tree map
     */

    public Set<PublicKey> getPublicKeys() {
        return getAccountBalanceBase().keySet();
    };

    /**
     * obtain the list of publicKeys in the order they were added
     */

    public ArrayList<PublicKey> getPublicKeysOrdered() {
        return publicKeyList;
    };

    /**
     * Adds a mapping from new account's name {@code publicKey} to its account
     * balance {@code balance} into the accountBalance.
     *
     * if there was an entry it is overridden.
     */

    public void addAccount(PublicKey publicKey, int balance) {
        accountBalanceBase.put(publicKey, balance);
        if (!publicKeyList.contains(publicKey)) {
            publicKeyList.add(publicKey);
        }
    }

    /**
     * @return true if the {@code publicKey} exists in the accountBalance.
     */

    public boolean hasPublicKey(PublicKey publicKey) {
        return accountBalanceBase.containsKey(publicKey);
    }

    /**
     * @return the balance for this account {@code account}
     *
     *         if there was no entry, return zero
     *
     */

    public int getBalance(PublicKey publicKey) {
        if (hasPublicKey(publicKey)) {
            return accountBalanceBase.get(publicKey);
        } else {
            return 0;
        }
    }

    /**
     * set the balance for {@code publicKey} to {@code amount}
     */

    public void setBalance(PublicKey publicKey, int amount) {
        accountBalanceBase.put(publicKey, amount);
        if (!publicKeyList.contains(publicKey)) {
            publicKeyList.add(publicKey);
        }
    };

    /**
     * Imcrements Adds amount to balance for {@code publicKey}
     * 
     * if there was no entry for {@code publicKey} add one with {@code balance}
     */

    public void addToBalance(PublicKey publicKey, int amount) {
        setBalance(publicKey, getBalance(publicKey) + amount);
    }

    /**
     * Subtracts amount from balance for {@code publicKey}
     */

    public void subtractFromBalance(PublicKey publicKey, int amount) {
        setBalance(publicKey, getBalance(publicKey) - amount);
    }

    /**
     * Check balance has at least amount for {@code publicKey}
     */
    public boolean checkBalance(PublicKey publicKey, int amount) {
        return (getBalance(publicKey) >= amount);
    }

    /*
     * checks whether an accountBalance can be deducted this is an auxiliary
     * function used to define checkTxInputListCanBeDeducted
     */

    public boolean checkAccountBalanceCanBeDeducted(AccountBalance accountBalance2) {
        for (PublicKey publicKey : accountBalance2.getPublicKeys()) {
            if (getBalance(publicKey) < accountBalance2.getBalance(publicKey))
                return false;
        }
        ;
        return true;
    };

    /**
     * Check that a list of publicKey amounts can be deducted from the current
     * accountBalance
     *
     * done by first converting the list of publicKey amounts into an accountBalance
     * and then checking that the resulting accountBalance can be deducted.
     * 
     */

    public boolean checkTxInputListCanBeDeducted(TxInputList txInputList) {
        return checkAccountBalanceCanBeDeducted(txInputList.toAccountBalance());
    };

    /**
     * Subtract a list of TxInput from the accountBalance
     *
     * requires that the list to be deducted is deductable.
     * 
     */

    public void subtractTxInputList(TxInputList txInputList) {
        for (TxInput entry : txInputList.toList()) {
            subtractFromBalance(entry.getSender(), entry.getAmount());
        }
    }

    /**
     * Adds a list of txOutput of a transaction to the current accountBalance
     *
     */

    public void addTxOutputList(TxOutputList txOutputList) {
        for (TxOutput entry : txOutputList.toList()) {
            addToBalance(entry.getRecipient(), entry.getAmount());
        }
    }

    /**
     *
     * Task 4 Check a transaction is valid.
     *
     * this means that the sum of outputs is less than or equal the sum of inputs
     * all signatures are valid and the inputs can be deducted from the
     * accountBalance. This method has been set to true so that the code compiles -
     * that should be changed
     */

    public boolean checkTransactionValid(Transaction tx) {
        return true;
        /*
         * this is not the correct value, only used here so that the code compiles
         */
    };

    /**
     * Process a transaction by first deducting all the inputs and then adding all
     * the outputs.
     *
     */

    public void processTransaction(Transaction tx) {
        subtractTxInputList(tx.toTxInputs());
        addTxOutputList(tx.toTxOutputs());
    };

    /**
     * Prints the current state of the accountBalance.
     */

    public void print(PublicKeyMap pubKeyMap) {
        for (PublicKey publicKey : publicKeyList) {
            Integer value = getBalance(publicKey);
            System.out.println("The balance for " + pubKeyMap.getUser(publicKey) + " is " + value);
        }

    }

    /**
     * Testcase
     */

    public static void test() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        Wallet exampleWallet = SampleWallet.generate(new String[] { "Alice" });
        byte[] exampleMessage = KeyUtils.integer2ByteArray(1);
        byte[] exampleSignature = exampleWallet.signMessage(exampleMessage, "Alice");

        /***
         * Task 5 add to the test case the test as described in the lab sheet
         * 
         * you can use the above exampleSignature, when a sample signature is needed
         * which cannot be computed from the data.
         **/

    }

    /**
     * main function running test cases
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        AccountBalance.test();
    }
}