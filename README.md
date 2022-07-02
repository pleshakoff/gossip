# GOSSIP
## Chat data synchronization between nodes using GOSSIP protocol 

1. [System Description](#desc)
2. [Get started](#get-started)
3. [Examples](#examples)


<a name="desc"></a>
## System description. 

![alt text](https://github.com/pleshakoff/gossip/blob/master/GOSSIP.png?raw=true"")

Distributed AP system for storing messages sent to the chat.
GOSSIP protocol is used for data replication.
The implementation is written in Java+Spring Boot.
 
The system consists of two modules for two types of nodes: client and server. 
You can deploy an unlimited number of instances for both the server and the client.
In the current configuration, 5 nodes and 1 client are configured. 
The network topology is shown in the figure above. The topology is configured in the configuration file 
(more details [configuration](#config)) 

### Server 

https://github.com/pleshakoff/gossip/tree/master/server

Five nodes are available. Node IDs: 1,2,3,4,5

You can work with the API via swagger (more details in [API](#api))

#### Description

The server node is able to add data to the chat and show all chat records. 

Each server node has access to the database in which data is stored. 

Each node has its own separate database. 

The current implementation uses embedded in-memory database solutions.
(Competitive List). Also, 
HashSet (O(1) is used as the database index for the contains operation to quickly check if there is a record already in the chat    
If necessary, you can simply implement the appropriate interface 
to support other types of storage.

A timer has been started in the server node to start gossip interaction. 
The pull strategy is selected in the current implementation.  

Each timer node polls its neighbors for new records. 
The concept of logical clocks is used to store the data state of each node. 

The current version of the data and the versions of neighboring nodes (from the point of view of the current node) are stored 
in a special vector. Any change in the node data leads to an increase in the version vector.

If the node data changed based on the result of a pull request from a neighboring node, then the 
data version of the neighboring node and the current node version + 1 are increased in the vector.   
       
When requesting data, the node sends the supplier node its version of the data version of the supplier node. 
If the supplier node has a version already higher than the one sent, it sends new data in response.
It is important that the version should not just be higher. The current version of the sender node 
must also be higher than the version of the receiver node in the sender node vector by more than 1 
(or the version of the receiver node in the sender vector = 0). 
It means that these new changes were not received from the receiver node and the receiver does not have them yet 
and they can be given to the receiver. 

Let's consider example with two nodes

There are two nodes
 
* 1[0,0]
* 2[0,0]  

Adding one record to the first node 

* 1 [1,0]
* 2 [0,0] 

The second node requests data from the first one by sending the version number 0 and receives a new record in response 
(the version of node #2 (receiver) in the node vector #1 (sender) is 0, so the data can be sent). 
When inserting each new record, node #2 increases its version number in the vector and the version number of node #1.
And it adds a unit on top of the current node version to show that the data was inserted as 
a result of data exchange
         
* 1 [1,0]
* 2 [1,2]  

The first node requests data from the second, sending version 0. And gets 0 records in response. 
Since the sender node version in the sender vector (equal to 2) is no more than the recevier node version +1 (equal to 1)    
* 1 [1,0]
* 2 [1,2]  

Since the data can come to the node in different ways, it is still necessary to 
provide verification in order to avoid looping.

Each chat record has a visited array with a list of nodes in which the record has passed. 
If the record was already in this node, it is not inserted. 

Also, each record has a version field that contains the local version in the node corresponding to the 
logical clocks version at the time of insertion.
The version is needed to pull the necessary data on request by neighboring nodes.     
 
In order to be able to emulate node disconnections without stopping containers, it is possible to 
stop nodes via the API. After the node is stopped, it is silent and not available for other nodes and for recording by the client.  
But it is possible to get the chat contents, which is convenient for studying the cluster behavior 
in an extraordinary situation. 

      
<a name="api"></a>            
#### API 

Node #1: http://localhost:8081/api/v1/swagger-ui.html

Node #2: http://localhost:8082/api/v1/swagger-ui.html

Node #3: http://localhost:8083/api/v1/swagger-ui.html

Node #4: http://localhost:8084/api/v1/swagger-ui.html

Node #5: http://localhost:8085/api/v1/swagger-ui.html
   
The following groups of methods are available in the API 

* Context. Getting node metadata. Stop/start the node 
* Chat. Adding a message to the chat. View the chat.
* Gossip. Endpoint for pull requests

#### Realization

Packages:

* [node](https://github.com/pleshakoff/gossip/tree/master/server/src/main/java/com/gossip/server/node). 
Node metadata. Neighbor node data. Logical time 
* [exchange](https://github.com/pleshakoff/gossip/tree/master/server/src/main/java/com/gossip/server/exchange). 
Service for sending and processing gossip pull request.   
* [storage](https://github.com/pleshakoff/gossip/tree/master/server/src/main/java/com/gossip/server/storage). 
Interface for accessing the database. Its in memory implementation. Service for database operations. 
* [context](https://github.com/pleshakoff/gossip/tree/master/server/src/main/java/com/gossip/server/context). 
Decorator for easy access to node metadata.  
* [timer](https://github.com/pleshakoff/gossip/tree/master/server/src/main/java/com/gossip/server/timer). 
Timer for gossip exchange 


### Client 

https://github.com/pleshakoff/gossip/tree/master/client

It runs in a single instance in the current configuration. 
You can work with the API via swagger (more details in [API](#apiclient))

#### Description

Sends requests to the server. 
It can collect metadata from the entire cluster and show the available nodes and their states. 

Reading and recording can be done in any node.  
The client does not know which node to call in the current implementation, 
it must be specified in the request parameter. It is done intentionally 
for easy investigation of the behavior of different nodes.

A record is added to the database of the selected node during recording. 
After that, the data is gradually synchronized in all other nodes

      
<a name="apiclient"></a>            
#### API 

Client: http://localhost:8080/api/v1/swagger-ui.html
   
The following groups of methods are available in the API 

* Context. Getting metadata from the entire cluster. Stop/start the node. 
* Chat. Adding a message to the chat. View the chat

#### Realization


Packages

* [exchange](https://github.com/pleshakoff/gossip/tree/master/client/src/main/java/com/gossip/client/exchange). 
Service for obtaining metadata of server nodes

Everything else just redirects to endpoints of server nodes for reading and recording data.  

    


<a name="get-started"></a>
## Get started 

At the root of the repository you can find [docker-compose.yml](https://github.com/pleshakoff/gossip/blob/master/docker-compose.yml )
it needs to be launched, five server nodes and a client start.

`docker-compose up`


<a name="config"></a>
##### Configuration

The configuration files for the server node lie at the root of the repository  
[config_server.yml](https://github.com/pleshakoff/gossip/blob/master/config_server.yml )
and for the client node 
[config_client.yml](https://github.com/pleshakoff/gossip/blob/master/config_client.yml).

When starting docker-compose, local containers are assembled (based on app containers from docker hub) 
and the configuration file is copied to this container (see
[Dockerfile_server](https://github.com/pleshakoff/gossip/blob/master/Dockerfile_server),
[Dockerfile_client](https://github.com/pleshakoff/gossip/blob/master/Dockerfile_client)). 
In the current implementation, the same file is used for all nodes, 
but inside it is divided into sections corresponding to different profiles. One node - one profile. 
In docker-compose.yml, each node has its own profile in the startup parameters.

If changes were made to the configuration file, it is necessary to rebuild the containers
For example, at startup, specifying the flag`docker-compose up --build` 
 
If necessary, you can increase the number of nodes or change their relationships. 
You can also make your own separate config file for each node, and your own docker file. 

##### Swagger 

Node #1: http://localhost:8081/api/v1/swagger-ui.html

Node #2: http://localhost:8082/api/v1/swagger-ui.html

Node #3: http://localhost:8083/api/v1/swagger-ui.html

Node #4: http://localhost:8084/api/v1/swagger-ui.html

Node #5: http://localhost:8085/api/v1/swagger-ui.html

Client: http://localhost:8080/api/v1/swagger-ui.html 

GET requests can be run directly in the browser. 
For example, you can get the node status by following the link: http://localhost:8080/api/v1/context 

##### Pool timeout

2 seconds for all nodes. 
Timeouts can be reconfigured in configuration files

##### Logs

`docker-compose logs -f gossip-server-1 `

`docker-compose logs -f gossip-server-2`
 
`docker-compose logs -f gossip-server-3` 

`docker-compose logs -f gossip-server-4` 

`docker-compose logs -f gossip-server-5` 


<a name="examples"></a>
## Examples

Below are examples of working with a cluster

All examples need to be performed via swagger of the client node.
The same can be done by accessing directly to the server nodes, but it is more convenient via the client.   

When disabling a node via the API, adding messages is not available. But the chat view operation and receiving metadata are not blocked.

To add messages to node #1, use 

**POST** http://localhost:8080/api/v1/chat?peerId=1 

To get the entire chat of node #1 

http://localhost:8080/api/v1/chat?peerId=1

You can check how replication is going by adding messages to different nodes and getting the chat composition from different nodes. 
The chat should be identical everywhere.

You can stop
   
**POST** http://localhost:8080/api/v1/context/stop?peerId=1
   
and launch nodes 

**POST** http://localhost:8080/api/v1/context/start?peerId=1
 
After launching, the node should pull up the chat data from the neighbors. 

For checking, you can also use a method that returns data from all nodes 

http://localhost:8080/api/v1/context 

It returns a status for each node, a list of neighbors, and logical clocks. 
There is also a convenient storage Size field showing the chat size for each node.  


