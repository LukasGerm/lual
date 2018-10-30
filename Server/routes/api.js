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

module.exports = router;