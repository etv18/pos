import {makeRequestToBackend} from "../utils/common.js";

const txtPassword = document.getElementById("txtPassword");
const txtUsername = document.getElementById("txtUsername");
const txtFullName = document.getElementById("txtFullName");
const rolesGroup  = document.getElementById("rolesGroup");
const btnSaveNewUser = document.getElementById("btnSaveNewUser");

let userRoles = [];
const usersUrl = "/api/v1/users";

async function createUser(){
    const createUserRequest = {
        username: txtUsername.value,
        fullName: txtFullName.value,
        password: txtPassword.value,
        roles: userRoles
    }

    return await makeRequestToBackend(usersUrl, createUserRequest, "POST");
}

function modifyUserRoles(event){
    const target = event.target.closest(".form-check");
    if(!target) return;

    const checkBoxInput = target.querySelector(".form-check-input");
    if (!checkBoxInput) return;

    const role = checkBoxInput.getAttribute("data-role-name");

    if(userRoles.includes(role)){
        userRoles = userRoles.filter(item => item !== role);
    } else {
        userRoles.push(role);
    }
    console.log(userRoles);
}

rolesGroup.addEventListener("change", e => {
   modifyUserRoles(e);
});

btnSaveNewUser.addEventListener("click", async e => {
    const userResponse = await createUser();
    if(userResponse) window.location.reload();
});
