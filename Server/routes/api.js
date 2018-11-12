const express = require('express');
const database = require('../class/database');
const router = express.Router();
const sha512 = require('js-sha512');
//Router for getting the logs, returning it as json
router.get('/getlogs', (req,res,next) => {
    database.getLogs(req.query.page, req.query.pageSize, req.query.date, logs => {
        //send the logs
        res.send(logs);
    })
});
//Router for getting the logs count, returning it as json
router.get('/getlogscount', (req,res,next) => {
    database.getLogsCount(req.query.date, count => res.send({count: count}));
});
//Router for getting the Users and the groups
router.get('/getuser', (req,res,next) => {
    //Variable that gets responsed
    let response = {};
    //Callback hell ftw
    database.getGroups((groups) => {
        response.groups = groups;
        database.getAllUsers(users => {
            response.users = users;
            res.send(JSON.stringify(response));
        });
    });
})

//Router for creating groups
router.get('/creategroup', (req,res,next) => {
    //If the query is not set return the bad request status
    if(!req.query.name){
        return res.send(false);
    }
    database.insertGroup({name: req.query.name}, (error, objectId) => {
        if(error){
            return res.send(false);
        }
        res.send(objectId);
    });
});
//Router to delete a group
router.get('/deletegroup', (req,res,next) => {
    //Delete the group in the database and check if the group is empty
    database.deleteGroup(req.query.id, (err) => {
        if(err) return res.send(false);
        //send succes and remove the div from the client and send a toast
        res.send('success');
    });
});
//Post method for creating a user
router.post('/createuser', (req,res,next) => {
    //Define user
    let user = req.body;
    //Validation
    if(user.username.length < 5){
        return res.send('uToShort');
    }
    else if(user.password.length < 8){
        return res.send('pToShort');
    }
    else if(!user.firstName || !user.lastName || !user.roomNumber){
        return res.send('missing');
    }
    database.getUser(user.userName, (doc) => {
        //Check if the username is taken
        if(doc){
            return res.send('taken');
        }
        //encrypt the pw
        user.password = sha512(user.password);
        //set the adminstate
        user.isAdmin = user.isAdmin ? true : false;
        //Set the firstlogin to true
        user.firstLogin = true;
        //insert the user
        database.insertUser(user, () => {
            return res.sendStatus(200);
        });
    });
});
module.exports = router;