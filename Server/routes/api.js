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
module.exports = router;