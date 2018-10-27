const express = require('express');
const router = express.Router();


//Router for the Admininterface /a
router.get('/', (req,res,next) => {
    res.send("Success");
});

module.exports = router;