const express = require('express');
const router = express.Router();


//Router for the Admininterface /a
router.get('/', (req,res,next) => {
    res.render('adminpanel/user');
});
//Using the same rendering, cause on the firstpage, you should review the users
router.get('/user', (req,res,next) => {
    res.render('adminpanel/user');
});
//Router for viewing the logs
router.get('/logs', (req,res,next) => {
    res.render('adminpanel/logs')
});
//router for the downloads page
router.get('/downloads', (req,res,next) => {
    res.render('adminpanel/downloads');
})
module.exports = router;