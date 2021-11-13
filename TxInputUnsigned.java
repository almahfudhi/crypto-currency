import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.Arrays;

/**
 * TxInputUnsigned specifies one input of a transaction without the signature %
 * given by a public key, and the amount to be transferred
 * 
 */

public class TxInputUnsigned {

    /** The amount to be transfered */
    private int amount;

    /** The sender */
    private PublicKey sender;

    /**
     * Create TxInputUnsigned from sender, amount, signature
     */

    public TxInputUnsigned(PublicKey sender, int amount) {
        this.amount = amount;
        this.sender = sender;
    }

    /**
     * Get the sender
     */

    public PublicKey getSender() {
        return sender;
    };

    /*
     * get the name of the sender by looking it up in a PublicKeyMap
     */

    public String getSenderName(PublicKeyMap pubKeys) {
        return pubKeys.getUser(sender);
    };

    /**
     * Get the amount
     */

    public int getAmount() {
        return amount;
    }

    /**
     * Print the entry in the form word1 <sender> word2 <amount>
     *
     * we use pubKeyMap in order to lookup the sender's name for each public key
     */

    public void print(String word1, String word2, PublicKeyMap pubKeyMap) {
        System.out.println(word1 + getSenderName(pubKeyMap) + word2 + getAmount());
    }

    /**
     * Default way of printing out the TxInputUnsigned
     */

    public void print(PublicKeyMap pubKeyMap) {
        print("Sender: ", " Amount:  ", pubKeyMap);
    }

    /**
     * Create the message to be signed, when the input is the current
     * TxInputUnsigned for a given outputlist
     *
     * In Bitcoin the sender signs his input and all outputs so other inputs of the
     * transaction are not included in the message to sign
     *
     */

    public byte[] getMessageToSign(TxOutputList txol) {
        SigData sigData = new SigData();
        sigData.addPublicKey(sender);
        sigData.addInteger(amount);
        for (TxOutput txOutput : txol.toList()) {
            sigData.addPublicKey(txOutput.getRecipient());
            sigData.addInteger(txOutput.getAmount());
        }
        return sigData.toArray();
    }

    /*
     * Check that a signature is correct for input given by the current sender and
     * amount and a given txOutputList
     */

    public boolean checkSignature(TxOutputList txol, byte[] signature) {
        return Crypto.verifySignature(sender, getMessageToSign(txol), signature);
    }

    /**
     * Test cases
     */

    public static void test() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet wallet = SampleWallet.generate(new String[] { "Alice", "Bob", "Carol", "David" });
        PublicKeyMap pubKeyMap = wallet.toPublicKeyMap();
        PublicKey pubKeyA = pubKeyMap.getPublicKey("Alice");
        PublicKey pubKeyB = pubKeyMap.getPublicKey("Bob");

        System.out.println();
        System.out.println("Test Alice 10");
        (new TxInputUnsigned(pubKeyA, 10)).print(pubKeyMap);
        System.out.println();
        System.out.println("Test Bob 20");
        (new TxInputUnsigned(pubKeyB, 20)).print(pubKeyMap);
    };

    /**
     * main function running test cases
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        TxInputUnsigned.test();
    }

}
