const mongoose = require('mongoose');
//Connect to the database
mongoose.connect('mongodb://localhost:27017/lual', {
    //Use the new url parser cause the old one is deprecated
    useNewUrlParser: true
});
//Userschema
const User = mongoose.model('User', require(__dirname + '/user'));
//Logschema
const Log = mongoose.model('Log', {logString: String});
//Groupschema
const Group = mongoose.model('Group', require(__dirname + '/group'));
class database {
    //Method to get a user by the username
    static getUser(username, callback){
        //find the user by the username
        User.findOne({username: username}, (err,doc) => {
            //if an error occurs, log in to the datbase
            if(err){
                this.insertLog('Database finderror');
                return;
            }
            //Return the document
            callback(doc);
        });
    }
    static updateUser(username){

    }
    static deleteUser(username){

    }
    //Method to insert a new user
    static insertUser(user, callback){
        let _user = new User(user);
        //save it
        _user.save(err => {
            if (err) console.log(err);
            //if its successful, log in the log collection
            else {
                this.insertLog("User " + user.username + " successfuly created!");
                callback();
            };
        });
    }
    //Create a new log in the log collection
    static insertLog(logString){
        let _log = new Log({ logString: logString });
        _log.save(err => {
            if (err) console.log(err);
        });
    }
    //Creates a new Group in the Database
    static insertGroup(group, callback){
        let _group = new Group(group);
        _group.save(err =>{
            if (err) console.log(err);
            else {
                this.insertLog("Group: "+ group.name + " with id: " + group.id + " successfuly created");
                callback();
            }
        });
    }
    //Used to get all the groups
    static getGroups(ids){
        //code goes here
    }
}

module.exports = database;