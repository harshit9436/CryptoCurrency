package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.MerkleTree;
import HelperClasses.Pair;
import HelperClasses.TreeNode;
import com.sun.jdi.event.StepEvent;
import com.sun.source.tree.WhileLoopTree;

import java.lang.reflect.Member;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Members
{

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans = new Transaction[100];

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {

   Pair<String , TransactionBlock> coin = this.mycoins.get(0);
   Transaction tobj = new Transaction();
   tobj.coinID = coin.first;
   String coinID = coin.first;
   Members buyer_memb = this;
   Members seller_memb = new Members();

   for (Members m : DSobj.memberlist ){
    if(m.UID.equals(destUID)){
     seller_memb = m;
    }
   }

   this.mycoins.remove(0);
   tobj.Destination = seller_memb;
   tobj.Source = buyer_memb;
   tobj.coinsrc_block = coin.second;

   int i =0;
   while(in_process_trans[i]!=null){
    i++;
   }
   in_process_trans[i] = tobj;

   DSobj.pendingTransactions.AddTransactions(tobj);

  }


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
   BlockChain_Honest blockChain = DSObj.bChain;
   TransactionBlock block = blockChain.lastBlock;
   TransactionBlock block_cont_Trans = null;

   loop1: while(block.previous!=null){
    for(Transaction t : block.trarray){
     if(t.equals(tobj)){
       block_cont_Trans = block;
      break loop1;
     }
    }
    block = block.previous;
   }
   if(block_cont_Trans == null && block.previous == null){
    for(Transaction t : block.trarray){
     if(t.equals(tobj)){
      block_cont_Trans = block;}
    }
   }
   if(block_cont_Trans == null){
    throw new MissingTransactionException();
   }else{
 Transaction[] array = block_cont_Trans.trarray;
    CRF crf = new CRF(64);
    TransactionBlock block1 = blockChain.lastBlock;
 List<Pair<String , String>> second_list = new ArrayList<Pair<String , String>>();
 List<Pair<String , String>> first_list = new ArrayList<Pair<String , String>>();

    while (block1!=block_cont_Trans.previous){
     String first = block1.dgst;
     String second = block1.previous.dgst + "#" + block1.trsummary + "#" + block1.nonce;
     second_list.add(new Pair<String , String>(first, second));

     block1 = block1.previous;
    }

    String first = block_cont_Trans.previous.dgst;
    String second = null;
    second_list.add(new Pair<String , String>(first , second));
    Collections.reverse(second_list);
    MerkleTree tree = block_cont_Trans.Tree;
    int index = 0;
       for (int i = 0; i < array.length; i++) {
     if(array[i].equals(tobj)){
      index = i;
      break;
     }
    }

    int length = array.length;
    TreeNode node = tree.rootnode;
    while (node.left!=null){
     if(index < length/2){
      node = node.left;
      length = length/2;
     }else{
         node= node.right;
         index = index-length/2;
         length = length/2;
     }
    }


    while (node.parent!=null){
        String first1 = node.parent.left.val;
        String second1 = node.parent.right.val;
        Pair<String , String> pair = new Pair<>(first1, second1);
        first_list.add(pair);
        node = node.parent;
    }
    String x = node.val;
       Pair<String , String> pair1 = new Pair<>(x, null);
       first_list.add(pair1);
       for (int i = index; i < this.in_process_trans.length -1 ; i++) {
           this.in_process_trans[i] = this.in_process_trans[i+1];
       }
       Pair<String, TransactionBlock> pair_final = new Pair<String, TransactionBlock>(tobj.coinID , block_cont_Trans);

       String coinID = tobj.coinID;
       boolean inserted = false;
       for (int i = 0; i < tobj.Destination.mycoins.size(); i++) {

           if(coinID.compareTo(tobj.Destination.mycoins.get(i).first) <0){

               tobj.Destination.mycoins.add(i,new Pair<String, TransactionBlock>(coinID, block_cont_Trans));
               inserted = true;
               break;
           }
       }
       if(!inserted){
           tobj.Destination.mycoins.add(new Pair<String, TransactionBlock>(tobj.coinID, block_cont_Trans));
       }

       Pair<List<Pair<String, String>>, List<Pair<String, String>>> ans = new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(first_list , second_list);

    return ans;
   }
  }

  public void MineCoin(DSCoin_Honest DSObj) {
      TransactionQueue pending_trans = DSObj.pendingTransactions;
      Transaction[] arr = new Transaction[DSObj.bChain.tr_count];

      try {
          arr[0] = pending_trans.RemoveTransaction();
      } catch (EmptyQueueException e) {
          e.printStackTrace();
      }

      while (pending_trans.numTransactions!=0){
          Transaction x = new Transaction();
          try {
              x = pending_trans.RemoveTransaction();
          } catch (EmptyQueueException e) {
              e.printStackTrace();
          }
          int index = 0;
          while(arr[index]!=null){
              index++;
          }

          boolean matched = false;
          for (int i = 0; i < index; i++) {
            if(arr[i].coinID.equals(x.coinID)){
                matched = true;
                break;
            }
          }
          if(!matched){
              arr[index] = x;
          }
      }



      Transaction miner_trans = new Transaction();
      DSObj.latestCoinID = String.valueOf(Integer.parseInt(DSObj.latestCoinID) + 1);
      miner_trans.coinID = DSObj.latestCoinID;
      miner_trans.Source = null;
      miner_trans.Destination = this;
      miner_trans.coinsrc_block = null;



      arr[DSObj.bChain.tr_count-1] = miner_trans;
      TransactionBlock tB = new TransactionBlock(arr);
      tB.trarray = arr;
      DSObj.bChain.InsertBlock_Honest(tB);
      this.mycoins.add(new Pair<String, TransactionBlock>(miner_trans.coinID, tB));


  }  

  public void MineCoin(DSCoin_Malicious DSObj) {
      TransactionQueue pending_trans = DSObj.pendingTransactions;
      Transaction[] arr = new Transaction[DSObj.bChain.tr_count];

      try {
          arr[0] = pending_trans.RemoveTransaction();
      } catch (EmptyQueueException e) {
          e.printStackTrace();
      }

      while (pending_trans.numTransactions != 0) {
          Transaction x = new Transaction();
          try {
              x = pending_trans.RemoveTransaction();
          } catch (EmptyQueueException e) {
              e.printStackTrace();
          }
          int index = 0;
          while (arr[index] != null) {
              index++;
          }

          boolean matched = false;
          for (int i = 0; i < index; i++) {
              if (arr[i].coinID.equals(x.coinID)) {
                  matched = true;
                  break;
              }
          }
          if (!matched) {
              arr[index] = x;
          }

      }

      Transaction miner_trans = new Transaction();
      DSObj.latestCoinID = String.valueOf(Integer.parseInt(DSObj.latestCoinID) + 1);
      miner_trans.coinID = DSObj.latestCoinID;
      miner_trans.Source = null;
      miner_trans.Destination = this;
      miner_trans.coinsrc_block = null;


      arr[DSObj.bChain.tr_count - 1] = miner_trans;


      TransactionBlock tB = new TransactionBlock(arr);

      tB.trarray = arr;
      DSObj.bChain.InsertBlock_Malicious(tB);
      this.mycoins.add(new Pair<String, TransactionBlock>(miner_trans.coinID, tB));

  }
  }
