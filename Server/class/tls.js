const tls = require("tls");
const database = require("./database");
const jwt = require('jsonwebtoken');
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
  //Used to generate the jwt token returns the token
  generateToken(user, callback){
    //Sign the token, put the username and password in it
    jwt.sign({
      userId: user._id,
      password: user.password
    },this.options.key,{algorithm: 'RS256'} ,(err, token) =>{
      //Return it to the callback
      if(err) return callback();
      callback(token);
    });
  }
  //used to verify a token
  verifyToken(token, callback){
    jwt.verify(token,this.options.cert, (err, data) => {
      if(err) return callback(err);
        //Get the user by id
        callback(err, data);
    });
  }
  //Validate the data
  validateData(data, socket) {
    //Split by |
    const splitData = data.split("|");
    const option = splitData[0];
    let token;
    switch (option) {
      // First Login case. The user authenticates by username and hashed password. He gets a jwt token and the object-id back so the client can save it
      //If the user loggs in first on the client, the user only has to log in once. If the user logs in first time in general, he gets the popup to change his pw
      case "1":
      let username = splitData[1];
      let password = splitData[2];
      database.getUser(username, (user) => {
        //User not found
        if(!user) return socket.write("1");
        //Number 1 is showing the normal login form
        if(user.password !== password) return socket.write("1|1");
        if(user.firstLogin){
          //Number 2  is first login, indicates that the client opens the changepw form
          return socket.write("1|2")
        }
        
        //generate a token and send it to the client
        this.generateToken(user, (token)=>{
          //1 indicates that a token was send
          if(token) {
            socket.write('token|'+token);
            let user = {
              objectid: user._id,
              group: user.group,
              socket: socket,
              firstName: user.firstName,
              lastName: user.lastName,
              roomNumber: user.roomNumber
            }
            this.users.push(user);
            //After that, tell the client everything is okay and logg the user in
            socket.write('1|ok');
          }
          else console.log('Error signing token');

        });
      });
        break;
      // Normal Login case. The client authenticates itself by sending the objectid and the jwt token. The Server checks it, if it is expired, it sends back a message.
      // If not, the user can log in
      case "2":
        token = splitData[1];
        //If an error occurs, the user must log in new
        this.verifyToken(token, (err, data) => {
          console.log(err);
          //Error happened, show the login form
          if(err) return socket.write("2|1");
          database.getUserById(data.userId, doc => {
            //User not found, show the loginform
            if(!doc) return socket.write("2|1");
            //PW Wrong, show the loginform
            if(doc.password !== data.password){
              return socket.write("2|1");
            }
            let user = {
              objectid: data.userId,
              group: doc.group,
              room: doc.roomNumber,
              socket: socket
            }
            this.users.push(user);
            //Everything is okay, user is loggedin
            socket.write("2|ok");
          });  
          
        });
        break;
      //Change PW case. If the user first loggs in in general, he gets the popup to change his pw. This case is called then.
      case "3":
        const user = splitData[1];
        const newPassword = splitData[2];
        database.updateUserPassword(user, newPassword, (err) => {
          //Something went wrong
          if(err) return socket.write("3|3");
          //Show the normal loginform
          socket.write("3|1");
        }) 
        break;
      //Alarm case. This case is called, when a client sends an alarm message.
      case "4":
        token = splitData[1];
        //Verify the token
        this.verifyToken(token, (err, data) => {
          if(err) return socket.write(err);
          console.log(data);
          //for every user in the array
          this.users.forEach(user => {
            
            if(socket !== user.socket && user.group == data.group){
              //Send alarm to the group, and the roomnumber plus first and lastname
              socket.write("4|"+data.roomNumber+"|"+data.firstName+"|"+data.lastName);
            }
          });
        })
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
      if (socket === clients[index].socket) {
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
      socket.setEncoding("utf8");
      socket.on("data", data => {
        this.validateData(data, socket);
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
