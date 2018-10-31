const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.Types.ObjectId;
let user = {
    username: String,
    password: String,
    firstName: String,
    lastName: String,
    roomNumber: String,
    group: ObjectId,
    isAdmin: Boolean,
    firstLogin: Boolean
}

module.exports = user;