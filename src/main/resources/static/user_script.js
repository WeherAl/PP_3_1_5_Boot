async function auth() {
    let res = await fetch('http://localhost:8080/user/api');
    return await res.json();
}

async function userPanel() {
    let user = await auth();
    document.getElementById("adminUsername").textContent = user.username;
    let roles = "";
        user.roles.forEach(role => {
            roles += role.name.substring(5, role.name.length) + " ";
        })
        document.getElementById("adminRoles").textContent = roles;
}

//Обновление панели юзера
async function refreshUserPanel() {
    const tbody = document.querySelector('#userTBody');
    let user = await auth();
    let roles = user.roles.map(role => role.name.substring(5, role.name.length));
    let rolesInTable = '';
    roles.forEach(role => {
        rolesInTable += `<div>${role}</div>`
    });

    tbody.innerHTML = `<tr>
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.name}</td>
            <td class="align-middle">${user.last_name}</td>
            <td class="align-middle">${user.age}</td>
            <td class="align-middle">${user.email}</td>
            <td class="align-middle">${rolesInTable}</td>
            </tr>`;
}

userPanel();
refreshUserPanel();


/*  */


