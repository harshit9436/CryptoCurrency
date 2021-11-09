package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if(firstTransaction==null){
      firstTransaction = transaction;
      lastTransaction = firstTransaction;
      numTransactions = 1;
    }else{
      lastTransaction.next = transaction;
      lastTransaction = transaction;
      numTransactions = numTransactions +1;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    if(numTransactions==0){
      throw new EmptyQueueException();
    }else if (numTransactions==1){
      Transaction first = new Transaction();
      first = firstTransaction;
      firstTransaction = null;
      lastTransaction = null;
      numTransactions = 0;
      return first;
    }
    else{
      Transaction new_first = new Transaction();
      Transaction first = new Transaction();
      first = firstTransaction;
      new_first = firstTransaction.next;
      firstTransaction = new_first;
      numTransactions = numTransactions-1;
      return first;
    }

  }

  public int size() {
    return numTransactions;
  }
}
