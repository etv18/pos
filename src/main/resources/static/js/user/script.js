import {makeRequestToBackend, validateFields} from "../utils/common.js";

const tblUsers = document.getElementById("tblUsers");

//ELEMENTS OF *** CREATE *** USER MODAL
const txtPassword = document.getElementById("txtPassword");
const txtUsername = document.getElementById("txtUsername");
const txtFullName = document.getElementById("txtFullName");
const rolesGroup  = document.getElementById("rolesGroup");
const btnSaveNewUser = document.getElementById("btnSaveNewUser");

//ELEMENTS OF *** UPDATE *** USER MODAL
const updateProductModalElem = document.getElementById("updateProductModal");
const txtUpdatePassword = document.getElementById("txtUpdatePassword");
const txtUpdateUsername = document.getElementById("txtUpdateUsername");
const txtUpdateFullName = document.getElementById("txtUpdateFullName");
const updateRolesGroup  = document.getElementById("updateRolesGroup");
const userIsActive = document.getElementById("userIsActive");
const btnSaveChangesUser = document.getElementById("btnSaveChangesUser");
const spanUsername = document.getElementById("spanUsername");

let userRoles = [];
let renderedUserRoles = [];
let updateUserRoles = [];

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

async function updateUser() {
    const updateUserRequest= {
        username: txtUpdateUsername.value,
        oldUsername: spanUsername.textContent,
        fullName: txtUpdateFullName.value,
        password: txtUpdatePassword.value,
        active: userIsActive.checked,
        roles: updateUserRoles || []
    }

    return await makeRequestToBackend(usersUrl, updateUserRequest, "PUT");
}

async function deleteUser(event){
    const target = event.target.closest(".delete-user");
    if(!target) return;
    const username = target.getAttribute("data-user-username");
    const fullUrl = usersUrl + "/" + username;
    const response = await fetch(fullUrl, {method: "DELETE"});
    if(!response.ok) {
        alert("Something went wrong: " + data.message);
        return;
    }
    window.location.reload();
}

function modifyUserRoles(event, roleList){
    const target = event.target.closest(".form-check");
    if(!target) return;

    const checkBoxInput = target.querySelector(".form-check-input");
    if (!checkBoxInput) return;

    const role = checkBoxInput.getAttribute("data-role-name");

    if(roleList.includes(role)){
        roleList = roleList.filter(item => item !== role);
    } else {
        roleList.push(role);
    }
    return roleList;
}

function getUserActiveRolesFromEditBtn(str){

    const formattedStr = str.replace(/[\[\]]/g, "") //Remove the square brackets [ ] using replace with a regex
    let result = new Map();
    if(formattedStr.includes(",")){
        let roleList = formattedStr.split(",");
        roleList.forEach(item => result.set(item.trim(), true));
        return result;
    }
    return result.set(formattedStr.trim(), true);
}

function createRoleElement(role, checked) {
    const wrapper = document.createElement("div");
    wrapper.innerHTML = `
       <div class="form-check">
           <input class="form-check-input" type="checkbox" data-role-name="${role}" ${checked ? "checked" : ""}>
           <label class="role-label form-check-label">${role}</label>
       </div>
    `;
    return wrapper;
}

function drawUserRolesInUpdateModal(strListOfUserRoles, mapOfUserRoles){
    updateRolesGroup.innerHTML = '';
    strListOfUserRoles.forEach(role => {
        const userHasThisRole = mapOfUserRoles.has(role);
       if(userHasThisRole){
           updateRolesGroup.appendChild(createRoleElement(role, userHasThisRole));
           updateUserRoles.push(role);
           return;
       }
        updateRolesGroup.appendChild(createRoleElement(role, userHasThisRole));
    });
}

function showUpdateUserModal(event){
    const target = event.target.closest(".edit-user");
    if(!target) return;
    updateUserRoles = [];

    spanUsername.textContent = target.getAttribute("data-user-username");
    txtUpdateUsername.value = target.getAttribute("data-user-username");
    txtUpdateFullName.value = target.getAttribute("data-user-fullname");
    userIsActive.checked = target.getAttribute("data-user-active").toLowerCase() === "true";
    let rolesFromDataProperty = target.getAttribute("data-user-roles"); //This a string

    const mapOfUserRoles = getUserActiveRolesFromEditBtn(rolesFromDataProperty);
    drawUserRolesInUpdateModal(renderedUserRoles, mapOfUserRoles);

    const modal = new bootstrap.Modal(updateProductModalElem);
    modal.show();
}

function getRenderedRoles(){
    const roleLabels = rolesGroup.querySelectorAll(".role-label");
    let renderedRoles = [];
    roleLabels.forEach(label => {
        renderedRoles.push(label.textContent);
    });
    return renderedRoles;
}

rolesGroup.addEventListener("change", e => {
  const modifiedListOfUserRoles = modifyUserRoles(e, userRoles);
  userRoles = modifiedListOfUserRoles;
});

updateRolesGroup.addEventListener("change", e => {
    const modifiedListOfUpdateUserRoles = modifyUserRoles(e, updateUserRoles);
    updateUserRoles = modifiedListOfUpdateUserRoles;
    console.log(updateUserRoles);

})
btnSaveNewUser.addEventListener("click", async e => {
    const inputs = [txtPassword, txtUsername, txtFullName];
    if(!validateFields(inputs)) return;
    const userResponse = await createUser();
    if(userResponse) window.location.reload();

});

btnSaveChangesUser.addEventListener("click", async e => {
    const inputs = [txtUpdateUsername, txtUpdateFullName];
    if(!validateFields(inputs)) return;
    const userResponse = await updateUser();
    if(userResponse) window.location.reload();
});

tblUsers.addEventListener("click", async e => {
   showUpdateUserModal(e);
   await deleteUser(e);
});

document.addEventListener("DOMContentLoaded", e => {
    renderedUserRoles = getRenderedRoles();
    console.log(renderedUserRoles);
});
