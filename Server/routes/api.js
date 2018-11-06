const express = require('express');
const database = require('../class/database');
const router = express.Router();
//Router for getting the logs, returning it as json
router.get('/getlogs', (req,res,next) => {
    database.getLogs(req.query.page, req.query.pageSize, logs => {
        //send the logs
        res.send(logs);
    })
});
//Router for getting the logs count, returning it as json
router.get('/getlogscount', (req,res,next) => {
    database.getLogsCount(count => res.send({count: count}));
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
    database.insertGroup({name: req.query.name}, (error) => {
        if(error){
            return res.send(false);
        }
        res.send('success');
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
module.exports = router;