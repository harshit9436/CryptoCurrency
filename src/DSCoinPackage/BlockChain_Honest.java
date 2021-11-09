package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    CRF crf = new CRF(64);
    if (lastBlock == null) {
      tr_count = newBlock.trarray.length;
      lastBlock = newBlock;
      String start = "1000000001";
      while (start.compareTo("9999999999") != 1) {
        String dgst = crf.Fn(start_string + "#" + newBlock.trsummary + "#" + start);
        if (dgst.substring(0, 4).equals("0000")) {
          newBlock.nonce = start;
          newBlock.dgst = dgst;
          lastBlock = newBlock;
          break;
        } else {
          long x = Long.parseLong(start);
          x = x + 1;
          start = String.valueOf(x);
        }

      }
    }else {
      tr_count = newBlock.trarray.length;
      newBlock.previous = lastBlock;
      lastBlock = newBlock;
      String start = "1000000001";
      while (start.compareTo("9999999999") != 1) {
        String dgst = crf.Fn(newBlock.previous.dgst + "#" + newBlock.trsummary + "#" + start);
        if (dgst.substring(0, 4).equals("0000")) {
          newBlock.nonce = start;
          newBlock.dgst = dgst;
          break;
        } else {
          long x = Long.parseLong(start);
          x = x + 1;
          start = String.valueOf(x);
        }
      }
    }
  }
}
