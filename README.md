[![Build Status](https://travis-ci.org/dlucia/moneytransfer.svg?branch=master)](https://travis-ci.org/dlucia/moneytransfer)
# Money Transfer

#####Assumptions / Design decision
1. *What is an account?*  
The first idea that came into my mind was to make a transfer (payment) between to different people,
but than reading more the assignment I realized that accounts in Revolut domain have a specific meaning, 
so I implemented the use case considering Revolut accounts (which are 1to1 to a currency)
2. *What storage to use?*  
I started using implementation Map based, since the assignment requires to use an in memory implementation.
But when I was finishing I had a doubt w/ an embedded database... I did not decided so I left both the implementation.
They are both working, and respect the repository contract tests. The composition is made in the main partition (Application.class).
Now the InMemoryConfiguration is commented in favor of the JDBC one. 
It is only necessary to uncomment it and comment the other to make a switch, if you like. 
3. *API Response body?*  
I did not decided if it was the case to have in the response body the result of the transfer, or anything.
So I left it empty and the API behave as a command, without any response body.
4. *How to handle money?*  
To handle money and currency I used **moneta** library, which handles all the operations on money.
It handles very efficiently the rounding, which is a difficult matter since different currency have different decimal policy 
(i.e EUR is like this ##.##€, whilst JPY is without decimals ##¥)
5. *Locking/Concurrency policy on account update?* I went with optimistic locking, using timestamp to validate an update.
# How to run

From the root folder:
```
mvn compile exec:java
```

It runs an http server which listen on uri http://localhost:8080/api

# Endpoint

### POST /v1/transfers
Create a new transfer between accounts for a customer.
Response codes:
* 202 =\> Transfer accepted
* 404 =\> If customer is not found with an error message
* 404 =\> If account is not found with an error message
* 400 =\> If insufficient balance for the account from which have the transfer with an error message
* 400 =\> If amount is not valid (negative) with an error message
* 503 =\> If there is a concurrent update for an account with an error message
* 500 =\> If any exception occur with an error message

Body:
```
{
	"customerId": "customer1",
	"accountFrom":"EUR",
	"accountTo":"GBP",
	"amount":"8",
	"note": "any note"
}
```