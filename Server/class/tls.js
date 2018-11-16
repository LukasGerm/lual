const tls = require("tls");
const database = require("./database");
//Server for the tls connections
class tlsServer {
  constructor(key, cert) {
    //initialize the clients array the sockets will be pushed here
    this.users = [];
    //Tls certs
    this.options = {
      key: key,
      cert: cert
    };
  }
  //Validate the data
  validateData(data, socket) {
    //Split by |
    const splitData = data.split("|");
    const option = splitData[0];

    switch (option) {
      // First Login case. The user authenticates by username and hashed password. He gets a jwt token and the object-id back so the client can save it
      //If the user loggs in first on the client, the user only has to log in once. If the user logs in first time in general, he gets the popup to change his pw
      case 1:
        break;
      // Normal Login case. The client authenticates itself by sending the objectid and the jwt token. The Server checks it, if it is expired, it sends back a message.
      // If not, the user can log in
      case 2:
        break;
      //Change PW case. If the user first loggs in in general, he gets the popup to change his pw. This case is called then.
      case 3:
        break;
      //Alarm case. This case is called, when a client sends an alarm message.
      case 4:
        break;
      //Do nothing or so
      default:
      //Destroy cause invalid input
      socket.destroy();
        break;
    }
  }
  //Splice the socket from the array or later the user
  spliceUser(clients, socket) {
    for (let index = 0; index < clients.length; index++) {
      if (socket === clients[index]) {
        clients.splice(index, 1);
      }
    }
    //Destroy the socket
    socket.destroy();
  }
  //method for alarming the users
  sendAlarm(user,socket){

  }
  run(port) {
    let server;
    server = tls.createServer(this.options, socket => {
      //Push the client in the client array
      //Here goes some authentication logic first
      //Only testing code
      this.users.push(socket);
      socket.setEncoding("utf8");
      socket.on("data", data => {
        this.validateData(data);
      });
      //If an error occurs
      socket.on("error", e => {
        console.log("Connection reset");
        this.spliceUser(this.users, socket);
      });
      socket.on("end", () => {
        this.spliceUser(this.users, socket);
        //Destroy the socket
        socket.destroy();
      });
    });
    server.listen(port, () => {});
  }
}

module.exports = tlsServer;
