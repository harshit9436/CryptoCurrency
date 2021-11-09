package DSCoinPackage;

import HelperClasses.*;


public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList = new TransactionBlock[100];

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    if(tB.dgst.substring(0,4).equals("0000")){
      CRF obj = new CRF(64);
      if(tB.previous==null){
        if(!tB.dgst.equals(obj.Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce))){
          return false;
        }
      }else{
        if(!tB.dgst.equals(obj.Fn(tB.previous.dgst+"#"+tB.trsummary+"#"+tB.nonce))){
          return false;
        }
      }
    }else{
      return false;
    }
    MerkleTree tree = new MerkleTree();
    if(!tB.trsummary.equals(tree.Build(tB.trarray))){
      return false;
    }
    for(int i=0;i<tB.trarray.length;i++){
      if(!tB.checkTransaction(tB.trarray[i])){
        return false;
      }
    }
    return true;
  }

  public TransactionBlock FindLongestValidChain () {
    TransactionBlock[] last_valid_block = new TransactionBlock[lastBlocksList.length];
    int[] length_list=new int[lastBlocksList.length];
    for(int i=0;lastBlocksList[i]!=null;i++){
      TransactionBlock block = lastBlocksList[i];
      last_valid_block[i]=block;
      int counter=0;
      while(block!=null){
        if(checkTransactionBlock(block)){
          if(counter==0){
            last_valid_block[i]=block;
          }
          counter++;
        }else{
          counter=0;
        }
        block=block.previous;
      }
      length_list[i]=counter;
    }
    int maxLengthIndex=0;
    for(int i=1;lastBlocksList[i]!=null;i++){
      if(length_list[maxLengthIndex]<length_list[i]){
        maxLengthIndex=i;
      }
    }
    return last_valid_block[maxLengthIndex];
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock lastBlock=this.FindLongestValidChain();
    int j=0;
    for(j=0;lastBlocksList[j]!=null;j++){
      if(lastBlocksList[j]==lastBlock){
        break;
      }
    }
    if(lastBlock==null){
      CRF obj = new CRF(64);
      newBlock.nonce="1000000001";
      newBlock.dgst= obj.Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      while(!newBlock.dgst.substring(0,4).equals("0000") ){
        newBlock.nonce=String.valueOf(Integer.parseInt(newBlock.nonce)+1);
        newBlock.dgst= obj.Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      }
      lastBlock=newBlock;
    }else{
      CRF obj = new CRF(64);
      newBlock.nonce="1000000001";
      newBlock.dgst= obj.Fn(lastBlock.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      while(!newBlock.dgst.substring(0,4).equals("0000") ){
        newBlock.nonce=String.valueOf(Integer.parseInt(newBlock.nonce)+1);
        newBlock.dgst= obj.Fn(lastBlock.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      }
      newBlock.previous=lastBlock;
      lastBlock=newBlock;  
    }
    lastBlocksList[j]=lastBlock;
  }
}
