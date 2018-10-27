const mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/lual');
const User = mongoose.model('User', require(__dirname + '/user'));
const Log = mongoose.model('Log', {logString: String});
class database {
    static getUser(username){
        User.findOne()
    }
    static updateUser(username){

    }
    static deleteUser(username){

    }
    static insertUser(user){
        let _user = new User(user);
        _user.save(err => {
            if (err) console.log(err);
            else this.insertLog("User " + user.username + " successfuly created!");
        });
    }
    static insertLog(logString){
        let _log = new Log({ logString: logString });
        _log.save(err => {
            if (err) console.log(err);
        })
    }
}

module.exports = database;