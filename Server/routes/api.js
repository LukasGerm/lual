const express = require('express');
const database = require('../class/database');
const router = express.Router();
//Router for getting the logs, returning it as json
router.get('/getlogs', (req,res,next) => {
    database.getLogs(logs => {
        //send the logs
        res.send(logs);
    })
});

module.exports = router;