const formNewUser = document.querySelector('#addUser');

const getRolesForNewUser = () => {
    const newRole = document.querySelector('#role').selectedOptions;
    const roles = [];
    for (let i = 0; i < newRole.length; i++) {
        roles.push({
            id: newRole[i].value
        })
    }
    return roles;
}

const getNewFormItemByName = (name) => {
    return formNewUser[name];
}

const getNewFormValues = () => {
    return {
        lastName: getNewFormItemByName("username").value,
        name: getNewFormItemByName("name").value,
        age: getNewFormItemByName("age").value,
        email: getNewFormItemByName("email").value,
        password: getNewFormItemByName("password").value,
    }
}


const resetNewForm = () => {
    formNewUser.reset();
}


formNewUser.addEventListener('submit', async (event) => {
    event.preventDefault();
    const tableAdmin = document.querySelector('#tableAdmin');
    const response = await fetch("http://localhost:8080/api/admin/create_user", {
        method: 'POST',
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            ...getNewFormValues(),
            roles: getRolesForNewUser()
        })
    });
    resetNewForm();
    getAllUsers();
    tableAdmin.click();
})