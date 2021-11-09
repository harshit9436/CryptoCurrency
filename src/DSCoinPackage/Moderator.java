package DSCoinPackage;

import HelperClasses.Pair;

public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) throws EmptyQueueException {
 TransactionQueue pendingTransaction = DSObj.pendingTransactions;
    Members moderator = new Members();
    moderator.UID = "Moderator";
    DSObj.latestCoinID = String.valueOf(100000 + coinCount -1);
   for (int i = 0; i < coinCount/DSObj.memberlist.length ; i++) {
    for (int j = 0; j < DSObj.memberlist.length; j++) {
     Transaction transaction = new Transaction();
     transaction.coinsrc_block = null;
     transaction.coinID = String.valueOf(100000 + DSObj.memberlist.length * i + j);
     transaction.Destination = DSObj.memberlist[j];
     transaction.Source = moderator;
     pendingTransaction.AddTransactions(transaction);
    }
   }

   for (int i = 0; i < coinCount/DSObj.memberlist.length; i++) {
       Transaction[] array = new Transaction[DSObj.bChain.tr_count];
       for (int j = 0; j < DSObj.bChain.tr_count ; j++) {
           array[j] = pendingTransaction.RemoveTransaction();
       }
       TransactionBlock block = new TransactionBlock(array);
       block.trarray = array;

       for(Transaction tr : array){
           tr.Destination.mycoins.add(new Pair<String , TransactionBlock>(tr.coinID,block));
       }
    DSObj.bChain.InsertBlock_Honest(block);
   }

  }

     public void initializeDSCoin (DSCoin_Malicious DSobj, int coinCount){
         Members moderator = new Members();
         moderator.UID="Moderator";
         int i=0;
         String firstcoin= "100000";
         DSobj.latestCoinID=Integer.toString(Integer.parseInt(firstcoin)+coinCount-1);
         while(i<coinCount){
             Transaction tr = new Transaction();
             tr.coinID=Integer.toString(Integer.parseInt(firstcoin)+i);
             tr.coinsrc_block=null;
             tr.Destination=DSobj.memberlist[i % DSobj.memberlist.length];
             tr.Source=moderator;
             DSobj.pendingTransactions.AddTransactions(tr);
             i++;
         }


         for(int j=0; j<coinCount/DSobj.bChain.tr_count; j++){
             Transaction[] array = new Transaction[DSobj.bChain.tr_count];

             for(int k=0; k<DSobj.bChain.tr_count; k++){

                 try{
                     array[k]=DSobj.pendingTransactions.RemoveTransaction();}
                 catch(Exception e){
                     e.printStackTrace();
                 }
             }

             TransactionBlock block = new TransactionBlock(array);

            for(Transaction tr : array){
                tr.Destination.mycoins.add(new Pair<String , TransactionBlock>(tr.coinID,block));
            }


             DSobj.bChain.InsertBlock_Malicious(block);
         }


     }
}
