
const formEditUser = document.querySelector('#formEdit'); // Получаю форму с дома-дерева.(форма правки юзверя)

const redactUser = ({id, name, lastName, age, email, password}) => {
    getFormItemByName("idEdit").value = id;
    getFormItemByName("nameEdit").value = name;
    getFormItemByName("lastNameEdit").value = lastName;
    getFormItemByName("ageEdit").value = age;
    getFormItemByName("emailEdit").value = email;
    getFormItemByName("passwordEdit").value = password;
}

const getFormItemByName = (name) => {
    return formEditUser[name];
}

const getFormValues = () => {
    return {
        id: getFormItemByName("idEdit").value,
        name: getFormItemByName("nameEdit").value,
        lastName: getFormItemByName("lastNameEdit").value,
        age: getFormItemByName("ageEdit").value,
        email: getFormItemByName("emailEdit").value,
        password: getFormItemByName("passwordEdit").value,
    }
}

const getRoles = () => {
    const editRole = document.querySelector('#roleEdit').selectedOptions;
    const roles = [];
    for (let i = 0; i < editRole.length; i++) {
        roles.push({
            id: Number(editRole[i].value)
        })
        // roles.push(
        //     Number(editRole[i].value)
        // )
    }
    return roles;
}

const closeEditModal = () => {
    const editModalClose = document.querySelector('#editModalClose');
    editModalClose.click();
}

const editModal = async (id) => {
    console.log(`Id is ->>> ${id}`);
    const response = await fetch(`http://localhost:8080/api/admin/get_user_by_id?id=${id}`);
    const user = await response.json();
    redactUser(user);
}
// Слушает событие 'submit' выведится колбек(функция передающаяся другому коду(в данном случае addEventListener)) в теле функции.
formEditUser.addEventListener('submit', async (event) => {
    event.preventDefault();

    console.log("ДАнные для Данича специально для вас", {
        ...getFormValues(),
        roles: getRoles()
    })
    const response = await fetch(`http://localhost:8080/api/admin/update`, 
    {
        method: 'PATCH',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            ...getFormValues(),
            roles: getRoles()
        })
    }
    );
    getAllUsers();
    closeEditModal();
})
