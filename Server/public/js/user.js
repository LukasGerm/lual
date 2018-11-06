let users = null;
let createGroupModal = null;
const userContainer = document.getElementById('users');
//function to delete a group
function deleteGroup(objectId){
  console.log(objectId);
}
//Function to get the users from the server
function getUser(){
  $.get('/api/getuser', (data, status) => {
    $('.preloader-background').fadeIn('fast');
    $('.loader').fadeIn('fast');
    users = JSON.parse(data);
    let htmlToAdd = "";
    users.groups.forEach(group => {
      htmlToAdd += `<ul class="collection with-header"> <li class="collection-header"><div><b>`+group.name+`</b><div onclick="deleteGroup('`+group._id+`')" class="secondary-content"><i class="material-icons">remove</i></div><div href="#" class="secondary-content"><i class="material-icons">add</i></div></div></li>`;
      for(let i = 0; i < users.users.length; i++){
        if(group._id === users.users[i].group){
          htmlToAdd += '<li class="collection-item"><div>'+users.users[i].firstName+' '+users.users[i].lastName+'<a href="#" class="secondary-content"><i class="material-icons">edit</i></a></div></li>';
        }
      }
      htmlToAdd += '</ul>';
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
      if(data == 'success'){
        M.toast({
          html: "Group successfuly created",
          classes: "green"
        });
        userContainer.innerHTML += '<ul class="collection with-header"> <li class="collection-header"><div><b>'+groupName.value+'</b><a href="#" class="secondary-content"><i class="material-icons">add</i></a></div></li></ul>';
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
  getUser();
});

