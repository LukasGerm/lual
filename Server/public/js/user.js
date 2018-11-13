let users = null;
let createGroupModal = null;
let editUserModal = null;
const userContainer = document.getElementById("users");
let createUserModal = null;

function showToast(data){
  switch (data) {
    //Username to short
    case "uToShort":
      M.toast({
        html: "Error: Username must be at least 5 characters long",
        classes: "red"
      });
      break;
    //password to short
    case "pToShort":
      M.toast({
        html: "Error: Password has to be at least 8 characters long",
        classes: "red"
      });
      break;
    //firstname, lastname or room number is missing
    case "missing":
      M.toast({
        html: "Error: Something in your form is missing, please check",
        classes: "red"
      });
      break;
    //Username taken
    case "taken":
      M.toast({
        html: "Error: Username is already taken",
        classes: "red"
      });
      break;
    //In case the passwords dont match
    case "pDoNotMatch":
      M.toast({
        html: "Error: Passwords do not match",
        classes: "red"
      });
      break;
    //Success
    case "OK":
      createUserModal.close();
      editUserModal.close();
      getUser();
      M.toast({
        html: "Operation successful",
        classes: "green"
      });
      break;
    default:
      M.toast({
        html: data,
        classes: "red"
      });
    break;
    }
}
//Function for opening the user-edit-mode
function openEditUserModal(objectId) {

  document.getElementById('userId').value = objectId;
  //Find the user and return the object
  const user = users.users.find(user => {
    return user._id === objectId;
  });
  //Prepare the form
  document.getElementById('editUserName').value = user.username;
  document.getElementById('editFirstName').value = user.firstName;
  document.getElementById('editLastName').value = user.lastName;
  document.getElementById('editRoomNumber').value = user.roomNumber;
  document.getElementById('editIsAdmin').checked = user.isAdmin;

  //open the edit modal
  editUserModal.open();

}
//make the postrequest
function editUser() {
  let postData = $("#editUserForm").serializeArray();
  $.post("/api/updateuser", postData).done(data => {
    showToast(data);
  });
}
//Function to edit the formular which adds the objectid to the create button
function openUserModal(ObjectId) {
  document.getElementById("objectId").value = ObjectId;
  //open the modal
  createUserModal.open();
}
//Method for creating a user via post-ajax
function createUser() {
  //serialize the form to an array
  let postData = $("#createUser").serializeArray();
  //make the post request
  $.post("/api/createuser", postData).done(data => {
    //Call the function
    showToast(data);
  });
}
//function to delete a group
function deleteGroup(objectId) {
  //Make a get request to delete the group
  $.get("/api/deletegroup?id=" + objectId, (data, status) => {
    if (!data) {
      M.toast({
        html: "Error: Group is not empty",
        classes: "red"
      });
      return;
    }
    M.toast({
      html: "Group successfuly deleted",
      classes: "green"
    });
    //Get the users again
    getUser();
  });
}
//Function to get the users from the server
function getUser() {
  $.get("/api/getuser", (data, status) => {
    $(".preloader-background").fadeIn("fast");
    $(".loader").fadeIn("fast");
    users = JSON.parse(data);
    let htmlToAdd = "";
    users.groups.forEach(group => {
      htmlToAdd +=
        `<ul class="collection with-header" id="` +
        group._id +
        `"> <li class="collection-header"><div><b>` +
        group.name +
        `</b><div onclick="deleteGroup('` +
        group._id +
        `')" class="secondary-content"><i class="material-icons">remove</i></div><div onclick="openUserModal('` +
        group._id +
        `')" class="secondary-content"><i class="material-icons">add</i></div></div></li>`;
      for (let i = 0; i < users.users.length; i++) {
        if (group._id === users.users[i].group) {
          htmlToAdd +=
            `<li class="collection-item"><div>Username: ` +
            users.users[i].username +
            `<div onclick="openEditUserModal('` + users.users[i]._id + `')" class="secondary-content"><i class="material-icons">edit</i></div></div></li>`;
        }
      }
      htmlToAdd += "</ul>";
      //add the html to the container
      userContainer.innerHTML = htmlToAdd;
    });
    //Insert the selectables into the document, but first clear it
    let groupSelect = document.getElementById('groupSelect');
    groupSelect.innerHTML = "";
    users.groups.forEach(group => {
      groupSelect.innerHTML += '<option value="'+group._id+'">'+group.name+'</option>';
    })
    $(".preloader-background").fadeOut("fast");
    $(".loader").fadeOut("fast");
  });
}
//Function for creating groups
function createGroup() {
  //The groupname element
  let groupName = document.getElementById("groupName");
  //Check if the name is valid
  if (!groupName.value || groupName.value.length < 5) {
    //If not, return a toast
    M.toast({
      html: "Check your groupname. It has to be five characters long.",
      classes: "red"
    });
    return;
  } else {
    //if its okay, make a get request with the value
    $.get("/api/creategroup?name=" + groupName.value, (data, status) => {
      //If the status is ok, proceed
      if (data) {
        M.toast({
          html: "Group successfuly created",
          classes: "green"
        });
        getUser();
        createGroupModal.close();
        //Here goes code to create the div
      } else {
        //What if not
        M.toast({
          html: "Groupname is already taken",
          classes: "red"
        });
      }
    });
  }
}
document.addEventListener("DOMContentLoaded", function () {
  let sidenav = M.Sidenav.init(document.querySelectorAll(".sidenav"));
  //Function for creating the createGroup-Modal
  createGroupModal = M.Modal.init(document.getElementById("createGroupModal"));
  //Init the user modal
  createUserModal = M.Modal.init(document.getElementById("createUserModal"));
  //Init the edit user modal
  editUserModal = M.Modal.init(document.getElementById("editUserModal"));
  //Init select
  let select = M.FormSelect.init(document.querySelectorAll('select'));

  getUser();
});
