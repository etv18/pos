const btnLogout = document.getElementById("btnLogOut");
const endPoint = "/api/v1/auth/logout";
async function logOutUser(){
    const res = await fetch(endPoint);
    const data = await res.json();

    if(!res.ok){
        alert(data.message);
    }

    window.location.href = "/auth/login";
}

btnLogout.addEventListener("click", async e => {
    await logOutUser();
});