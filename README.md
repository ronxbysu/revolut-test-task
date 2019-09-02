# revolut-test-task
Design and implement a RESTful API (including data model and the backing implementation) for money transfers between accounts.

Backend Test

Design and implement a RESTful API (including data model and the backing implementation)
for money transfers between accounts.
Explicit requirements:
1. You can use Java, Scala or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 – keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require
a pre-installed container/server).
7. Demonstrate with tests that the API works as expected.
Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.
Please put your work on github or bitbucket.

==========================================================================================
##how to build:
  mvn clean package
##how to run:
  `cd target`
  `java -jar revolut-1.0-SNAPSHOT.jar 8081`
##what will be displayed:  
  Server is listening on port 8081
##API
* ###  By design there is at least one user exist - CashMachine with 1000 credit. Therefore you can transfer from cash machine to any new user and that user will be created. In the log will listed information about the current status of the whole database - who has how much  

   
###Example: 
  `curl -X POST -i http://localhost:8081/transfer --data '{"from":"CashMachine","to":"Tanya","amount":"20"}'`
#####what will be returned to the client:
  {
    "status": "TRANSFERRED"
  }
#####what will you see in the default system log:  
  >New client connected  
  >No recipient found. Creating...  
  >CashMachine 980  
  >Tanya 20  

###Next example:  
  
  `curl -X POST -i http://localhost:8081/transfer --data '{"from":"CashMachine","to":"Roman","amount":"200"}'`
  
  {
    "status": "TRANSFERRED"
  }
  
  >New client connected  
  >No recipient found. Creating...  
  >CashMachine 780  
  >Tanya 20  
  >Roman 200  

###Next example:  
  
  `curl -X POST -i http://localhost:8081/transfer --data '{"from":"Roman","to":"Tanya","amount":"100"}'`
  
  {
    "status": "TRANSFERRED"
  }
  
  >New client connected  
  >CashMachine 780  
  >Tanya 120  
  >Roman 100  

* ### By design if you try to transfer less or equals than zero or amount of transfer will be more than already loaded to the account you will get a failed message with error message in the log  
  
###Example:  
  `curl -X POST -i http://localhost:8081/transfer --data '{"from":"Roman","to":"Tanya","amount":"-100"}'`

  {
    "status": "FAILED"
  }
  #####what will you see in the default system log:    
  ``
  New client connected
  Server exception: null
  com.drastic.exception.NegativeAmountException
  	at com.drastic.transfer.MoneyTransfer.transfer(MoneyTransfer.java:34)
  	at com.drastic.transfer.TransferMaker.makeTransfer(TransferMaker.java:20)
  	at com.drastic.server.Request.run(Request.java:42)
  	at java.lang.Thread.run(Thread.java:748)
  	``

###Next example:  
`curl -X POST -i http://localhost:8081/transfer --data '{"from":"Roman","to":"Tanya","amount":"500"}'`

{
  "status": "FAILED"
}

  #####what will you see in the default system log:   
  ``
New client connected
com.drastic.exception.InsufficientFundsException
Server exception: null
	at com.drastic.transfer.MoneyTransfer.transfer(MoneyTransfer.java:36)
	at com.drastic.transfer.TransferMaker.makeTransfer(TransferMaker.java:20)
	at com.drastic.server.Request.run(Request.java:42)
	at java.lang.Thread.run(Thread.java:748)
  ``
 
