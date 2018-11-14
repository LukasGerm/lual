const express = require('express');
const database = require('../class/database');
const router = express.Router();
const sha512 = require('js-sha512');
//some validation
function validateInput(req, res, next, user) {
    if (user.username.length < 5) {
        return res.send('uToShort');
    }
    //Check if the user is trying to create a user, because sometimes you done have a password when updating a user
    if(req.url === '/createuser'){
        if (user.password.length < 8) {
            return res.send('pToShort');
        }
    }
    else{
        if (user.password === user.retypePassword) {
            delete user.retypePassword;
        }
        else if (user.password !== user.retypePassword) {
            return res.send("pDoNotMatch");
        }
        else {
            //Delete the propertys cause not needed
            delete user.password;
            delete user.retypePassword;
        }
    }
    if (!user.firstName || !user.lastName || !user.roomNumber) {
        return res.send('missing');
    }
}
//Router for getting the logs, returning it as json
router.get('/getlogs', (req, res, next) => {
    database.getLogs(req.query.page, req.query.pageSize, req.query.date, logs => {
        //send the logs
        res.send(logs);
    })
});
//Router for getting the logs count, returning it as json
router.get('/getlogscount', (req, res, next) => {
    database.getLogsCount(req.query.date, count => res.send({ count: count }));
});
//Router for getting the Users and the groups
router.get('/getuser', (req, res, next) => {
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
router.get('/creategroup', (req, res, next) => {
    //If the query is not set return the bad request status
    if (!req.query.name) {
        return res.send(false);
    }
    database.insertGroup({ name: req.query.name }, (error, objectId) => {
        if (error) {
            return res.send(false);
        }
        res.send(objectId);
    });
});
//Router to delete a group
router.get('/deletegroup', (req, res, next) => {
    //Delete the group in the database and check if the group is empty
    database.deleteGroup(req.query.id, (err) => {
        if (err) return res.send(false);
        //send succes and remove the div from the client and send a toast
        res.send('success');
    });
});
//Post method for creating a user
router.post('/createuser', (req, res, next) => {
    //Define user
    let user = req.body;
    //Validation
    if(validateInput(req, res, next, user)) return;
    database.getUser(user.userName, (doc) => {
        //Check if the username is taken
        if (doc) {
            res.send('taken');
        }
        else{
            //encrypt the pw
            user.password = sha512(user.password);
            //set the adminstate
            user.isAdmin = user.isAdmin ? true : false;
            //Set the firstlogin to true
            user.firstLogin = true;
            //insert the user
            database.insertUser(user, () => {
                res.sendStatus(200);
            });
        }
    });
});
//Router for updating a user
router.post('/updateuser', (req, res, next) => {
    //Get the user from the context
    let user = req.body;
    user.isAdmin = user.isAdmin ? true : false;
    //Validate some things
    if(validateInput(req, res, next, user)) return;

    database.updateUser(user, (err) => {
        //If an error occurs, return it
        if (err) res.send(err);
        else res.sendStatus(200);
    })
});

router.get('/deleteuser', (req,res,next) => {
    const deleteUserId = req.query.userId;
    //You cannot delete yourself
    if(deleteUserId === req.session.userId) return res.send("deleteFail");
    database.deleteUser(deleteUserId, (err) => {
        //If an error, send it to the client
        if(err) return res.send(err);
        res.sendStatus(200);
    })
})
module.exports = router;