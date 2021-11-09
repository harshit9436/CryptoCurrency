package DSCoinPackage;

import java.util.*;
import HelperClasses.*;
import DSCoinPackage.*;

class Tester {


    public static void main(String args[]) {
	DSCoin_Honest obj = createObj(4, 4);

	Moderator mod = new Moderator();
		try {
			mod.initializeDSCoin(obj, 8);
		} catch (EmptyQueueException e) {
			e.printStackTrace();
		}

		System.out.println("AFTER DISTRIBUTION BY MOD-");
	System.out.println("Nonce is " + obj.bChain.lastBlock.nonce);
	System.out.println("Dgst is " + obj.bChain.lastBlock.dgst);
	System.out.println("Trsummary is " + obj.bChain.lastBlock.trsummary);
	System.out.println("Coins of 101: ");
	System.out.println(obj.memberlist[0].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[0].mycoins.get(1).get_first());
	System.out.println("Coins of 102: ");
	System.out.println(obj.memberlist[1].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(1).get_first());
	System.out.println("Coins of 103: ");
	System.out.println(obj.memberlist[2].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[2].mycoins.get(1).get_first());
	System.out.println("Coins of 104: ");
	System.out.println(obj.memberlist[3].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[3].mycoins.get(1).get_first());
	System.out.println();

	Transaction t = new Transaction();
	t.coinID = "100000";
	t.Source = obj.memberlist[0];
	t.Destination = obj.memberlist[2];
	t.coinsrc_block = obj.bChain.lastBlock.previous.previous;

	obj.memberlist[0].initiateCoinsend(obj.memberlist[1].UID, obj);
	obj.pendingTransactions.AddTransactions(t);
	obj.memberlist[2].initiateCoinsend(obj.memberlist[1].UID, obj);
	obj.memberlist[2].initiateCoinsend(obj.memberlist[1].UID, obj);
	obj.memberlist[1].MineCoin(obj);
	try {
		obj.memberlist[0].finalizeCoinsend(obj.bChain.lastBlock.trarray[0], obj);
		obj.memberlist[2].finalizeCoinsend(obj.bChain.lastBlock.trarray[1], obj);
		obj.memberlist[2].finalizeCoinsend(obj.bChain.lastBlock.trarray[2], obj);
	} catch (MissingTransactionException e) {}

	System.out.println("AFTER 1ST SET OF TRANSACTIONS-");
	System.out.println("Nonce is " + obj.bChain.lastBlock.nonce);
	System.out.println("Dgst is " + obj.bChain.lastBlock.dgst);
	System.out.println("Trsummary is " + obj.bChain.lastBlock.trsummary);
	System.out.println("Coins of 101: ");
	System.out.println(obj.memberlist[0].mycoins.get(0).get_first());
	System.out.println("Coins of 102: ");
	System.out.println(obj.memberlist[1].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(1).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(2).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(3).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(4).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(5).get_first());
	System.out.println("Coins of 103: ");
	if (obj.memberlist[2].mycoins.size() == 0) {
		System.out.println("None");
	} else {
		System.out.println("WRONG");
	}
	System.out.println("Coins of 104: ");
	System.out.println(obj.memberlist[3].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[3].mycoins.get(1).get_first());
	System.out.println();

	obj.memberlist[1].initiateCoinsend(obj.memberlist[0].UID, obj);
	obj.memberlist[1].initiateCoinsend(obj.memberlist[0].UID, obj);
	obj.memberlist[3].initiateCoinsend(obj.memberlist[0].UID, obj);
	obj.memberlist[2].MineCoin(obj);
	try {
		obj.memberlist[1].finalizeCoinsend(obj.bChain.lastBlock.trarray[0], obj);
		obj.memberlist[1].finalizeCoinsend(obj.bChain.lastBlock.trarray[1], obj);
		obj.memberlist[3].finalizeCoinsend(obj.bChain.lastBlock.trarray[2], obj);
	} catch (MissingTransactionException e) {}

	System.out.println("AFTER 2ND SET OF TRANSACTIONS-");
	System.out.println("Nonce is " + obj.bChain.lastBlock.nonce);
	System.out.println("Dgst is " + obj.bChain.lastBlock.dgst);
	System.out.println("Trsummary is " + obj.bChain.lastBlock.trsummary);
	System.out.println("Coins of 101: ");
	System.out.println(obj.memberlist[0].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[0].mycoins.get(1).get_first());
	System.out.println(obj.memberlist[0].mycoins.get(2).get_first());
	System.out.println(obj.memberlist[0].mycoins.get(3).get_first());
	System.out.println("Coins of 102: ");
	System.out.println(obj.memberlist[1].mycoins.get(0).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(1).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(2).get_first());
	System.out.println(obj.memberlist[1].mycoins.get(3).get_first());
	System.out.println("Coins of 103: ");
	System.out.println(obj.memberlist[2].mycoins.get(0).get_first());
	System.out.println("Coins of 104: ");
	System.out.println(obj.memberlist[3].mycoins.get(0).get_first());

	System.out.println();
	System.out.println("MALICIOUS TEST CASE: ");
	System.out.println();


	DSCoin_Malicious DSObj2 = new DSCoin_Malicious();
	DSObj2.pendingTransactions = new TransactionQueue();
	DSObj2.bChain = new BlockChain_Malicious();
	DSObj2.bChain.lastBlocksList = new TransactionBlock[100];
	Boolean correct2 = true;
	DSObj2.bChain.tr_count = 8;
	Members M1 = new Members();
	Members M2 = new Members();
	Members M3 = new Members();
	Members M4 = new Members();
	Members M5 = new Members();
	Members M6 = new Members();
	M1.UID = "201";
	M2.UID = "202";
	M3.UID = "203";
	M4.UID = "204";
	M5.UID = "205";
	M6.UID = "206";
	M1.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
	M2.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
	M3.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
	M4.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
	M5.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
	M6.mycoins = new ArrayList<Pair<String,TransactionBlock>>();
	M1.in_process_trans = new Transaction[100];
	M2.in_process_trans = new Transaction[100];
	M3.in_process_trans = new Transaction[100];
	M4.in_process_trans = new Transaction[100];
	M5.in_process_trans = new Transaction[100];
	M6.in_process_trans = new Transaction[100];
	DSObj2.memberlist = new Members[6];
	DSObj2.memberlist[0] = M1;
	DSObj2.memberlist[1] = M2;
	DSObj2.memberlist[2] = M3;
	DSObj2.memberlist[3] = M4;
	DSObj2.memberlist[4] = M5;
	DSObj2.memberlist[5] = M6;
	try {
		mod.initializeDSCoin(DSObj2, 24);
	} catch (Exception e) {
		System.out.println(e);
	}

	System.out.println(DSObj2.bChain.lastBlocksList[0].dgst);
	System.out.println(DSObj2.bChain.lastBlocksList[0].nonce);

//		System.out.println(M1.mycoins.get(2).get_first());
//		System.out.println(M1.mycoins.get(3).get_first());
//		System.out.println(M1.mycoins.get(4).get_first());
//		System.out.println(M1.mycoins.get(5).get_first());
	Transaction T1 = new Transaction();
	Transaction T2 = new Transaction();
	Transaction T3 = new Transaction();
	Transaction T4 = new Transaction();
	Transaction T5 = new Transaction();
	Transaction T6 = new Transaction();
	Transaction T7 = new Transaction();
	Transaction T8 = new Transaction();
//		System.out.println("Driver M1" + M1.mycoins.get(0).get_first());
//		System.out.println("Driver M2" + M2.mycoins.get(0).get_first());
//		System.out.println("Driver M3" + M3.mycoins.get(0).get_first());
//		System.out.println("Driver M4" + M4.mycoins.get(0).get_first());
//		System.out.println("Driver M5" + M5.mycoins.get(0).get_first());
//		System.out.println("Driver M6" + M6.mycoins.get(0).get_first());
//		System.out.println("_______________");
//
//		System.out.println("Driver M1" + M1.mycoins.get(1).get_first());
//		System.out.println("Driver M2" + M2.mycoins.get(1).get_first());
//		System.out.println("Driver M3" + M3.mycoins.get(1).get_first());
//		System.out.println("Driver M4" + M4.mycoins.get(1).get_first());
//		System.out.println("Driver M5" + M5.mycoins.get(1).get_first());
//		System.out.println("Driver M6" + M6.mycoins.get(1).get_first());
//		System.out.println("_______________");
//		System.out.println("Driver M1" + M1.mycoins.get(2).get_first());
//		System.out.println("Driver M2" + M2.mycoins.get(2).get_first());
//		System.out.println("Driver M3" + M3.mycoins.get(2).get_first());
//		System.out.println("Driver M4" + M4.mycoins.get(2).get_first());
//		System.out.println("Driver M5" + M5.mycoins.get(2).get_first());
//		System.out.println("Driver M6" + M6.mycoins.get(2).get_first());
//		System.out.println("______");
//		System.out.println("Driver M1" + M1.mycoins.get(3).get_first());
//		System.out.println("Driver M2" + M2.mycoins.get(3).get_first());
//		System.out.println("Driver M3" + M3.mycoins.get(3).get_first());
//		System.out.println("Driver M4" + M4.mycoins.get(3).get_first());
//		System.out.println("Driver M5" + M5.mycoins.get(3).get_first());
//		System.out.println("Driver M6" + M6.mycoins.get(3).get_first());

	T1.coinID = DSObj2.memberlist[0].mycoins.get(0).get_first();
	T1.Source = DSObj2.memberlist[0];
	T1.Destination = DSObj2.memberlist[1];
	T1.coinsrc_block = DSObj2.memberlist[0].mycoins.get(0).get_second();
	DSObj2.pendingTransactions.AddTransactions(T1);

	T2.coinID = DSObj2.memberlist[2].mycoins.get(0).get_first();
	T2.Source = DSObj2.memberlist[2];
	T2.Destination = DSObj2.memberlist[1];
	T2.coinsrc_block = DSObj2.memberlist[2].mycoins.get(0).get_second();
	DSObj2.pendingTransactions.AddTransactions(T2);

	T3.coinID = DSObj2.memberlist[2].mycoins.get(1).get_first();
	T3.Source = DSObj2.memberlist[2];
	T3.Destination = DSObj2.memberlist[1];
	T3.coinsrc_block = DSObj2.memberlist[2].mycoins.get(1).get_second();
	DSObj2.pendingTransactions.AddTransactions(T3);

	T4.coinID = DSObj2.memberlist[3].mycoins.get(0).get_first();
	T4.Source = DSObj2.memberlist[3];
	T4.Destination = DSObj2.memberlist[4];
	T4.coinsrc_block = DSObj2.memberlist[3].mycoins.get(0).get_second();
	DSObj2.pendingTransactions.AddTransactions(T4);

	T5.coinID = DSObj2.memberlist[3].mycoins.get(1).get_first();
	T5.Source = DSObj2.memberlist[3];
	T5.Destination = DSObj2.memberlist[4];
	T5.coinsrc_block = DSObj2.memberlist[3].mycoins.get(1).get_second();
	DSObj2.pendingTransactions.AddTransactions(T5);

	T6.coinID = DSObj2.memberlist[3].mycoins.get(2).get_first();
	T6.Source = DSObj2.memberlist[3];
	T6.Destination = DSObj2.memberlist[5];
	T6.coinsrc_block = DSObj2.memberlist[3].mycoins.get(2).get_second();
	DSObj2.pendingTransactions.AddTransactions(T6);

	T7.coinID = DSObj2.memberlist[0].mycoins.get(1).get_first();
	T7.Source = DSObj2.memberlist[0];
	T7.Destination = DSObj2.memberlist[5];
	T7.coinsrc_block = DSObj2.memberlist[0].mycoins.get(1).get_second();
	DSObj2.pendingTransactions.AddTransactions(T7);

	T8.coinID = DSObj2.memberlist[2].mycoins.get(2).get_first();
	T8.Source = DSObj2.memberlist[2];
	T8.Destination = DSObj2.memberlist[4];
	T8.coinsrc_block = DSObj2.memberlist[2].mycoins.get(2).get_second();
	DSObj2.pendingTransactions.AddTransactions(T8);



	try {
		M3.MineCoin(DSObj2);
	} catch (Exception e) {}



	System.out.println(DSObj2.bChain.lastBlocksList[0].dgst);
	System.out.println(DSObj2.bChain.lastBlocksList[0].nonce);
    }

    public static DSCoin_Honest createObj (int numOfMembers, int trcount){
	DSCoin_Honest obj = new DSCoin_Honest();
	obj.pendingTransactions = new TransactionQueue();
	obj.memberlist = new Members[numOfMembers];
	for (int i = 0; i < numOfMembers; i ++){
	    obj.memberlist[i] = new Members();
	    obj.memberlist[i].UID = Integer.toString(i+101);
	    obj.memberlist[i].mycoins = new ArrayList<Pair<String,TransactionBlock>>();
	}
	obj.bChain = new BlockChain_Honest();
	obj.bChain.tr_count = trcount;
	return obj;
    }
}
