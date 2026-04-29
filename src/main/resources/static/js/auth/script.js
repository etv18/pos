const btnSignIn = document.getElementById("btnSignIn");
const loginForm = document.getElementById("loginForm");
const txtUsername = document.getElementById("txtUsername");
const txtPassword = document.getElementById("txtPassword");
const loginEndpoint = "http://localhost:8080/api/v1/auth/login";

async function logIn(username, password){
    const res = await fetch(loginEndpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    });

    const data = await res.json();

    if(!res.ok){
        alert(data.message);
        return;
    }

    window.location.href = data.url;
}

btnSignIn.addEventListener("click", async e => {
    await logIn(txtUsername.value, txtPassword.value);
});
