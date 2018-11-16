const mongoose = require("mongoose");
const ObjectId = mongoose.Types.ObjectId;

//Connect to the database
mongoose.connect(
  "mongodb://localhost:27017/lual",
  {
    //Use the new url parser cause the old one is deprecated
    useNewUrlParser: true
  }
);
//Userschema
const User = mongoose.model("User", require(__dirname + "/user"));
//Logschema
const Log = mongoose.model("Log", { logString: String, date: String });
//Groupschema
const Group = mongoose.model("Group", { name: String });
class database {
  //Method to get a user by the username
  static getUser(username, callback) {
    //find the user by the username
    User.findOne({ username: username }, (err, doc) => {
      //if an error occurs, log in to the datbase
      if (err) {
        this.insertLog("Database finderror", () => {});
        return;
      }
      //Return the document
      callback(doc);
    });
  }
  //user by id
  static getUserById(objectId, callback){
       //find the user by the username
       User.findOne({ _id: objectId }, (err, doc) => {
        //if an error occurs, log in to the datbase
        if (err) {
          this.insertLog("Database finderror", () => {});
          return;
        }
        //Return the document
        callback(doc);
      });
  }
  //Used to get all the users
  static getAllUsers(callback) {
    User.find((err, docs) => {
      callback(docs);
    });
  }
  //used to update the user
  static updateUser(user, callback) {
    //Check if the username is taken
    this.getUser(user.username, (doc) => {
      //If yes, return it
      if(doc.username != user.username) return callback("Username is already taken");
      //Update it
      User.findOneAndUpdate({_id: user._id}, user, (err) => {
        if(err) return this.insertLog("Something went wrong updating User: "+ user.username);
        callback();
      })
    });
  }
  static deleteUser(userId, callback) {
    User.findOneAndRemove({_id: userId}, (err) => {
      //If err, return it to the callback
      if(err) return callback(err);
      //log it
      this.insertLog("User with id:'"+userId+"' successfuly deleted", () => {});
      //Go back
      callback();
    })
  }
  //Method to insert a new user
  static insertUser(user, callback) {
    let _user = new User(user);
    //save it
    _user.save(err => {
      if (err) console.log(err);
      //if its successful, log in the log collection
      else {
        this.insertLog(
          "User " + user.username + " successfuly created!",
          () => {}
        );
        callback();
      }
    });
  }
  //Create a new log in the log collection
  static insertLog(logString, callback) {
    let _log = new Log({
      logString: logString,
      date: new Date().toLocaleString()
    });
    _log.save(err => {
      if (err) callback(err);
      else callback();
    });
  }
  //Creates a new Group in the Database
  static insertGroup(group, callback) {
    let _group = new Group(group);
    //check if there is a group
    Group.findOne({ name: group.name }, (err, doc) => {
      if (doc) {
        callback("Error: Groupname is already taken");
        return;
      }
      _group.save((err, group) => {
        if (err) callback(err);
        else {
          this.insertLog(
            "Group: '" +
              group.name +
              "' with id: " +
              group._id +
              " successfuly created",
            () => {}
          );
          //callback the group objectid
          callback(false, group.id);
        }
      });
    });
  }
  //Used to get all the groups
  static getGroups(callback) {
    Group.find((err, docs) => {
      callback(docs);
    });
  }
  //Get the logs
  static getLogs(page, pageSize, date, callback) {
    // set the default pageSize if there is no or invalid pageSize
    if (!pageSize || isNaN(pageSize) || pageSize < 1) {
      pageSize = 10;
    } else {
      pageSize = Number.parseInt(pageSize);
    }
    // If no page is set as GET parameter, we set the page to 0 per default
    if (!page || isNaN(page) || page < 0) {
      page = 0;
    } else {
      page = Number.parseInt(page);
    }
    // Calculate the number of entries to skip (simulate pagination)
    const pageSkip = page * pageSize;
    let mongoFind = null;
    if (date) {
      mongoFind = Log.find({ date: new RegExp(`^${date}.*$`) });
    } else {
      mongoFind = Log.find();
    }
    if (pageSkip > 0) {
      mongoFind = mongoFind.skip(pageSkip);
    }
    mongoFind.limit(pageSize).exec((err, doc) => {
      callback(JSON.stringify(doc));
    });
  }
  // Get count of logs
  static getLogsCount(date, callback) {
    let mongoFind = !date
      ? Log.find()
      : Log.find({ date: new RegExp(`^${date}.*$`) });
    mongoFind.countDocuments((err, count) => {
      callback(count);
    });
  }
  //Delete the group, has to check if the group is empty
  static deleteGroup(objectId, callback) {
    //String-Objectid converted to objectid-object
    const convObjectId = ObjectId(objectId);
    User.find({ group: convObjectId }, (err, doc) => {
      //If a doc is there, callback with the not empty error
      if (doc.length > 0) return callback("Group is not empty");
      //Delete the group
      Group.find({ _id: convObjectId }).remove(() => {
        this.insertLog("Group '" + objectId + "' deleted", () => {
          //Callback to the function
          callback();
        });
      });
    });
  }
}

module.exports = database;
