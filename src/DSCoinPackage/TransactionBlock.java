package DSCoinPackage;

import HelperClasses.MerkleTree;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    trarray = new Transaction[t.length];
    for (int i = 0; i < t.length; i++) {
      this.trarray[i]  = t[i];
    }
    MerkleTree merkleTree = new MerkleTree();
    this.trsummary = merkleTree.Build(t);
    this.Tree = merkleTree;
    this.dgst = null;
    this.nonce = null;
    this.previous = null;
  }

  public boolean checkTransaction (Transaction t) {
    TransactionBlock transactionBlock = t.coinsrc_block;
    if (transactionBlock!=null) {
      Transaction[] arr = transactionBlock.trarray;
      boolean ans = false;
      for (Transaction a : arr) {
        if (a.coinID.equals(t.coinID)) {
          ans = true;
        }
      }
      TransactionBlock current = this;
      loop1:
      while (current != transactionBlock) {
        Transaction[] arr1 = current.trarray;
        for (Transaction a : arr1) {
          if (a.coinID.equals(t.coinID)) {
            ans = false;
            break loop1;
          }
        }
        current = current.previous;
      }
      return ans;
    }else{
      return true;
    }
  }
}
