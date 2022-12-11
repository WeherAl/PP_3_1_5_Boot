//получение данных авторизированного пользователя
async function getPrincipal() {
    let res = await fetch('http://localhost:8080/admin/api/auth');
    return await res.json();
}
//получение списка ролей из БД с добавлением в select
async function getRoles() {
    let roles = await fetch("http://localhost:8080/admin/api/roles");
    roles = await roles.json();
    roles.forEach(role => {
        if (document.querySelector('.form-select').children.length < 3) {
            let option = document.createElement("option");
            option.value = role.id;
            option.text = role.name.substring(5, role.name.length);
            let option1 = option.cloneNode(true);
            let option2 = option.cloneNode(true);
            document.querySelector('#rolesNewUser').add(option);
            document.querySelector('#rolesDelete').add(option1);
            document.querySelector('#rolesEdit').add(option2);
        }
    });
}

//Нажатие на кнопку Edit в таблице юзеров
function onEditButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        const tr = button.parentNode.parentNode;
        document.querySelector('#idEdit').value = tr.children[0].innerHTML;
        document.querySelector('#firstNameEdit').value = tr.children[1].innerHTML;
        document.querySelector('#lastNameEdit').value = tr.children[2].innerHTML;
        document.querySelector('#ageEdit').value = tr.children[3].innerHTML;
        document.querySelector('#emailEdit').value = tr.children[4].innerHTML;
        document.querySelector('#userNameEdit').value = tr.children[5].innerHTML;
        document.querySelector('#passwordEdit').value = '';
        document.querySelector('#editFormBody').ariaModal = 'show';
    })
}
//Нажатие на кнопку Delete в таблице юзеров
function onDeleteButton(button) {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        const tr = button.parentNode.parentNode;
        document.querySelector('#idDelete').value = tr.children[0].innerHTML;
        document.querySelector('#firstNameDelete').value = tr.children[1].innerHTML;
        document.querySelector('#lastNameDelete').value = tr.children[2].innerHTML;
        document.querySelector('#ageDelete').value = tr.children[3].innerHTML;
        document.querySelector('#emailDelete').value = tr.children[4].innerHTML;
        document.querySelector('#userNameDelete').value = tr.children[5].innerHTML;
        document.querySelector('#passwordDelete').value = '';
        document.querySelector('#rolesDelete').ariaModal = 'show';
    })
}

//Преобразование выбранных option-ов в массив ролей
function listOfRoles(options) {
    let res = [];
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            res.push({id: options[i].value, name: "ROLE_" + options[i].text});
        }
    }
    return res;
}

//Отправка запроса на редактирование
async function editModal() {
    document.querySelector('#editBtnSubmit').addEventListener('click', async (e) => {
        e.preventDefault();
        const url = `http://localhost:8080/admin/api/edit`;
        let response = await fetch(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: document.querySelector('#idEdit').value,
                name: document.querySelector('#firstNameEdit').value,
                last_name: document.querySelector('#lastNameEdit').value,
                age: document.querySelector('#ageEdit').value,
                email: document.querySelector('#emailEdit').value,
                username: document.querySelector('#userNameEdit').value,
                password: document.querySelector('#passwordEdit').value,
                roles: listOfRoles(document.querySelector('#rolesEdit'))
            })
        });
        if (response.status === 200) {
            $('.btn-close').click();
            await refreshPage();
            await refreshPrincipalData()
        }
    });
}

//Отправка запроса на удаление
async function deleteModal() {
    document.querySelector('#deleteBtnSubmit').addEventListener('click', async (e) => {
        e.preventDefault();
        const url = `http://localhost:8080/admin/api/delete/${document.querySelector('#idDelete').value}`;
        /*        ${document.querySelector('#editId').value}*/
        let response = await fetch(url, {
            method: "DELETE",
        });

        if (response.status === 200) {
            $('.btn-close').click();
            await refreshPage();
            await refreshPrincipalData()
        }

    });
}

//Таб нового юзера
async function newUser() {
    document.querySelector('#newUserForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const url = 'http://localhost:8080/admin/api/create';
        let response = await fetch(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: document.querySelector('#name').value,
                last_name: document.querySelector('#lastName').value,
                age: document.querySelector('#age').value,
                email: document.querySelector('#email').value,
                username: document.querySelector('#username').value,
                password: document.querySelector('#password').value,
                roles: listOfRoles(document.querySelector('#rolesNewUser'))
            })
        });
        if (response.status === 400) {
            document.querySelector('.warning').style.display = "block";
        } else {
            await refreshPage();
            document.querySelector('.warning').style.display = "none";
            document.querySelector('a.allUsers').classList.add('active');
            document.querySelector('a.newUser').classList.remove('active');
            document.querySelector('#allUsers').classList.add('active');
            document.querySelector('#newUser').classList.remove('active');
            document.querySelector('#newUserForm').reset();
        }
    })
}


//Обновление списка юзеров
async function refreshPage() {
    let response = await fetch("http://localhost:8080/admin/api");
    let users = await response.json();
    document.querySelector('#allUsersTBody').innerHTML = '';
    users.forEach(user => {
        let table = "";
        let roles = user.roles.map(role => role.name.substring(5, role.name.length));
        let rolesInTable = '';
        roles.forEach(role => {
            rolesInTable += `<div>${role}</div>`
        });
        table += `<tr id="tr${user.id}">
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.name}</td>
            <td class="align-middle">${user.last_name}</td>
            <td class="align-middle">${user.age}</td>
            <td class="align-middle">${user.email}</td>
            <td class="align-middle" hidden="true">${user.username}</td>
            <td class="align-middle">${rolesInTable}</td>
            <td class="align-middle"><button class="btn btn-primary btn-sm editBtn" data-toggle="modal" data-target="#editModal">Edit</button></td>
            <td class="align-middle"><button class="btn btn-danger btn-sm deleteBtn" data-toggle="modal" data-target="#deleteModal">Delete</button></td>
            </tr>`;
        document.querySelector('#allUsersTBody').innerHTML += table;
    });
    document.querySelectorAll('.editBtn').forEach(btn => {
        onEditButton(btn);
    });
    document.querySelectorAll('.deleteBtn').forEach(btn => {
        onDeleteButton(btn);
    })
}

//Обновление панели юзера
async function refreshPrincipalData() {
    const tbody = document.querySelector('#userTBody');

    let user = await getPrincipal();

    document.getElementById("adminUsername").textContent = user.username;
    let roles = "";
    user.roles.forEach(role => {
        roles += role.name.substring(5, role.name.length) + " ";
    })
    document.getElementById("adminRoles").textContent = roles;

    roles = user.roles.map(role => role.name.substring(5, role.name.length));
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


refreshPage()
refreshPrincipalData()
getRoles()
editModal()
deleteModal()
newUser()

