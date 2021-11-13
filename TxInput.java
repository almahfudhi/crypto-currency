import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.util.Arrays;

/**
 * TxInput specifies one input of a transaction % given by the sender given by a
 * public key, the amount to be transferred and a signature
 */

public class TxInput {

    /** The sender */
    private PublicKey sender;

    /** The amount to be transfered */
    private int amount;

    /** The signature produced to check validity */
    private byte[] signature;

    /**
     * Create TxInput from sender, amount, signature
     */

    public TxInput(PublicKey sender, int amount, byte[] signature) {
        this.amount = amount;
        this.sender = sender;
        this.signature = Arrays.copyOf(signature, signature.length);
    }

    /**
     * If we have a Wallet covering the sender and an txOutputList
     *
     * then we can compute the signature by signing the transaction to be signed
     * consisting of the public key and input amount and the txOutput list using the
     * private key of the sender
     */

    public TxInput(String sender, int amount, TxOutputList txOutputList, Wallet wallet)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        this.amount = amount;
        this.sender = wallet.getPublicKey(sender);
        byte[] signatureTmp = wallet.getSignature(this.sender, amount, txOutputList);
        this.signature = Arrays.copyOf(signatureTmp, signatureTmp.length);
    };

    /*
     * as before but referring to the sender by the public key rather than the
     * string
     */

    public TxInput(PublicKey sender, int amount, TxOutputList txOutputList, Wallet wallet)
            throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        this.amount = amount;
        this.sender = sender;
        byte[] signatureTmp = wallet.getSignature(this.sender, amount, txOutputList);
        this.signature = Arrays.copyOf(signatureTmp, signatureTmp.length);
    };

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
     * Get the signature
     */

    public byte[] getSignature() {
        return signature;
    }

    /**
     * Create the corresponding element of TxInputUnsigned which is obtained by
     * omitting the signature.
     **/

    public TxInputUnsigned toTxInputUnsigned() {
        return new TxInputUnsigned(sender, amount);
    }

    /**
     * Task 1 Check the signature is correct for a given TxOutputList. (The
     * TxOutputList is needed to determine the message to be signed which consists
     * of the sender, amount, and the public keys and amounts for each output. It is
     * computed in the method getMessageToSign of TxInputList.java) This can be done
     * by getting the underlying TxInputUnsigned and executing the method
     * checkSignature for it referring to the TxOutputList and the signature which
     * is a field of TxInput In order for the code to compile it has been defined in
     * the questions as True but that should be replaced by the correct value.
     **/

    public boolean checkSignature(TxOutputList txol) {
        return true;
        /*
         * this is not the correct value, only used here so that the code compiles
         */
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
     * Default way of printing out the TxInput
     */

    public void print(PublicKeyMap pubKeyMap) {
        print("Sender: ", " Amount:  ", pubKeyMap);
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
        System.out.println();
        PublicKey pubKeyA = pubKeyMap.getPublicKey("Alice");
        PublicKey pubKeyB = pubKeyMap.getPublicKey("Bob");
        System.out.println("Test Alice 10");
        (new TxInput(pubKeyA, 10, signedMessage1)).print(pubKeyMap);
        System.out.println();
        System.out.println("Test Bob 20");
        (new TxInput(pubKeyB, 20, signedMessage1)).print(pubKeyMap);
    };

    /**
     * main function running test cases
     */

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        TxInput.test();
    }

}
