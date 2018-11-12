let users = null;
let createGroupModal = null;
const userContainer = document.getElementById('users');
let createUserModal = null;
//Function to edit the formular which adds the objectid to the create button
function openUserModal(ObjectId){
  document.getElementById('objectId').value = ObjectId;
  //open the modal
  createUserModal.open();
}
//Method for creating a user via post-ajax
function createUser (){
  //serialize the form to an array
  let postData = $('#createUser').serializeArray();
  //make the post request
  $.post('/api/createuser', postData)
    .done((data) => {
      switch(data){
        //Username to short
        case 'uToShort':
          M.toast({
            html: "Error: Username must be at least 5 characters long",
            classes: "red"
          });
          break;
        //password to short
        case 'pToShort':
          M.toast({
            html: "Error: Password has to be at least 8 characters long",
            classes: "red"
          });
          break;
        //firstname, lastname or room number is missing
        case 'missing':
          M.toast({
            html: "Error: Something in your form is missing, please check",
            classes: "red"
          });  
          break;
        //Username taken
        case 'taken':
          M.toast({
            html: "Error: Username is already taken",
            classes: "red"
          });
          break;
        //Success
        case "OK":
          createUserModal.close();
          getUser();
          M.toast({
            html: "user "+postData.username+" created successfuly",
            classes: "green"
          });
          break;
      }
    });
}
//function to delete a group
function deleteGroup(objectId){
  //Make a get request to delete the group
  $.get('/api/deletegroup?id='+objectId, (data,status) => {
    if(!data){
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
    let deletedGroup = document.getElementById(objectId);
    deletedGroup.parentElement.removeChild(deletedGroup);
    //Code for deleting the div
  });
}
//Function to get the users from the server
function getUser(){
  $.get('/api/getuser', (data, status) => {
    $('.preloader-background').fadeIn('fast');
    $('.loader').fadeIn('fast');
    users = JSON.parse(data);
    let htmlToAdd = "";
    users.groups.forEach(group => {
      htmlToAdd += `<ul class="collection with-header" id="`+group._id+`"> <li class="collection-header"><div><b>`+group.name+`</b><div onclick="deleteGroup('`+group._id+`')" class="secondary-content"><i class="material-icons">remove</i></div><div onclick="openUserModal('`+group._id+`')" class="secondary-content"><i class="material-icons">add</i></div></div></li>`;
      for(let i = 0; i < users.users.length; i++){
        if(group._id === users.users[i].group){
          htmlToAdd += '<li class="collection-item"><div>Username: '+users.users[i].username+'<a href="#" class="secondary-content"><i class="material-icons">edit</i></a></div></li>';
        }
      }
      htmlToAdd += '</ul>';
      //add the html to the container
      userContainer.innerHTML = htmlToAdd;
    });
    $('.preloader-background').fadeOut('fast');
    $('.loader').fadeOut('fast');
  })
}
//Function for creating groups
function createGroup(){
  //The groupname element
  let groupName = document.getElementById('groupName');
  //Check if the name is valid
  if(!groupName.value || groupName.value.length < 5){
    //If not, return a toast
    M.toast({
      html: "Check your groupname. It has to be five characters long.",
      classes: "red"
    });
    return;
  }
  else{
    //if its okay, make a get request with the value
    $.get('/api/creategroup?name='+groupName.value, (data,status) => {
      //If the status is ok, proceed
      if(data){
        M.toast({
          html: "Group successfuly created",
          classes: "green"
        });
        userContainer.innerHTML += `<ul class="collection with-header" id="`+data+`"><li class="collection-header"><div><b>`+groupName.value+`</b><div onclick="deleteGroup('`+data+`')" class="secondary-content"><i class="material-icons">remove</i></div><div href="#" class="secondary-content"><i class="material-icons">add</i></div></div></li>`;
        createGroupModal.close();
        //Here goes code to create the div
      }
      else{
        //What if not
        M.toast({
          html: "Groupname is already taken",
          classes: "red"
        });
      }
    });
  }
}
document.addEventListener('DOMContentLoaded', function() {
  let sidenav = M.Sidenav.init(document.querySelectorAll('.sidenav'));
  //Function for creating the createGroup-Modal
  createGroupModal = M.Modal.init(document.getElementById('createGroupModal'));
  //Init the user modal
  createUserModal = M.Modal.init(document.getElementById('createUserModal'));
  getUser();
});

