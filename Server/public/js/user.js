let users = null;
//Function to get the users from the server
function getUser(){
  $.get('/api/getuser', (data, status) => {
    $('.preloader-background').fadeIn('fast');
    $('.loader').fadeIn('fast');
    const userContainer = document.getElementById('users');
    users = JSON.parse(data);
    let htmlToAdd = "";
    users.groups.forEach(group => {
      htmlToAdd += '<ul class="collection with-header"> <li class="collection-header"><div><b>'+group.name+'</b><a href="#" class="secondary-content"><i class="material-icons">add</i></a></div></li>';
      console.log("Created <ul>");
      for(let i = 0; i < users.users.length; i++){
        if(group._id === users.users[i].group){
          htmlToAdd += '<li class="collection-item"><div>'+users.users[i].firstName+' '+users.users[i].lastName+'<a href="#" class="secondary-content"><i class="material-icons">edit</i></a></div></li>';
          console.log("Created User <li>");
        }
      }
      console.log("About to create the </ul>");
      htmlToAdd += '</ul>';
      userContainer.innerHTML = htmlToAdd;
    });
    $('.preloader-background').fadeOut('fast');
    $('.loader').fadeOut('fast');
  })
}



document.addEventListener('DOMContentLoaded', function() {
  let sidenav = M.Sidenav.init(document.querySelectorAll('.sidenav'));

  getUser();
});